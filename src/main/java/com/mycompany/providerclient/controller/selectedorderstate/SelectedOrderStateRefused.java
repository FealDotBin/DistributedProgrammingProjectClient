/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.providerclient.controller.HomeController;
import com.mycompany.providerclient.view.HomeView;

/**
 * Represents the state of a refused order.
 * @author aferr
 */
public class SelectedOrderStateRefused extends SelectedOrderState {
    
    /**
     * Initialize the SelectedOrderStateRefused
     * @param homeView an instance of HomeView
     * @param selectedOrder the order currently selected by the user from the table
     * @param selectedOrderIndex the row of the order currently selected by the user
     */
    public SelectedOrderStateRefused(HomeView homeView, OrderDto selectedOrder, int selectedOrderIndex){
        super(homeView, selectedOrder, selectedOrderIndex);
    }
    
    /**
     * Disable all buttons
     */
    @Override
    public void updateButton(){
        acceptBtn.setEnabled(false);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(false);
    }
}
