package com.costler.userproject.service.impl;

import com.costler.userproject.entity.User;
import com.costler.userproject.model.ApiResponse;
import com.costler.userproject.model.ChangePassword;
import com.costler.userproject.model.UserResponse;
import com.costler.userproject.repo.UserRepo;
import com.costler.userproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public ApiResponse signUp(User user) {
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        User user1 = userRepo.save(user);
        UserResponse userResponse = userResponse(user1);
        return new ApiResponse("Successfully Registered ", userResponse);
    }

    @Transactional
    @Override
    public ApiResponse deleteByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if(user != null) {
            userRepo.deleteByEmail(email);
            UserResponse userResponse = userResponse(user);
            return new ApiResponse("Successfully Deleted !! ", userResponse);
        }
        throw new UsernameNotFoundException("Email not found !!");
    }

    @Override
    public ApiResponse findByEmail(String email) {
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("Email not found !!");
        }
        UserResponse userResponse = userResponse(user);
        return new ApiResponse("",userResponse);
    }


    @Override
    public ApiResponse updateProfile(User user1, String email) {
        User user = userRepo.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("Email not found !!");
        }
        if(user1.getCity()!=null) user.setCity(user1.getCity());
        if(user1.getName()!=null) user.setName(user1.getName());
        if(user1.getCompanyName()!=null) user.setCompanyName(user1.getCompanyName());
        userRepo.save(user);
        UserResponse userResponse = userResponse(user);
        return new ApiResponse("Profile Updated Successfully ", userResponse);
    }

    @Override
    public ApiResponse changePassword(ChangePassword changePassword, String email) {
        User user = userRepo.findByEmail(email);
        if(user != null) {
            if (this.bCryptPasswordEncoder.matches(changePassword.getOld_password(), user.getPassword())) {
                user.setPassword(this.bCryptPasswordEncoder.encode(changePassword.getPassword()));
                user = userRepo.save(user);
                UserResponse userResponse = userResponse(user);
                return new ApiResponse("Password Change Successfully ", userResponse);
            }
            else throw new UsernameNotFoundException("You enter wrong email !!");
        }
        else throw new UsernameNotFoundException("You enter wrong password!!");
    }


    private UserResponse userResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setRole(user.getRole());
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setCompanyName(user.getCompanyName());
        userResponse.setCity(user.getCity());
        return userResponse;
    }


}
