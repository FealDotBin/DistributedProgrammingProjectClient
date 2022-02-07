/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.providerclient.view.HomeView;
import javax.swing.JOptionPane;

/**
 * Represents the state of an order that is not selected by the user
 * @author aferr
 */
public class SelectedOrderStateNotSelected extends SelectedOrderState {
    
    /**
     * Initialize the SelectedOrderStateNotSelected
     * @param homeView an instance of HomeView
     */
    public SelectedOrderStateNotSelected(HomeView homeView){
        super(homeView);
    }
    
    /**
     * Pop up a message dialog informing the user that an error
     * has occured
     */
    @Override
    public void updateButton(){
        JOptionPane.showMessageDialog(homeView,
                "Oops! An error has occured.", 
                "ERROR", 
                JOptionPane.ERROR_MESSAGE);
    }
    
}
