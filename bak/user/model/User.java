package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int UserId;
    private String IdUser;
    private String Name;
    private String Passwd;
    private String Email;
}
