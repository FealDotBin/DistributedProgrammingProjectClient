/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model;

/**
 * This is a utility class used by any user of the application to store ausername and password that must be sent to the server.
 * @author aferr
 */
public class Credentials {
    
    private String username;
    private String password;

    public Credentials(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    
}
