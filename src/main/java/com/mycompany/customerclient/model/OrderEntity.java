package com.mycompany.customerclient.model;


import com.mycompany.common.model.dao.order.DishOrderAssociation;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.common.model.enumerations.OrderType;
import com.mycompany.providerclient.model.ProviderEntity;
import java.io.Serializable;
import java.util.List;


public class OrderEntity implements Serializable {

    private Long id;



    private List<DishOrderAssociation> dishOrderAssociations;



    private CustomerEntity customer;



    private ProviderDto provider;


    //private RiderEntity rider;



    private OrderType orderType;


    private OrderState orderState;


    private String deliveryTime;

    private double price;

    private static final int minuteOffsetDeliveryTime = 10;

    public OrderEntity(List<DishOrderAssociation> dishOrderAssociations, CustomerEntity customer, ProviderDto provider, OrderType orderType, OrderState orderState, String deliveryTime, double price) {
        this.dishOrderAssociations = dishOrderAssociations;
        this.customer = customer;
        this.provider = provider;
        this.orderType = orderType;
        this.orderState = orderState;
        this.deliveryTime = deliveryTime;
        this.price = price;
    }




}