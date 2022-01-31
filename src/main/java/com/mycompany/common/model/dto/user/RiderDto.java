/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dto.user;

/**
 *
 * @author aferr
 */
public class RiderDto {
    
    private String vehicleType;
    private String name;
    private String telephoneNumber;
    private Long id;

    public String getVehicleType() {
        return vehicleType;
    }

    public String getName() {
        return name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public Long getId() {
        return id;
    }
    
}
