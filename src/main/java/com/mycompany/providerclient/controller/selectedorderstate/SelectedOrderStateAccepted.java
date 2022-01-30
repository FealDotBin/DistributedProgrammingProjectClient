/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.providerclient.controller.HomeController;
import com.mycompany.providerclient.view.HomeView;
import javax.swing.JButton;

/**
 *
 * @author aferr
 */
public class SelectedOrderStateAccepted extends SelectedOrderState {
    
    public SelectedOrderStateAccepted(HomeView homeView, HomeController.Order selectedOrder, int selectedOrderIndex){
        super(homeView, selectedOrder, selectedOrderIndex);
    }
    
    @Override
    public void updateButton(){
        String orderType = selectedOrder.getOrderType();
        
        if(orderType.equals("TAKE_AWAY")){
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(false);
            completeBtn.setEnabled(true);
            refuseBtn.setEnabled(false);
        }
        else if(orderType.equals("DELIVERY_NO_RIDER")){
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(true);
            completeBtn.setEnabled(false);
            refuseBtn.setEnabled(false);
        }
        else if(orderType.equals("DELIVERY_RIDERS")){
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(false);
            completeBtn.setEnabled(false);
            refuseBtn.setEnabled(false);
        }
    }
    
    @Override
    public void ship(){
        String orderType = selectedOrder.getOrderType();
        if(orderType.equals("DELIVERY_NO_RIDER")){
            selectedOrder.setOrderState("SHIPPED");
        }
        updateView();
    }
    
    @Override
    public void complete(){
        String orderType = selectedOrder.getOrderType();
        if(orderType.equals("TAKE_AWAY")){
            selectedOrder.setOrderState("COMPLETED");
        }
        updateView();
    }
}
