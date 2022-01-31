/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dao.order;

import java.util.List;

/**
 *
 * @author aferr
 */
public class MenuEntity {
    
    private Long id;
    private List<DishEntity> dishEntities;

    public Long getId() {
        return id;
    }

    public List<DishEntity> getDishEntities() {
        return dishEntities;
    }
    
}
