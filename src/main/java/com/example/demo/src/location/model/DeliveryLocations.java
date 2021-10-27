package com.example.demo.src.location.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DeliveryLocations {
    private int userId;
    private List<String> address;
}
