package com.pikolo.pikolo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.pikolo.pikolo.dto.ItemsDTO;
import com.pikolo.pikolo.service.ItemsService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ItemController {

    @Autowired
    private ItemsService itemService;

    @GetMapping("/api/items")
    public List<ItemsDTO> getAllItems() {
        return itemService.getAllItems();
    }
}

