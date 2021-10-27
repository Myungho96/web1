package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
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
public class UserProvider {//UserProvider를 호출하면 이 함수가 호출되는데,

    private final UserDao userDao;//필요한 객체를 정의한다. final 키워드는 엔티티를 한 번만 할당함. 두번 할당하면 오류 발생.
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());//로그를 찍기 위해 하는 것이라고 함.

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {//실질적인 쿼리문은 UserDao에 있으며, Jwt는 자바 웹 토큰이라고 비밀번호를 암호화하는 데 쓰는 듯 하다.
        this.userDao = userDao;//각 객체를 생성하지 않고 받아온다.
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{//UserController의 52번째 줄에서 이 함수의 결과값을 반환받는다.
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();//Dao에서 getUsers를 호출한 뒤 반환값을 전달 받아 리스트 형식으로 반환한다.
            return getUserRes;
        }
        catch (Exception exception) {//오류나면 DB에러를 throw한다. 이래서 연결이 안된다는 오류가 계속 떴구나...
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetUserRes getUser(int userId) throws BaseException {//getUser(userIdx)를 반환해주기위해서
        try {
            GetUserRes getUserRes = userDao.getUser(userId);//getUser(userIdx)를 반환받아서 반환한다.
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkStatus(String email) throws BaseException{
        try{
            return userDao.checkStatus(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.getPwd(postLoginReq);//db에서 받아온 암호화된 비밀번호
        String passwd;
        try {
            passwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postLoginReq.getPasswd());//받아온 암호를 암호화한다.
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPasswd().equals(passwd)){//디비에 저장된 암호화된 암호와 비교해서 같으면
            int userId = userDao.getPwd(postLoginReq).getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(jwt,userId);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }


}
