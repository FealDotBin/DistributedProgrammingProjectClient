/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dto.user;

import com.mycompany.common.model.dao.order.MenuEntity;

/**
 *
 * @author aferr
 */
public class ProviderDto {
    
    private String id;
    private String telephoneNumber;
    private String providerName;
    private String cuisine;
    private String address;
    private Boolean doDelivering;
    private Boolean doTakeAway;
    private MenuEntity menu;
    
}
