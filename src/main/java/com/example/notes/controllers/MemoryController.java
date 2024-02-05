package com.example.notes.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MemoryController {

    @GetMapping(value = "getMemoryStats")
    public List<Long> getMememoryParameters(){
        return List.of(
                Runtime.getRuntime().totalMemory(),
                Runtime.getRuntime().maxMemory(),
                Runtime.getRuntime().freeMemory()
        );
    }
}