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
public class Keycodes {
    private int id;
    private int method_id;
    private String keycode;
    private String code;
    private int button_id;
}
