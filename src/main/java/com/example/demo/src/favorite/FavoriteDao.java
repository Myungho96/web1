package com.example.demo.src.favorite;


import com.example.demo.src.favorite.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class FavoriteDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //리스트로 나오게 만든 것
    public DeliveryFavorite deliveryFavorite(int userId){
        String deliveryFavoriteQuery = "select restaurantName,image from Favorites where userId = ? && status = 'valid'";//이 쿼리를 디비에 날리고

        return new DeliveryFavorite (
                userId,
                //this.jdbcTemplate.query(deliveryFavoriteQuery, (rs, rowNum) -> rs.getString("restaurantName"),userId)

                this.jdbcTemplate.query(deliveryFavoriteQuery,
                        (rs, rowNum) -> new NameImage(
                                rs.getString("restaurantName"),
                                rs.getString("image")),
                        userId));
    }


    //유저 인덱스 있는지 검사
    public int checkUserId(int favoriteId){
        String checkUserIdQuery = "select userId from Favorites where favoriteId = ?";
        int checkUserIdParams = favoriteId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserIdParams);

    }



    //생성
    public int createFavorite(PostFavoriteReq postFavoriteReq){//배송지 생성하는 부분.
        String createFavoriteQuery = "insert into Favorites (userId, restaurantId, restaurantName, image) VALUES (?,?,?,?)";
        Object[] createFavoriteParams = new Object[]{postFavoriteReq.getUserId(), postFavoriteReq.getRestaurantId(), postFavoriteReq.getRestaurantName(), postFavoriteReq.getImage()};
        this.jdbcTemplate.update(createFavoriteQuery, createFavoriteParams);//쿼리로 유저 생성한 후,
        String lastInsertFavoriteQuery = "select last_insert_id()";//마지막으로 insert된 id를 반환한다.
        return this.jdbcTemplate.queryForObject(lastInsertFavoriteQuery,int.class);
    }


    //상태 수정 == 삭제
    public int modifyFavoriteStatus(StatusFavorite statusFavorite){
        String modifyFavoriteStatusQuery = "update Favorites set status = ? where favoriteId = ? ";
        Object[] modifyFavoriteStatusParams = new Object[]{statusFavorite.getStatus(), statusFavorite.getFavoriteId()};
        return this.jdbcTemplate.update(modifyFavoriteStatusQuery,modifyFavoriteStatusParams);
    }

    public int checkStatus(int userId){
        String checkStatusQuery = "select exists(select * from User where userId = ? && status='Valid')";
        int checkStatusParams = userId;
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                int.class,
                checkStatusParams);

    }

    public int checkRestaurantStatus(int restaurantId){
        String checkStatusQuery = "select exists(select * from Restaurant where restaurantId = ? && status='Valid')";
        int checkStatusParams = restaurantId;
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                int.class,
                checkStatusParams);

    }

    public String checkRestaurantName(int restaurantId){
        String checkStatusQuery = "select name from Restaurant where restaurantId = ? && status='Valid'";
        int checkStatusParams = restaurantId;
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                String.class,
                checkStatusParams);

    }

    public String checkFavoriteStatus(int favoriteId){
        String checkStatusQuery = "select status from Favorites where favoriteId = ?";
        int checkStatusParams = favoriteId;
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                String.class,
                checkStatusParams);

    }



}
