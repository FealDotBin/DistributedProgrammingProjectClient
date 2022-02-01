/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dto.order;

import com.mycompany.common.model.dao.order.DishOrderAssociation;
import com.mycompany.common.model.dto.user.CustomerDto;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.common.model.dto.user.RiderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.common.model.enumerations.OrderType;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author aferr
 */
public class OrderDto {
    
    private Long id;
    private List<DishOrderAssociation> dishOrderAssociations;
    private CustomerDto customer;
    private ProviderDto provider;
    private RiderDto rider;
    private OrderType orderType;
    private OrderState orderState;
    private String deliveryTime;
    private double price;

    public void setId(Long id) {
        this.id = id;
    }

    public void setDishOrderAssociations(List<DishOrderAssociation> dishOrderAssociations) {
        this.dishOrderAssociations = dishOrderAssociations;
    }

    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    public void setProvider(ProviderDto provider) {
        this.provider = provider;
    }

    public void setRider(RiderDto rider) {
        this.rider = rider;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public List<DishOrderAssociation> getDishOrderAssociations() {
        return dishOrderAssociations;
    }

    public CustomerDto getCustomer() {
        return customer;
    }

    public ProviderDto getProvider() {
        return provider;
    }

    public RiderDto getRider() {
        return rider;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "OrderDto{" + "id=" + id + ", dishOrderAssociations=" + dishOrderAssociations + ", customer=" + customer + ", provider=" + provider + ", rider=" + rider + ", orderType=" + orderType + ", orderState=" + orderState + ", deliveryTime=" + deliveryTime + ", price=" + price + '}';
    }
    
    
    
}
