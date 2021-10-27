package com.example.demo.src.category;


import com.example.demo.src.category.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository//실질적인 쿼리가 있는 부분.
public class CategoryDao {

    private JdbcTemplate jdbcTemplate;//필요한 객체를 생성한다. 디비를 이용하기때문에 JdbcTemplate이 필요한 것 같다.

    @Autowired  //생성/
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    //리스트로 나오게 만든 것
    public DeliveryCategory deliveryCategory(){
        String deliveryCategoryQuery = "select name,image from Category where status='Valid'";//이 쿼리를 디비에 날리고
        return new DeliveryCategory (
                this.jdbcTemplate.query(deliveryCategoryQuery,
                        (rs, rowNum) -> new ResultList(
                                rs.getString("name"),
                                rs.getString("image")
                        )));
    }

    //카트 유무 검사
    public int checkCategory(){
        String checkCategoryQuery = "select exists(select * from Category)";
        return this.jdbcTemplate.queryForObject(checkCategoryQuery,
                int.class);

    }
}
