package com.pikolo.pikolo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pikolo.pikolo.dto.ItemsDTO;
import com.pikolo.pikolo.mapper.ItemDAO;

@Service
public class ItemsService {
    
    @Autowired
    private ItemDAO itemMapper;

    public List<ItemsDTO> getAllItems() {
        return itemMapper.selectAllItems();
    }
}
