package com.pikolo.pikolo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pikolo.pikolo.dto.ItemsDTO;

@Mapper
public interface ItemDAO {
    List<ItemsDTO> selectAllItems();
}
