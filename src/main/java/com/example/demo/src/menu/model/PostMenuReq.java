package com.example.demo.src.menu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostMenuReq {
    private int restaurantId;
    private String name;
    private String image;
    private String explan;
    private int price;
}
