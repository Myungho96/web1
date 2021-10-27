package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Cart {
    private int CartId;
    private String Status;
    private int MenuNum;
}
