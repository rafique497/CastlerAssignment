package com.costler.userproject.model;

import lombok.Data;

@Data
public class UserResponse {
    private String email;
    private String name;
    private String companyName;
    private String city;
    private String role;
}
