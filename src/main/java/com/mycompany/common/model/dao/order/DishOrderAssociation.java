/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dao.order;

/**
 *
 * @author aferr
 */
public class DishOrderAssociation {
    
    private DishEntity dish;
    private int quantity;

    public DishEntity getDish() {
        return dish;
    }

    public int getQuantity() {
        return quantity;
    }
    
}
