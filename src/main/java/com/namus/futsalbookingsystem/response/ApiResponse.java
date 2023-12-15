package com.namus.futsalbookingsystem.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.namus.futsalbookingsystem.entity.AppUser;
import com.namus.futsalbookingsystem.entity.Futsal;

import java.util.List;

public class ApiResponse <T> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("status")
    String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("status_code")
    int statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("list")
    List<T> list;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("role")
    String role;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("token")
    String token;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("user_details")
    AppUser appUser;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("futsal_details")
    Futsal futsal;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("error")
    String error;

    public ApiResponse(String status, int status_code){
        this.status=status;
        this.statusCode=status_code;
    }

    public ApiResponse(String status, int status_code,String error){
        this.status=status;
        this.statusCode=status_code;
        this.error=error;
    }


    public ApiResponse(String status,int statusCode,String token,String role){
        this.status=status;
        this.statusCode=statusCode;
        this.role=role;
        this.token=token;
    }


    public ApiResponse(String status,int statusCode,AppUser appUser){
        this.status=status;
        this.statusCode=statusCode;

        this.appUser=appUser;
    }

    public ApiResponse(String status, int statusCode, Futsal futsal){
        this.status=status;
        this.statusCode=statusCode;

        this.futsal=futsal;
    }

    public ApiResponse(String status,int statusCode,List<T> list){
        this.status=status;
        this.statusCode=statusCode;
        this.list=list;

    }
}
