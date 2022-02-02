/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dao.order;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author aferr
 */
public class DishOrderAssociation implements Serializable{
    
    private DishEntity dish;
    private int quantity;

    public DishEntity getDish() {
        return dish;
    }

    public int getQuantity() {
        return quantity;
    }

    public DishOrderAssociation(DishEntity dish) {
        this.dish = dish;
        this.quantity = 1;
    }
    
    public void increaseQuantity(){
        this.quantity++;
    }

    @Override
    public String toString() {
        return "DishOrderAssociation{" + "dish=" + dish + ", quantity=" + quantity + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.dish);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DishOrderAssociation other = (DishOrderAssociation) obj;
        if (!Objects.equals(this.dish, other.dish)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
