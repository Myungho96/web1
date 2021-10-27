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
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FavoriteService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FavoriteDao favoriteDao;
    private final FavoriteProvider favoriteProvider;
    private final JwtService jwtService;


    @Autowired
    public FavoriteService(FavoriteDao favoriteDao, FavoriteProvider favoriteProvider, JwtService jwtService) {
        this.favoriteDao = favoriteDao;
        this.favoriteProvider = favoriteProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostFavoriteRes createFavorite(PostFavoriteReq postFavoriteReq) throws BaseException {
        //중복
        try{
            int favoriteId = favoriteDao.createFavorite(postFavoriteReq);
            return new PostFavoriteRes(favoriteId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public void modifyFavoriteStatus(StatusFavorite statusFavorite) throws BaseException {
        //디비에 있는 자료가 Valid인지 먼저 체크해야한다.
        int checkFavoriteStatus = favoriteProvider.checkFavoriteStatus(statusFavorite.getFavoriteId());

        if(checkFavoriteStatus == 0){
            throw new BaseException(DB_FAVORITE_INVALID_STATUS);
        }
        //

        try{
            int result = favoriteDao.modifyFavoriteStatus(statusFavorite);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_FAVORITE_STATUS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
