package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String Name;
    private String IdUser;
    private String Email;
    private String Passwd;
    private String Phone;
}
