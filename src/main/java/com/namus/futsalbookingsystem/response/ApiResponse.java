package com.namus.futsalbookingsystem.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ApiResponse <T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("status")
    String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("status_code")
    int statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("admin_list")
    List<T> list;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("role")
    String role;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("token")
    String token;

    public ApiResponse(String status, int status_code){
        this.status=status;
        this.statusCode=status_code;
    }


    public ApiResponse(String status,int statusCode,String token,String role){
        this.status=status;
        this.statusCode=statusCode;
        this.role=role;
        this.token=token;
    }

    public ApiResponse(String status,int statusCode,List<T> list){
        this.status=status;
        this.statusCode=statusCode;
        this.list=list;

    }
}
