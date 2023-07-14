package com.costler.userproject.model;

import lombok.Data;

@Data
public class ChangePassword {

    private String password;
    private String email;
    private String old_password;
}
