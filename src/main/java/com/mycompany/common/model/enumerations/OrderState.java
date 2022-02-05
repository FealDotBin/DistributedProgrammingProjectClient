/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.common.model.enumerations;

/**
 * List of all possible states in which an order can be found during the execution of the application
 * @author aferr
 */
public enum OrderState {
    PENDING,
    ACCEPTED,
    SHIPPED,
    COMPLETED,
    SEMI_ACCEPTED,
    REFUSED
}
