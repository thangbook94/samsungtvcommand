package com.example.samsungtvcontrol.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TvInfoDetail {
    private int id;
    private int tv_info_id;
    private String ip;
    private String uuid;
    private String version;
    private Timestamp last_connect;
    private String name;
    private String type;
}
