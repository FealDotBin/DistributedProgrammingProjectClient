/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.common.components.NoEditableTableModel;
import com.mycompany.providerclient.controller.HomeController;
import com.mycompany.providerclient.view.HomeView;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author aferr
 */
public class SelectedOrderStatePending extends SelectedOrderState {
    
    public SelectedOrderStatePending(HomeView homeView, HomeController.Order selectedOrder, int selectedOrderIndex){
        super(homeView, selectedOrder, selectedOrderIndex);
    }
    
    @Override
    public void updateButton(){
        acceptBtn.setEnabled(true);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(true);
    }
    
    
    @Override
    public void accept(){
        // update order's state according to order's type
        // and choose next state
        SelectedOrderState nextState = null;
        String orderType = selectedOrder.getOrderType();
        if(orderType.equals("DELIVERY_RIDERS")){
            selectedOrder.setOrderState("SEMI_ACCEPTED");
        }
        else {
            selectedOrder.setOrderState("ACCEPTED");
        }
        updateView();
    }
    
    @Override
    public void refuse(){
        selectedOrder.setOrderState("REFUSED");
        updateView();
    }
}
