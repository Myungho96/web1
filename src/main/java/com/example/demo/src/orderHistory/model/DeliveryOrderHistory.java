package com.example.demo.src.orderHistory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryOrderHistory {
    //private int userId;
    private List<ResultList> historyInfo;
}
