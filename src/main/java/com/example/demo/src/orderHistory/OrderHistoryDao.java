package com.example.demo.src.orderHistory;


import com.example.demo.src.orderHistory.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class OrderHistoryDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //리스트로 나오게 만든 것
    public DeliveryOrderHistory deliveryOrderHistory(int userId){
        String deliveryOrderHistoryQuery = "select R.name, O.created,\n" +
                "       CASE WHEN O.status = 'Complete' THEN '배달 완료'\n" +
                "        WHEN O.status = 'Valid' THEN '주문 완료'\n" +
                "        WHEN O.status = 'Invalid' THEN '주문 취소' END AS 'deliveryStatus',\n" +
                "        Re.score\n" +
                "from OrderHistory O\n" +
                "left join Restaurant R on R.restaurantId = O.restaurantId\n" +
                "left join Review Re on Re.orderId = O.orderId\n" +
                "where O.userId = ?";//이 쿼리를 디비에 날리고



        return new DeliveryOrderHistory (
                this.jdbcTemplate.query(deliveryOrderHistoryQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("R.name"),
                                rs.getString("O.created"),
                                rs.getString("deliveryStatus"),
                                rs.getInt("Re.score")),
                        userId)
        );
    }


    //카트 유무 검사
    public int checkOrderHistory(int userId){
        String checkOrderHistoryQuery = "select exists(select orderId from OrderHistory where userId = ?)";
        return this.jdbcTemplate.queryForObject(checkOrderHistoryQuery,
                int.class,
                userId);

    }


}
