package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DetailList {
    private String name;
    private String location;
    private String boss;
    private String registNum;
    private String introduce;
    private String phone;
    private String origin;
}