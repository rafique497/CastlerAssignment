package com.costler.userproject.controller;

import com.costler.userproject.config.CustomUserDetailsService;
import com.costler.userproject.entity.User;
import com.costler.userproject.helper.JwtUtil;
import com.costler.userproject.model.ApiResponse;
import com.costler.userproject.model.ChangePassword;
import com.costler.userproject.model.JwtRequest;
import com.costler.userproject.model.JwtResponse;
import com.costler.userproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @PostMapping("/signup")
    public ApiResponse save(@RequestBody User user){
        return userService.signUp(user);
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) throws Exception {
        System.out.println(jwtRequest);
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
        }catch(UsernameNotFoundException e) {
            e.printStackTrace();
            throw new Exception("Username not found !!");
        }catch(BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        UserDetails userDetails=this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername() );
        String token=this.jwtUtil.generateToken(userDetails);
        System.out.println("JWT.."+token);
        return ResponseEntity.ok(new JwtResponse(token));
    }



    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return token;
    }

    @PatchMapping("/updateByEmail/{email}")
    public ApiResponse updateProfile(HttpServletRequest request, @RequestBody User user, @PathVariable("email") String email){
        return userService.updateProfile(user,email);

    }


    @GetMapping("/findByEmail/{email}")
    public ApiResponse findByEmail(HttpServletRequest request, @PathVariable("email") String email){
       return userService.findByEmail(email);

    }


    @PostMapping("/changePassword/{email}")
    public ApiResponse changePassword(HttpServletRequest request,@PathVariable("email") String email,@RequestBody ChangePassword changePassword){
            return userService.changePassword(changePassword,email);
    }


    @DeleteMapping("/delete/{email}")
    public ApiResponse deleteByEmail(HttpServletRequest request,@PathVariable("email") String email){
        return userService.deleteByEmail(email);
    }


}
