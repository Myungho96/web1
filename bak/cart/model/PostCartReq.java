package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostCartReq {
    private int UserId;
    private int RestaurantId;
    private int MenuId;
    private int MenuNum;
}
