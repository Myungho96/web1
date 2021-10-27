package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class UserDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    //전체 유저 조회
    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from User";    //디비에 이 쿼리를 날린다.
        return this.jdbcTemplate.query(getUsersQuery,//쿼리결과를 GetUserRes로 바꿔서 반환한다.
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("idUser"),
                        rs.getString("email"),
                        rs.getString("passwd"),
                        rs.getString("phone"),
                        rs.getString("created"),
                        rs.getString("updated"),
                        rs.getString("status"))
                );
    }
    //특정 이메일 유저 조회
    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from User where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("idUser"),
                        rs.getString("email"),
                        rs.getString("passwd"),
                        rs.getString("phone"),
                        rs.getString("created"),
                        rs.getString("updated"),
                        rs.getString("status")),
                getUsersByEmailParams);
    }
    //특정 인덱스의 유저 조회
    public GetUserRes getUser(int userId){//특정 유저를 조회할 경우
        String getUserQuery = "select * from User where userId = ? ";//이 쿼리를 디비에 날리고
        int getUserParams = userId;//패스 변수로 받은 걸 getUserParams에 저장한다.
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userId"),
                        rs.getString("name"),
                        rs.getString("idUser"),
                        rs.getString("email"),
                        rs.getString("passwd"),
                        rs.getString("phone"),
                        rs.getString("created"),
                        rs.getString("updated"),
                        rs.getString("status")),
                getUserParams);
    }

    //특정 인덱스의 유저의 주소 조회
    public List<GetLocation> getLocation(int userId){//특정 유저의 배송지를 조회할 경우
        String getLocationQuery = "select location,x,y from Location where userId = ? && status = 'valid'";//이 쿼리를 디비에 날리고
        int getUserParams = userId;//패스 변수로 받은 걸 getUserParams에 저장한다.
        return this.jdbcTemplate.query(getLocationQuery,
                (rs, rowNum) -> new GetLocation(
                        rs.getString("location"),
                        rs.getInt("x"),
                        rs.getInt("y")),
                getUserParams);
    }

    //특정 인덱스의 유저의 즐겨찾기 조회
    public List<GetFavorites> getFavorites(int userId){//특정 유저의 배송지를 조회할 경우
        String getFavoritesQuery = "select restaurantName,image from Favorites where userId = ? && status = 'valid'";//이 쿼리를 디비에 날리고
        int getUserParams = userId;//패스 변수로 받은 걸 getUserParams에 저장한다.
        return this.jdbcTemplate.query(getFavoritesQuery,
                (rs, rowNum) -> new GetFavorites(
                        rs.getString("restaurantName"),
                        rs.getString("image")),
                getUserParams);
    }

    //회원가입(유저 생성)
    public int createUser(PostUserReq postUserReq){//유저 생성하는 부분.
        String createUserQuery = "insert into User (name, idUser, email, passwd, phone) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getIdUser(), postUserReq.getEmail(), postUserReq.getPasswd(), postUserReq.getPhone()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);//쿼리로 유저 생성한 후,
        String lastInserIdQuery = "select last_insert_id()";//마지막으로 insert된 id를 반환한다.
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    //이메일 중복검사
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    //이름 수정
    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set name = ? where userId = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getName(), patchUserReq.getUserId()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
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

    }



}
