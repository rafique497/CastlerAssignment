package com.costler.userproject.service;
import com.costler.userproject.entity.User;
import com.costler.userproject.model.ApiResponse;
import com.costler.userproject.model.ChangePassword;

public interface UserService {

    public ApiResponse signUp(User user);
    public ApiResponse deleteByEmail(String email);
    public ApiResponse findByEmail(String email);
    public ApiResponse updateProfile(User user, String email);
    public ApiResponse changePassword(ChangePassword changePassword, String email);
   }
