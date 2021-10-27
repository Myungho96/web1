package com.example.demo.src.menu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostMenuReq {
    private int RestaurantId;
    private String Name;
    private String Image;
    private String Explan;
    private int Price;
}
