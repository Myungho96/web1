package com.example.demo.src.menu;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.menu.model.*;
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
public class MenuService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final MenuDao menuDao;
    private final MenuProvider menuProvider;
    private final JwtService jwtService;


    @Autowired
    public MenuService(MenuDao menuDao, MenuProvider menuProvider, JwtService jwtService) {
        this.menuDao = menuDao;
        this.menuProvider = menuProvider;
        this.jwtService = jwtService;

    }

    //POST
    public PostMenuRes createMenu(PostMenuReq postMenuReq) throws BaseException {
        //중복
        try{
            int menuId = menuDao.createMenu(postMenuReq);
            return new PostMenuRes(menuId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyMenuName(PatchMenu patchMenu) throws BaseException {
        try{
            int result = menuDao.modifyMenuName(patchMenu);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /*
    public void modifyMenuStatus(StatusMenu statusMenu) throws BaseException {
        try{
            int result = menuDao.modifyMenuStatus(statusMenu);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



     */

}
