package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResultList {
    private String name;
    private int delivery;
    private int time;
    private float totalScore;
    private String image;
    private String cheetah;
}