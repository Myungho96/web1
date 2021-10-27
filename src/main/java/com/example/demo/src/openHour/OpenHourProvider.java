package com.example.demo.src.openHour;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.openHour.model.*;
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
public class OpenHourProvider {//OpenHourProvider 호출하면 이 함수가 호출되는데,

    private final OpenHourDao openHourDao;//필요한 객체를 정의한다. final 키워드는 엔티티를 한 번만 할당함. 두번 할당하면 오류 발생.
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());//로그를 찍기 위해 하는 것이라고 함.

    @Autowired
    public OpenHourProvider(OpenHourDao openHourDao, JwtService jwtService) {//실질적인 쿼리문은 openHourDao에 있으며, Jwt는 자바 웹 토큰이라고 비밀번호를 암호화하는 데 쓰는 듯 하다.
        this.openHourDao = openHourDao;//각 객체를 생성하지 않고 받아온다.
        this.jwtService = jwtService;
    }


    //특정 유저의 주소 검색
    public DeliveryOpenHour deliveryOpenHour(int restaurantId) throws BaseException {//getLocation(restaurantIdx)를 반환해주기위해서
        //장바구니 비어있으면 예외처리.
        if(checkOpenHour(restaurantId) == 0){
            throw new BaseException(OPENHOUR_ERROR);
        }

        try {
            DeliveryOpenHour deliveryOpenHour = openHourDao.deliveryOpenHour(restaurantId);//getLocation(restaurantIdx)를 반환받아서 반환한다.
            return deliveryOpenHour;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkOpenHour(int restaurantId){
        return openHourDao.checkOpenHour(restaurantId);
    }



}
