/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.customerclient.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;

/**
 *This is a utility class used to store costumer information that must be sent or received by the server.
 *This class should be used by someone who is a costumer given that there is sensitive information on it such as username and password
 * @author Amos
 */
public class CustomerEntity {
    
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;    
    private String birthDate;        
    private String iban;
    private String telephoneNumber;
    private double balance;
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "CustomerEntity{" + "id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate + ", iban=" + iban + ", telephoneNumber=" + telephoneNumber + ", balance=" + balance + ", address=" + address + '}';
    }

    public CustomerEntity(String username, String password, String name, String surname, String birthDate, String iban, String telephoneNumber, String address) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.iban = iban;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
    }

    public CustomerEntity(Long id, String username, String password, String name, String surname, String birthDate, String iban, String telephoneNumber, String address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.iban = iban;
        this.telephoneNumber = telephoneNumber;
        this.address = address;
    }

    
    
  
    
    
    
}
