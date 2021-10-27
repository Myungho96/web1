package com.example.demo.src.menu;


import com.example.demo.src.menu.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class MenuDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //리스트로 나오게 만든 것
    public DeliveryMenu deliveryMenu(int restaurantId){
        String deliveryMenuQuery = "select name,image,explan,price from Menu where restaurantId = ? && status='Valid'";//이 쿼리를 디비에 날리고
        String temp = "select name from Restaurant where restaurantId = ?";
        String RestaurantName = jdbcTemplate.queryForObject(temp, String.class, restaurantId);
        return new DeliveryMenu (
                restaurantId,
                RestaurantName,
                this.jdbcTemplate.query(deliveryMenuQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("name"),
                                rs.getString("image"),
                                rs.getString("explan"),
                                rs.getInt("price")
                        ),
                        restaurantId));
    }

    //카트 유무 검사
    public int checkMenu(int restaurantId){
        String checkMenuQuery = "select exists(select menuId from Menu where restaurantId = ? && status='Valid')";
        return this.jdbcTemplate.queryForObject(checkMenuQuery,
                int.class,
                restaurantId);

    }





    /*


    //이메일 중복검사
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }



    //비밀번호 조회
    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userId,passwd,email,name,idUser from User where idUser = ?";
        String getPwdParams = postLoginReq.getUserId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("idUser"),
                        rs.getString("email"),
                        rs.getString("passwd")),
                getPwdParams
                );

    }*/




    //생성
    public int createMenu(PostMenuReq postMenuReq){//배송지 생성하는 부분.
        String createMenuQuery = "insert into Menu (restaurantId, name, image, explan, price) VALUES (?,?,?,?,?)";
        Object[] createMenuParams = new Object[]{postMenuReq.getRestaurantId(), postMenuReq.getName(), postMenuReq.getImage(), postMenuReq.getExplan(), postMenuReq.getPrice()};
        this.jdbcTemplate.update(createMenuQuery, createMenuParams);//쿼리로 유저 생성한 후,
        String lastInsertMenuQuery = "select last_insert_id()";//마지막으로 insert된 id를 반환한다.
        return this.jdbcTemplate.queryForObject(lastInsertMenuQuery,int.class);
    }

    //이름 수정
    public int modifyMenuName(PatchMenu patchMenu){
        String modifyMenuNameQuery = "update Menu set name = ? where menuId = ? ";
        Object[] modifyMenuNameParams = new Object[]{patchMenu.getName(), patchMenu.getMenuId()};

        return this.jdbcTemplate.update(modifyMenuNameQuery,modifyMenuNameParams);
    }

    /*
    //상태 수정 == 삭제
    public int modifyMenuStatus(StatusMenu statusMenu){
        String modifyMenuStatusQuery = "update Coupon set status = ? where couponId = ? ";
        Object[] modifyMenuStatusParams = new Object[]{statusMenu.getStatus(), statusMenu.getMenuId()};
        return this.jdbcTemplate.update(modifyMenuStatusQuery,modifyMenuStatusParams);
    }



     */





}
