/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.common.model.enumerations.OrderType;
import com.mycompany.providerclient.controller.HomeController;
import com.mycompany.providerclient.view.HomeView;
import javax.swing.JButton;

/**
 *
 * @author aferr
 */
public class SelectedOrderStateShipped extends SelectedOrderState {
    
    public SelectedOrderStateShipped(HomeView homeView, OrderDto selectedOrder, int selectedOrderIndex){
        super(homeView, selectedOrder, selectedOrderIndex);
    }
    
    @Override
    public void updateButton(){
        OrderType orderType = selectedOrder.getOrderType();
        
        if(orderType.equals(OrderType.DELIVERY_NORIDER)){
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(false);
            completeBtn.setEnabled(true);
            refuseBtn.setEnabled(false);
        }
        else {
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(false);
            completeBtn.setEnabled(false);
            refuseBtn.setEnabled(false);
        }
    }
    
    @Override
    public void complete(){
        OrderType orderType = selectedOrder.getOrderType();
        if(orderType.equals(OrderType.DELIVERY_NORIDER)){
            selectedOrder.setOrderState(OrderState.COMPLETED);
        }
        updateView();
    }
}
