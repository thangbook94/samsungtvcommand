package com.example.samsungtvcontrol.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class Brands {
    private int id;
    private String full_name;
    private int brand_from;
    private int brand_to;
    private int status;
}
