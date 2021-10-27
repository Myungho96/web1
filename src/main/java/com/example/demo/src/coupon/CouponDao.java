package com.example.demo.src.coupon;


import com.example.demo.src.coupon.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class CouponDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //리스트로 나오게 만든 것
    public DeliveryCoupon deliveryCoupon(int userId){
        String deliveryCouponQuery = "select C.name, C.discount, CASE WHEN R.name IS NULL THEN '전체식당' ELSE R.name end as 'canUse'\n" +
                "from CouponUser CU\n" +
                "    left join Coupon C on CU.couponId = C.couponId\n" +
                "    left join Restaurant R on C.restaurantId = R.restaurantId\n" +
                "where CU.userId = ? && C.status = 'Valid'";//이 쿼리를 디비에 날리고
        return new DeliveryCoupon (
                userId,
                this.jdbcTemplate.query(deliveryCouponQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("C.name"),
                                rs.getInt("C.discount"),
                                rs.getString("canUse")),
                        userId));
    }

    //카트 유무 검사
    public int checkCoupon(int userId){
        String checkCouponQuery = "select exists(select couponId from CouponUser where userId = ? && status = 'Valid')";
        return this.jdbcTemplate.queryForObject(checkCouponQuery,
                int.class,
                userId);

    }



    //상태 수정 == 삭제
    public int modifyCouponStatus(StatusCoupon statusCoupon){
        String modifyCouponUserStatusQuery = "update CouponUser set status = ? where couponId = ? && userId = ? ";
        Object[] modifyCouponStatusParams = new Object[]{statusCoupon.getStatus(), statusCoupon.getCouponId(), statusCoupon.getUserId()};
        return this.jdbcTemplate.update(modifyCouponUserStatusQuery,modifyCouponStatusParams);
    }


    public String checkCouponStatus(int couponId, int userId){
        String checkStatusQuery = "select status from CouponUser where couponId = ? && userId = ?";
        Object[] checkStatusParams = new Object[]{couponId, userId};
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                String.class,
                checkStatusParams);

    }




}
