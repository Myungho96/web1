package com.example.demo.src.location.model;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Location {
    private int locationId;
    private String location;
    private String status;
}
