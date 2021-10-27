package com.example.demo.src.orderHistory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResultList {
    private String name;
    private String created;
    private String deliveryStatus;
    private int score;
}