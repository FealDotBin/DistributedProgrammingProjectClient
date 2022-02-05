/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dto.user;

import java.io.Serializable;

/**
 *This is a utility class used to store rider information that must be sent or received by the server.
 *This class should be used by someone who is not a rider given that there is no sensitive information on it such as username and password
 * @author aferr
 */
public class RiderDto implements Serializable{
    
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

    @Override
    public String toString() {
        return "RiderDto{" + "vehicleType=" + vehicleType + ", name=" + name + ", telephoneNumber=" + telephoneNumber + ", id=" + id + '}';
    }
    
    
    
    
}
