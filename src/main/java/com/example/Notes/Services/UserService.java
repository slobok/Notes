package com.example.Notes.Services;

import com.example.Notes.Data.User;
import com.example.Notes.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
       return this.userRepository.findAll();
    }



}
