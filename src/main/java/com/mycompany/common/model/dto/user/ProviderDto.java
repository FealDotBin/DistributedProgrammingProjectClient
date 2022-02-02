/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dto.user;

import com.mycompany.common.model.dao.order.MenuEntity;
import java.io.Serializable;

/**
 *
 * @author aferr
 */
public class ProviderDto implements Serializable{
    
    private Long id;
    private String telephoneNumber;
    private String providerName;
    private String cuisine;
    private String address;
    private Boolean doDelivering;
    private Boolean doTakeAway;
    private MenuEntity menu;

    public Long getId() {
        return id;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getDoDelivering() {
        return doDelivering;
    }

    public Boolean getDoTakeAway() {
        return doTakeAway;
    }

    public MenuEntity getMenu() {
        return menu;
    }
    
    
    
}
