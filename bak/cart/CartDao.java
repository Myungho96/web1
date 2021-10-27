package com.example.demo.src.cart;


import com.example.demo.src.cart.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class CartDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //리스트로 나오게 만든 것
    public DeliveryCart deliveryCart(int userId){
        String deliveryCartQuery = "select M.name, C.menuNum from Cart C left join Menu M on C.menuId = M.menuId where C.userId = ? && C.status = 'Valid'";//이 쿼리를 디비에 날리고
        String temp = "select R.name from Cart C,Restaurant R where C.userId = ? && C.status = 'Valid' && C.restaurantId = R.restaurantId limit 1";
        String RestaurantName = jdbcTemplate.queryForObject(temp, String.class, userId);
        return new DeliveryCart (
                userId,
                RestaurantName,
                this.jdbcTemplate.query(deliveryCartQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("M.name"),
                                rs.getInt("C.menuNum")),
                        userId));
    }

    //카트 유무 검사
    public int checkCart(int userId){
        String checkCartQuery = "select exists(select cartId from Cart where userId = ? && status = 'Valid')";
        return this.jdbcTemplate.queryForObject(checkCartQuery,
                int.class,
                userId);

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
    public int createCart(PostCartReq postCartReq){//배송지 생성하는 부분.
        String createCartQuery = "insert into Cart (userId, restaurantId, menuId, menuNum) VALUES (?,?,?,?)";
        Object[] createCartParams = new Object[]{postCartReq.getUserId(), postCartReq.getRestaurantId(), postCartReq.getMenuId(), postCartReq.getMenuNum()};
        this.jdbcTemplate.update(createCartQuery, createCartParams);//쿼리로 유저 생성한 후,
        String lastInsertCartQuery = "select last_insert_id()";//마지막으로 insert된 id를 반환한다.
        return this.jdbcTemplate.queryForObject(lastInsertCartQuery,int.class);
    }


    //상태 수정 == 삭제
    public int modifyCartStatus(StatusCart statusCart){
        String modifyCartStatusQuery = "update Cart set status = ? where cartId = ? ";
        Object[] modifyCartStatusParams = new Object[]{statusCart.getStatus(), statusCart.getCartId()};
        return this.jdbcTemplate.update(modifyCartStatusQuery,modifyCartStatusParams);
    }

    //메뉴 수량 수정
    public int modifyCartMenuNum(PatchCart patchCart){
        String modifyCartMenuNumQuery = "update Cart set menuNum = ? where cartId = ? ";
        Object[] modifyCartMenuNumParams = new Object[]{patchCart.getMenuNum(), patchCart.getCartId()};

        return this.jdbcTemplate.update(modifyCartMenuNumQuery,modifyCartMenuNumParams);
    }





}
