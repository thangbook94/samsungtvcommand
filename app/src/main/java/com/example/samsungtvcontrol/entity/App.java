package com.example.samsungtvcontrol.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class App {
    private int id;
    private int method_id;
    private String app_name;
    private String app_id;
}
