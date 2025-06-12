package com.pikolo.pikolo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.entity.Items;
import com.pikolo.pikolo.repository.ItemRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class ItemController {
    
    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/api/items")
    public List<Items> getMethodName() {
        List<Items> items = itemRepository.findAll();

        return items;
    }
    
}
