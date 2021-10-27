package com.example.demo.src.coupon;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.coupon.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CouponService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CouponDao couponDao;
    private final CouponProvider couponProvider;
    private final JwtService jwtService;


    @Autowired
    public CouponService(CouponDao couponDao, CouponProvider couponProvider, JwtService jwtService) {
        this.couponDao = couponDao;
        this.couponProvider = couponProvider;
        this.jwtService = jwtService;

    }



    public void modifyCouponStatus(StatusCoupon statusCoupon) throws BaseException {
        //디비에 있는 자료가 Valid인지 먼저 체크해야한다.
        int checkCouponStatus = couponProvider.checkCouponStatus(statusCoupon.getCouponId(), statusCoupon.getUserId());
        if(checkCouponStatus == 0){
            throw new BaseException(DB_COUPON_INVALID_STATUS);
        }
        //
        try{
            int result = couponDao.modifyCouponStatus(statusCoupon);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_COUPON);
            }
        } catch(Exception exception){
            throw new BaseException(COUPON_EMPTY_ERROR);
        }
    }



}
