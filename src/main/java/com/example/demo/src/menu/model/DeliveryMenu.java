package com.example.demo.src.menu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryMenu {
    private int restaurantId;
    private String restaurantName;
    private List<ResultList> menuInfo;
}
