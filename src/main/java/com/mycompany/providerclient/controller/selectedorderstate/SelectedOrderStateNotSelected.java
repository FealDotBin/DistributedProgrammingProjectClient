/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.providerclient.view.HomeView;
import javax.swing.JOptionPane;

/**
 *
 * @author aferr
 */
public class SelectedOrderStateNotSelected extends SelectedOrderState {
    
    public SelectedOrderStateNotSelected(HomeView homeView){
        super(homeView);
    }
    
    @Override
    public void updateButton(){
        JOptionPane.showMessageDialog(homeView,
                "Oops! An error has occured.", 
                "ERROR", 
                JOptionPane.ERROR_MESSAGE);
    }
    
}
