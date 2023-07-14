package com.costler.userproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ApiResponse {
    private String message;
    private UserResponse userResponse;
    public ApiResponse(UserResponse userResponse){
    }

}
