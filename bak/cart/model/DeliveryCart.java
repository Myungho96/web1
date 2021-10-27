package com.example.demo.src.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryCart {
    private int userId;
    private String restaurantName;
    private List<ResultList> idNameNum;
}
