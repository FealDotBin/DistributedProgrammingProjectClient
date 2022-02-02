/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dao.order;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author aferr
 */
public class DishEntity implements Serializable{
    
    private Long id;
    private String name;


    private String description;
    private List<String> ingredients;
    private double price;

    // constructor with all fields
    public DishEntity(Long id, String name, String description, List<String> ingredients, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.price = price;
    }

    // constructor without id
    public DishEntity(String name, String description, List<String> ingredients, double price){
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.price = price;
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public double getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setPrice(double price) {
        this.price = price;
    }
        @Override
    public String toString() {
        return "DishEntity{" + "id=" + id + ", name=" + name + ", description=" + description + ", ingredients=" + ingredients + ", price=" + price + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.name);
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
        final DishEntity other = (DishEntity) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
    
    
}
