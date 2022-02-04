/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.providerclient.controller.HomeController;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderState;
import com.mycompany.providerclient.view.HomeView;

/**
 * Represents the state of a completed order.
 * @author aferr
 */
public class SelectedOrderStateCompleted extends SelectedOrderState {

    /**
     * Initialize the SelectedOrderStateCompleted
     * @param homeView an instance of HomeView
     * @param selectedOrder the order currently selected by the user from the table
     * @param selectedOrderIndex the row of the order currently selected by the user
     */
    public SelectedOrderStateCompleted(HomeView homeView, OrderDto selectedOrder, int selectedOrderIndex) {
        super(homeView, selectedOrder, selectedOrderIndex);
    }
    
    /**
     * Disable the "accept", "ship", "complete" and "refuse" buttons.
     */
    @Override
    public void updateButton(){
        acceptBtn.setEnabled(false);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(false);
    }
    
}
