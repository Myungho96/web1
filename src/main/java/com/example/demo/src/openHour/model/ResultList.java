package com.example.demo.src.openHour.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResultList {
    private String day;
    private String startTime;
    private String endTime;
    private String breakStartTime;
    private String breakEndTime;
}