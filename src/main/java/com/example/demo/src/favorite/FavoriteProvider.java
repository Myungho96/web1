package com.example.demo.src.favorite;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.favorite.model.*;
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
public class FavoriteProvider {//FavoriteProvider 호출하면 이 함수가 호출되는데,

    private final FavoriteDao favoriteDao;//필요한 객체를 정의한다. final 키워드는 엔티티를 한 번만 할당함. 두번 할당하면 오류 발생.
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());//로그를 찍기 위해 하는 것이라고 함.

    @Autowired
    public FavoriteProvider(FavoriteDao favoriteDao, JwtService jwtService) {//실질적인 쿼리문은 favoriteDao에 있으며, Jwt는 자바 웹 토큰이라고 비밀번호를 암호화하는 데 쓰는 듯 하다.
        this.favoriteDao = favoriteDao;//각 객체를 생성하지 않고 받아온다.
        this.jwtService = jwtService;
    }


    //특정 유저의 주소 검색
    public DeliveryFavorite deliveryFavorite(int userId) throws BaseException {//getLocation(userIdx)를 반환해주기위해서
        try {
            DeliveryFavorite deliveryFavorite = favoriteDao.deliveryFavorite(userId);//getLocation(userIdx)를 반환받아서 반환한다.
            return deliveryFavorite;
        } catch (Exception exception) {
            throw new BaseException(FAVORITE_ERROR);
        }
    }

    public int checkUserId(int favoriteId) throws BaseException{
        try{
            return favoriteDao.checkUserId(favoriteId);
        } catch (Exception exception){
            throw new BaseException(FAVORITE_ERROR);
        }
    }

    public int checkStatus(int userId) throws BaseException{
        try{
            return favoriteDao.checkStatus(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkRestaurantStatus(int restaurantId) throws BaseException{
        try{
            return favoriteDao.checkRestaurantStatus(restaurantId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkRestaurantName(int restaurantId, String restaurantName) throws BaseException{
        try{
            String name = favoriteDao.checkRestaurantName(restaurantId);
            if(restaurantName.equals(name)){//음식점 이름이 같으면 1 반환
                return 1;
            }
            else{
                return 0;
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFavoriteStatus(int favoriteId) throws BaseException{
        try{
            String Status = favoriteDao.checkFavoriteStatus(favoriteId);
            if(Status.equals("Valid")){//상태가 Valid이면 1 반환
                return 1;
            }
            else{
                return 0;
            }
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
