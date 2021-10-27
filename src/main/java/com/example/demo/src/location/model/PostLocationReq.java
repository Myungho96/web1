package com.example.demo.src.location.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostLocationReq {
    private int userId;
    //List<GetLocationList> addressList;
    private String location;
    private float x;
    private float y;
}
