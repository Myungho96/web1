package com.example.demo.src.restaurant;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리. get은 다 여기로 온다.
@Service
public class RestaurantProvider {//RestaurantProvider 호출하면 이 함수가 호출되는데,

    private final RestaurantDao restaurantDao;//필요한 객체를 정의한다. final 키워드는 엔티티를 한 번만 할당함. 두번 할당하면 오류 발생.
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());//로그를 찍기 위해 하는 것이라고 함.

    @Autowired
    public RestaurantProvider(RestaurantDao restaurantDao, JwtService jwtService) {//실질적인 쿼리문은 restaurantDao에 있으며, Jwt는 자바 웹 토큰이라고 비밀번호를 암호화하는 데 쓰는 듯 하다.
        this.restaurantDao = restaurantDao;//각 객체를 생성하지 않고 받아온다.
        this.jwtService = jwtService;
    }


    //전체 레스토랑 검색
    public DeliveryRestaurant deliveryRestaurant() throws BaseException {
        //전체 레스토랑이 존재하지 않을 경우

        if(checkAllRestaurant() == 0){
            throw new BaseException(RESTAURANT_ERROR);
        }



        try {
            DeliveryRestaurant deliveryRestaurant = restaurantDao.deliveryRestaurant();
            return deliveryRestaurant;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //치타배달이 되는 음식점 조회
    public DeliveryRestaurant deliveryRestaurantByCheetah(String cheetah) throws BaseException {
        //
        if(checkRestaurantCheetah(cheetah) == 0){
            throw new BaseException(RESTAURANT_ERROR);
        }

        try {
            DeliveryRestaurant deliveryRestaurant = restaurantDao.deliveryRestaurantByCheetah(cheetah);
            return deliveryRestaurant;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //특정 카테고리 음식점 조회
    public DeliveryRestaurant deliveryRestaurantBycategoryId(int categoryId) throws BaseException {
        //
        if(checkRestaurantCategoryId(categoryId) == 0){
            throw new BaseException(RESTAURANT_ERROR);
        }

        try {
            DeliveryRestaurant deliveryRestaurant = restaurantDao.deliveryRestaurantBycategoryId(categoryId);
            return deliveryRestaurant;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public DeliveryRestaurantDetail deliveryRestaurantDetail(int restaurantId) throws BaseException {
        //장바구니 비어있으면 예외처리.

        if(checkRestaurant(restaurantId) == 0){
            throw new BaseException(RESTAURANT_ERROR);
        }


        try {
            DeliveryRestaurantDetail deliveryRestaurant = restaurantDao.deliveryRestaurantDetail(restaurantId);
            return deliveryRestaurant;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public int checkRestaurant(int restaurantId){
        return restaurantDao.checkRestaurant(restaurantId);
    }
    public int checkRestaurantCheetah(String cheetah){
        return restaurantDao.checkRestaurantCheetah(cheetah);
    }
    public int checkRestaurantCategoryId(int categoryId){
        return restaurantDao.checkRestaurantCategoryId(categoryId);
    }

    public int checkAllRestaurant(){
        return restaurantDao.checkAllRestaurant();
    }

}
