package com.example.demo.src.openHour;


import com.example.demo.src.openHour.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class OpenHourDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //리스트로 나오게 만든 것
    public DeliveryOpenHour deliveryOpenHour(int restaurantId){
        String deliveryOpenHourQuery = "select day,\n" +
                "       CASE WHEN start IS NULL THEN '휴무' else start END AS startTime,\n" +
                "       CASE WHEN end IS NULL THEN '휴무' else end END AS endTime,\n" +
                "       CASE WHEN breakStart IS NULL THEN '브레이크 없음' else breakStart END AS breakStartTime,\n" +
                "       CASE WHEN breakEnd IS NULL THEN '브레이크 없음' else breakEnd END AS breakEndTime\n" +
                "from OpeningHours\n" +
                "where restaurantId = ?";//이 쿼리를 디비에 날리고
        String temp = "select restaurantName from OpeningHours where restaurantId = ? limit 1";
        String RestaurantName = jdbcTemplate.queryForObject(temp, String.class, restaurantId);
        return new DeliveryOpenHour (
                restaurantId,
                RestaurantName,
                this.jdbcTemplate.query(deliveryOpenHourQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("day"),
                                rs.getString("startTime"),
                                rs.getString("endTime"),
                                rs.getString("breakStartTime"),
                                rs.getString("breakEndTime")
                        ),
                        restaurantId));
    }

    //카트 유무 검사
    public int checkOpenHour(int restaurantId){
        String checkOpenHourQuery = "select exists(select openId from OpeningHours where restaurantId = ?)";
        return this.jdbcTemplate.queryForObject(checkOpenHourQuery,
                int.class,
                restaurantId);

    }





    /*


    //이메일 중복검사
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }



    //비밀번호 조회
    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userId,passwd,email,name,idUser from User where idUser = ?";
        String getPwdParams = postLoginReq.getUserId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("idUser"),
                        rs.getString("email"),
                        rs.getString("passwd")),
                getPwdParams
                );

    }*/


    /*

    //생성
    public int createOpenHour(PostOpenHourReq postOpenHourReq){//배송지 생성하는 부분.
        String createOpenHourQuery = "insert into Coupon (userId, restaurantId, menuId, menuNum) VALUES (?,?,?,?)";
        Object[] createOpenHourParams = new Object[]{postOpenHourReq.getUserId(), postOpenHourReq.getRestaurantId(), postOpenHourReq.getMenuId(), postOpenHourReq.getMenuNum()};
        this.jdbcTemplate.update(createOpenHourQuery, createOpenHourParams);//쿼리로 유저 생성한 후,
        String lastInsertOpenHourQuery = "select last_insert_id()";//마지막으로 insert된 id를 반환한다.
        return this.jdbcTemplate.queryForObject(lastInsertOpenHourQuery,int.class);
    }


    //상태 수정 == 삭제
    public int modifyOpenHourStatus(StatusOpenHour statusOpenHour){
        String modifyOpenHourStatusQuery = "update Coupon set status = ? where couponId = ? ";
        Object[] modifyOpenHourStatusParams = new Object[]{statusOpenHour.getStatus(), statusOpenHour.getOpenHourId()};
        return this.jdbcTemplate.update(modifyOpenHourStatusQuery,modifyOpenHourStatusParams);
    }

    //메뉴 수량 수정
    public int modifyOpenHourMenuNum(PatchOpenHour patchOpenHour){
        String modifyOpenHourMenuNumQuery = "update Coupon set menuNum = ? where couponId = ? ";
        Object[] modifyOpenHourMenuNumParams = new Object[]{patchOpenHour.getMenuNum(), patchOpenHour.getOpenHourId()};

        return this.jdbcTemplate.update(modifyOpenHourMenuNumQuery,modifyOpenHourMenuNumParams);
    }

     */





}
