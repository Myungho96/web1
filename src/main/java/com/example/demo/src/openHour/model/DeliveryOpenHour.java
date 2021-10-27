package com.example.demo.src.openHour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryOpenHour {
    private int restaurantId;
    private String restaurantName;
    private List<ResultList> hourInfo;
}
