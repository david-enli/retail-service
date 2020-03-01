package com.example.demo.dao;


import com.example.demo.model.DAOItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDao extends CrudRepository<DAOItem, Integer> {



    List<DAOItem> findByItemNameContaining(String username);
    DAOItem findOneById(long id);
    List<DAOItem> findByCategory(String category);

}