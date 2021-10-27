package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int userId;
    private String name;
    private String idUser;
    private String email;
    private String passwd;
    private String phone;
    private String created;
    private String updated;
    private String status;
}
