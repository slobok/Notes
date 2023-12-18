package com.example.notes.services;

import com.example.notes.data.User;
import com.example.notes.repository.UserRepository;
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
