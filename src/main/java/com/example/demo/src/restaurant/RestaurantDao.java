package com.example.demo.src.restaurant;


import com.example.demo.src.restaurant.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class RestaurantDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //리스트로 나오게 만든 것
    public DeliveryRestaurant deliveryRestaurant(){
        String deliveryRestaurantQuery = "select name,delivery,time,totalScore,image,cheetah from Restaurant where status='Valid'";//이 쿼리를 디비에 날리고
        return new DeliveryRestaurant (
                this.jdbcTemplate.query(deliveryRestaurantQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("name"),
                                rs.getInt("delivery"),
                                rs.getInt("time"),
                                rs.getFloat("totalScore"),
                                rs.getString("image"),
                                rs.getString("cheetah")
                        )));
    }

    //리스트로 나오게 만든 것
    public DeliveryRestaurant deliveryRestaurantByCheetah(String cheetah){
        String deliveryRestaurantQuery = "select name,delivery,time,totalScore,image,cheetah from Restaurant where cheetah = ? && status='Valid'";//이 쿼리를 디비에 날리고
        return new DeliveryRestaurant (
                this.jdbcTemplate.query(deliveryRestaurantQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("name"),
                                rs.getInt("delivery"),
                                rs.getInt("time"),
                                rs.getFloat("totalScore"),
                                rs.getString("image"),
                                rs.getString("cheetah")
                        ),
                        cheetah));
    }

    //리스트로 나오게 만든 것
    public DeliveryRestaurant deliveryRestaurantBycategoryId(int categoryId){
        String deliveryRestaurantQuery = "select name,delivery,time,totalScore,image,cheetah from Restaurant R left join RestaurantCategory RC on R.restaurantId = RC.restaurantId where RC.categoryId = ? && RC.status='Valid' && R.status='Valid'";//이 쿼리를 디비에 날리고
        return new DeliveryRestaurant (
                this.jdbcTemplate.query(deliveryRestaurantQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("name"),
                                rs.getInt("delivery"),
                                rs.getInt("time"),
                                rs.getFloat("totalScore"),
                                rs.getString("image"),
                                rs.getString("cheetah")
                        ),
                        categoryId));
    }
    //음식점 상세조회
    public DeliveryRestaurantDetail deliveryRestaurantDetail(int restaurantId){
        String deliveryRestaurantQuery = "select name,location,boss,registNum,introduce,phone,origin from Restaurant where restaurantId=? && status='Valid'";//이 쿼리를 디비에 날리고
        return new DeliveryRestaurantDetail (
                this.jdbcTemplate.query(deliveryRestaurantQuery,
                        (rs, rowNum) -> new DetailList(
                                rs.getString("name"),
                                rs.getString("location"),
                                rs.getString("boss"),
                                rs.getString("registNum"),
                                rs.getString("introduce"),
                                rs.getString("phone"),
                                rs.getString("origin")
                        ),
                        restaurantId));
    }





    public int checkAllRestaurant(){
        String checkRestaurantQuery = "select exists(select restaurantId from Restaurant where status='Valid')";
        return this.jdbcTemplate.queryForObject(checkRestaurantQuery,
                int.class);

    }



    //특정 음식점 유무 검사
    public int checkRestaurant(int restaurantId){
        String checkRestaurantQuery = "select exists(select restaurantId from Restaurant where restaurantId = ? && status='Valid')";
        return this.jdbcTemplate.queryForObject(checkRestaurantQuery,
                int.class,
                restaurantId);

    }

    //특정카테고리 음식점 유무 검사
    public int checkRestaurantCategoryId(int categoryId){
        String checkRestaurantQuery = "select exists(select restaurantId from RestaurantCategory where categoryId = ? && status='Valid')";
        return this.jdbcTemplate.queryForObject(checkRestaurantQuery,
                int.class,
                categoryId);

    }

    //치타 음식점 유무 검사
    public int checkRestaurantCheetah(String cheetah){
        String checkRestaurantQuery = "select exists(select restaurantId from Restaurant where cheetah = ? && status='Valid')";
        return this.jdbcTemplate.queryForObject(checkRestaurantQuery,
                int.class,
                cheetah);

    }








}
