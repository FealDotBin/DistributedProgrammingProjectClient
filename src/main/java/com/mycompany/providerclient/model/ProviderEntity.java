/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.model;

import java.time.LocalDate;

/**
 *
 * @author aferr
 */
public class ProviderEntity {
    
    private String username;
    private String password;
    
    private String name;
    private String surname;
    private String birthDate;
    private String iban;
    private String telephoneNumber;
    private double balance;
    
    private String providerName;
    private String cuisine;
    private String address;
    private Boolean doDelivering;
    private Boolean doTakeAway;
    private Boolean hasOwnRiders;
    private Boolean isAvailable;

    // Constructor with all attributes except balance
    public ProviderEntity(String username, String password, 
            String name, String surname, String birthDate, String iban, 
            String telephoneNumber, String providerName, String cuisine, 
            String address, Boolean doDelivering, Boolean doTakeAway, 
            Boolean hasOwnRiders, Boolean isAvailable) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.iban = iban;
        this.telephoneNumber = telephoneNumber;
        this.providerName = providerName;
        this.cuisine = cuisine;
        this.address = address;
        this.doDelivering = doDelivering;
        this.doTakeAway = doTakeAway;
        this.hasOwnRiders = hasOwnRiders;
        this.isAvailable = isAvailable;
    }

    // constructor without isAvailable and balance attribute
    public ProviderEntity(String username, String password, String name, 
            String surname, String birthDate, String iban,
            String telephoneNumber, String providerName, String cuisine, 
            String address, Boolean doDelivering, Boolean doTakeAway, 
            Boolean hasOwnRiders) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.iban = iban;
        this.telephoneNumber = telephoneNumber;
        this.balance = balance;
        this.providerName = providerName;
        this.cuisine = cuisine;
        this.address = address;
        this.doDelivering = doDelivering;
        this.doTakeAway = doTakeAway;
        this.hasOwnRiders = hasOwnRiders;
        isAvailable = false;
    }
    
    

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getIban() {
        return iban;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public double getBalance() {
        return balance;
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

    public Boolean getHasOwnRiders() {
        return hasOwnRiders;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    
}
