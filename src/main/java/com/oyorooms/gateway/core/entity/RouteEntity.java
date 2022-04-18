package com.oyorooms.gateway.core.entity;

import lombok.Data;

import java.util.List;

@Data
public class RouteEntity {

    private String path;

    private String method;

    private String uri;

    private String name;

    private Integer order;

    private List<String> filters;
}
