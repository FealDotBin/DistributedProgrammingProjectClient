/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.riderclient.model;

/**
 *
 * @author Amos
 */
public class RiderEntity {
    
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;    
    private String birthDate;        
    private String iban;
    private String telephoneNumber;
    private double balance;
    private String vehicleType;

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



    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public RiderEntity(String username, String password, String name, String surname, String birthDate, String iban, String telephoneNumber, String vehicleType) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.iban = iban;
        this.telephoneNumber = telephoneNumber;
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "RiderEntity{" + "id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate + ", iban=" + iban + ", telephoneNumber=" + telephoneNumber + ", balance=" + balance +  ", documentPath="+ ", vehicleType=" + vehicleType + '}';
    }

    public RiderEntity(Long id, String username, String password, String name, String surname, String birthDate, String iban, String telephoneNumber, String vehicleType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.iban = iban;
        this.telephoneNumber = telephoneNumber;
        this.vehicleType = vehicleType;
    }
    
    
    
    
    
    
}
