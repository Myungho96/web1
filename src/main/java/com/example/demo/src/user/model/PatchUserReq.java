package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserReq {
    private int userId;
    private String name;
    private String idUser;
    private String passwd;
    private String phone;
    private String email;
}
