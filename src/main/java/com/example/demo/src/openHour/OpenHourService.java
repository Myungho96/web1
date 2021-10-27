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
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class OpenHourService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final OpenHourDao openHourDao;
    private final OpenHourProvider openHourProvider;
    private final JwtService jwtService;


    @Autowired
    public OpenHourService(OpenHourDao openHourDao, OpenHourProvider openHourProvider, JwtService jwtService) {
        this.openHourDao = openHourDao;
        this.openHourProvider = openHourProvider;
        this.jwtService = jwtService;

    }
    /*
    //POST
    public PostOpenHourRes createOpenHour(PostOpenHourReq postOpenHourReq) throws BaseException {
        //중복
        try{
            int openHourId = openHourDao.createOpenHour(postOpenHourReq);
            return new PostOpenHourRes(openHourId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public void modifyOpenHourStatus(StatusOpenHour statusOpenHour) throws BaseException {
        try{
            int result = openHourDao.modifyOpenHourStatus(statusOpenHour);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyOpenHourMenuNum(PatchOpenHour patchOpenHour) throws BaseException {
        try{
            int result = openHourDao.modifyOpenHourMenuNum(patchOpenHour);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

     */

}
