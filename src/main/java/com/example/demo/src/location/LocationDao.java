package com.example.demo.src.location;


import com.example.demo.src.location.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class LocationDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /*
    //특정 인덱스의 유저의 주소 조회
    public List<GetLocation> getLocation(int userId){//특정 유저의 배송지를 조회할 경우
        String getLocationQuery = "select userId,location,x,y from Location where userId = ? && status = 'valid'";//이 쿼리를 디비에 날리고
        int getUserParams = userId;//패스 변수로 받은 걸 getUserParams에 저장한다.
        return this.jdbcTemplate.query(getLocationQuery,
                (rs, rowNum) -> new GetLocation(
                        rs.getInt("userId"),
                        rs.getString("location"),
                        rs.getInt("x"),
                        rs.getInt("y")),
                getUserParams);
    }*/

    //리스트로 나오게 만든 것
    public DeliveryLocations getLocation(int userId){
        String getLocationQuery = "select location from Location where userId = ? && status = 'valid'";

        return new DeliveryLocations (
                userId,
                this.jdbcTemplate.query(getLocationQuery, (rs, rowNum) -> rs.getString("location"),userId)
        );
    }




    //유저 인덱스 있는지 검사
    public int checkUserId(int locationId){
        String checkUserIdQuery = "select userId from Location where locationId = ?";
        int checkUserIdParams = locationId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserIdParams);

    }


    //배송지 생성
    public int createLocation(PostLocationReq postLocationReq){//배송지 생성하는 부분.
        String createLocationQuery = "insert into Location (userId, location, x, y) VALUES (?,?,?,?)";
        Object[] createLocationParams = new Object[]{postLocationReq.getUserId(), postLocationReq.getLocation(), postLocationReq.getX(), postLocationReq.getY()};
        this.jdbcTemplate.update(createLocationQuery, createLocationParams);//쿼리로 유저 생성한 후,
        String lastInsertLocationQuery = "select last_insert_id()";//마지막으로 insert된 id를 반환한다.
        return this.jdbcTemplate.queryForObject(lastInsertLocationQuery,int.class);
    }

    //이름 수정
    public int modifyLocationName(PatchLocation patchLocation){
        String modifyLocationNameQuery = "update Location set location = ?, x = ?, y = ? where locationId = ? ";
        Object[] modifyLocationNameParams = new Object[]{patchLocation.getLocation(), patchLocation.getX(), patchLocation.getY(), patchLocation.getLocationId()};

        return this.jdbcTemplate.update(modifyLocationNameQuery,modifyLocationNameParams);
    }

    //상태 수정 == 삭제
    public int modifyLocationStatus(StatusLocation statusLocation){
        String modifyLocationStatusQuery = "update Location set status = ? where locationId = ? ";
        Object[] modifyLocationStatusParams = new Object[]{statusLocation.getStatus(), statusLocation.getLocationId()};

        return this.jdbcTemplate.update(modifyLocationStatusQuery,modifyLocationStatusParams);
    }

    public int checkStatus(int userId){
        String checkUserIdQuery = "select exists(select * from User where userId = ? && status='Valid')";
        int checkUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserIdParams);

    }

    public String checkLocationStatus(int locationId){
        String checkStatusQuery = "select status from Location where locationId = ?";
        int checkStatusParams = locationId;
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                String.class,
                checkStatusParams);

    }



}
