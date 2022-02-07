/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.model;

import java.time.LocalDate;

/**
 *This is a utility class used to store provider information that must be sent or received by the server.
 *This class should be used by someone who is a provider given that there is sensitive information on it such as username and password
 * @author aferr
 */
public class ProviderEntity {
    
    private Long id;
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
    public ProviderEntity(Long id, String username, String password, 
            String name, String surname, String birthDate, String iban, 
            String telephoneNumber, String providerName, String cuisine, 
            String address, Boolean doDelivering, Boolean doTakeAway, 
            Boolean hasOwnRiders, Boolean isAvailable) {
        this.id = id;
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
    
    // Constructor with all attributes except id, balance
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

    // constructor without id, isAvailable, balance attributes
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

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDoDelivering(Boolean doDelivering) {
        this.doDelivering = doDelivering;
    }

    public void setDoTakeAway(Boolean doTakeAway) {
        this.doTakeAway = doTakeAway;
    }

    public void setHasOwnRiders(Boolean hasOwnRiders) {
        this.hasOwnRiders = hasOwnRiders;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
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
