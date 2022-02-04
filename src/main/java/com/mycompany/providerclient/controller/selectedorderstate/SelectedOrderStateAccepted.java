/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.common.model.enumerations.OrderType;
import com.mycompany.providerclient.view.HomeView;

/**
 * Represents the state of an accepted order.
 * @author aferr
 */
public class SelectedOrderStateAccepted extends SelectedOrderState {
    
    /**
     * Initialize the SelectedOrderStateAccepted
     * @param homeView an instance of HomeView
     * @param selectedOrder the order currently selected by the user from the table
     * @param selectedOrderIndex the row of the order currently selected by the user
     */
    public SelectedOrderStateAccepted(HomeView homeView, OrderDto selectedOrder, int selectedOrderIndex){
        super(homeView, selectedOrder, selectedOrderIndex);
    }
    
    /**
     * Enable and disable the "accept", "ship", "complete" and "refuse" buttons
     * according to the order's type.
     */
    @Override
    public void updateButton(){
        OrderType orderType = selectedOrder.getOrderType();
        
        if(orderType.equals(OrderType.TAKE_AWAY)){
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(false);
            completeBtn.setEnabled(true);
            refuseBtn.setEnabled(false);
        }
        else if(orderType.equals(OrderType.DELIVERY_NORIDER)){
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(true);
            completeBtn.setEnabled(false);
            refuseBtn.setEnabled(false);
        }
        else if(orderType.equals(OrderType.DELIVERY_RIDERS)){
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(false);
            completeBtn.setEnabled(false);
            refuseBtn.setEnabled(false);
        }
    }
    
    /**
     * Change the selected order's state to "shipped" if the order's type is 
     * "DELIVERY_NORIDER".
     * Moreover, update the view by:
     * <ol>
     *   <li> Deleting the row associated to the selected order, 
     *   because the table is actually displaying the "accepted" order but 
     *   the order state has been changed to "shipped" </li>
     *   <li> Disabling all buttons </li>
     *   <li> Clearing the selected order </li>
     * </ol>
     */
    @Override
    public void ship(){
        OrderType orderType = selectedOrder.getOrderType();
        if(orderType.equals(OrderType.DELIVERY_NORIDER)){
            selectedOrder.setOrderState(OrderState.SHIPPED);
        }
        updateView();
    }
    
    /**
     * Change the selected order's state to "completed" if the order's type is 
     * "TAKE_AWAY".
     * Moreover, update the view by:
     * <ol>
     *   <li> Deleting the row associated to the selected order, 
     *   because the table is actually displaying the "accepted" order but 
     *   the order state has been changed to "shipped" </li>
     *   <li> Disabling all buttons </li>
     *   <li> Clearing the selected order </li>
     * </ol>
     */
    @Override
    public void complete(){
        OrderType orderType = selectedOrder.getOrderType();
        if(orderType.equals(OrderType.TAKE_AWAY)){
            selectedOrder.setOrderState(OrderState.COMPLETED);
        }
        updateView();
    }
}
