package com.example.demo.src.orderHistory;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.orderHistory.model.*;
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
public class OrderHistoryProvider {//OrderHistoryProvider 호출하면 이 함수가 호출되는데,

    private final OrderHistoryDao orderHistoryDao;//필요한 객체를 정의한다. final 키워드는 엔티티를 한 번만 할당함. 두번 할당하면 오류 발생.
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());//로그를 찍기 위해 하는 것이라고 함.

    @Autowired
    public OrderHistoryProvider(OrderHistoryDao orderHistoryDao, JwtService jwtService) {//실질적인 쿼리문은 orderHistoryDao에 있으며, Jwt는 자바 웹 토큰이라고 비밀번호를 암호화하는 데 쓰는 듯 하다.
        this.orderHistoryDao = orderHistoryDao;//각 객체를 생성하지 않고 받아온다.
        this.jwtService = jwtService;
    }


    //특정 유저의 주소 검색
    public DeliveryOrderHistory deliveryOrderHistory(int userId) throws BaseException {//getLocation(userIdx)를 반환해주기위해서
        //장바구니 비어있으면 예외처리.
        if(checkOrderHistory(userId) == 0){
            throw new BaseException(ORDERHISTORY_ERROR);
        }

        try {
            DeliveryOrderHistory deliveryOrderHistory = orderHistoryDao.deliveryOrderHistory(userId);//getLocation(userIdx)를 반환받아서 반환한다.
            return deliveryOrderHistory;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkOrderHistory(int userId){
        return orderHistoryDao.checkOrderHistory(userId);
    }



}
