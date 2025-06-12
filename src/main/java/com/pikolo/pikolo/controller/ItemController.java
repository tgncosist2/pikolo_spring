package com.pikolo.pikolo.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class ItemController {
    
    @GetMapping("/api/items")
    public List<String> getMethodName() {
        List<String> items = new ArrayList<>();
        items.add("강태일");
        items.add("심규민");
        items.add("쌍용교육센터");
        
        return items;
    }
    
}
