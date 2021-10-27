package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private String idUser;
    private String name;
    private String passwd;
    private String email;
    private String phone;
    private String status;
}
