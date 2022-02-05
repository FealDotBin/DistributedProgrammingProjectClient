/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.dto.user;

import java.io.Serializable;

/**
 *This is a utility class used to store costumer information that must be sent or received by the server.
 *This class should be used by someone who is not a costumer given that there is no sensitive information on it such as username and password
 * @author aferr
 */
public class CustomerDto implements Serializable{
    
    private Long id;
    private String address;
    private String name;
    private String surname;
    private String telephoneNumber;

    public CustomerDto(Long id, String address, String name, String surname, String telephoneNumber) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
    }

    
    
    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    @Override
    public String toString() {
        return "CustomerDto{" + "id=" + id + ", address=" + address + ", name=" + name + ", surname=" + surname + ", telephoneNumber=" + telephoneNumber + '}';
    }
    
    
    
    
}
