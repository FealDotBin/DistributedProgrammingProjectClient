/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dao.order;

import java.io.Serializable;
import java.util.List;

/**
 * This is a utility class used to store provider menù that must be sent or received by the server.
 * @author aferr
 */
public class MenuEntity implements Serializable{
    
    private Long id;
    private List<DishEntity> dishEntities;

    public Long getId() {
        return id;
    }

    public List<DishEntity> getDishEntities() {
        return dishEntities;
    }
    
}
