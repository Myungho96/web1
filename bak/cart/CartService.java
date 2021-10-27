package com.example.demo.src.cart;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.cart.model.*;
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
public class CartService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CartDao cartDao;
    private final CartProvider cartProvider;
    private final JwtService jwtService;


    @Autowired
    public CartService(CartDao cartDao, CartProvider cartProvider, JwtService jwtService) {
        this.cartDao = cartDao;
        this.cartProvider = cartProvider;
        this.jwtService = jwtService;

    }
    //POST
    public PostCartRes createCart(PostCartReq postCartReq) throws BaseException {
        //중복
        try{
            int cartId = cartDao.createCart(postCartReq);
            return new PostCartRes(cartId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public void modifyCartStatus(StatusCart statusCart) throws BaseException {
        try{
            int result = cartDao.modifyCartStatus(statusCart);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyCartMenuNum(PatchCart patchCart) throws BaseException {
        try{
            int result = cartDao.modifyCartMenuNum(patchCart);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
