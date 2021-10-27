package com.example.demo.src.location;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.location.model.*;
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
public class LocationService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LocationDao locationDao;
    private final LocationProvider locationProvider;
    private final JwtService jwtService;


    @Autowired
    public LocationService(LocationDao locationDao, LocationProvider locationProvider, JwtService jwtService) {
        this.locationDao = locationDao;
        this.locationProvider = locationProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostLocationRes createLocation(PostLocationReq postLocationReq) throws BaseException {
        //중복
        try{
            int locationId = locationDao.createLocation(postLocationReq);
            return new PostLocationRes(locationId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyLocationName(PatchLocation patchLocation) throws BaseException {
        try{
            int result = locationDao.modifyLocationName(patchLocation);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_LOCATION);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void modifyLocationStatus(StatusLocation statusLocation) throws BaseException {
        //디비에 있는 자료가 Valid인지 먼저 체크해야한다.
        int checkLocationStatus = locationProvider.checkLocationStatus(statusLocation.getLocationId());

        if(checkLocationStatus == 0){
            throw new BaseException(DB_LOCATIONS_INVALID_STATUS);
        }
        //
        try{

            int result = locationDao.modifyLocationStatus(statusLocation);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_LOCATION_STATUS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
