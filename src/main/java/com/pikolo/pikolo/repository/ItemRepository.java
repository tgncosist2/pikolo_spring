package com.pikolo.pikolo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pikolo.pikolo.entity.Items;

public interface ItemRepository extends JpaRepository<Items, String>{
}