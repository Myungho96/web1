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
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.Optional;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkEmail(postUserReq.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPasswd());
            postUserReq.setPasswd(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userId = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt,userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserId(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserId(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERID);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserPhone(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserPhone(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPHONE);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserPasswd(PatchUserReq patchUserReq) throws BaseException {
        String pwd;
        try{
            //암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(patchUserReq.getPasswd());
            patchUserReq.setPasswd(pwd);
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        try{
            int result = userDao.modifyUserPasswd(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPASSWD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public void modifyUserStatus(StatusUserReq statusUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserStatus(statusUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERSTATUS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //kakao

    public PostUserRes createKakaoLogin(KakaoLoginReq kakaoLoginReq) throws BaseException {
        if(userProvider.checkEmail(kakaoLoginReq.getEmail()) == 0){//가입자가 존재하지 않는 경우
            throw new BaseException(POST_USERS_NOT_CREATED);
        }
        else{//가입이 된 경우
            //탈퇴한 유저인지 체크
            int checkStatus = userProvider.checkStatus(kakaoLoginReq.getEmail());
            if(checkStatus == 0){//탈퇴한 유저라면
                throw new BaseException(POST_USERS_INVALID_USER);
            }
            int userId = userDao.getUserId(kakaoLoginReq);
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt,userId);
        }
    }
}
