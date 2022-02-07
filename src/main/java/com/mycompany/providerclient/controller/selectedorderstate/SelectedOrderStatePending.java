/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.common.components.NoEditableTableModel;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.common.model.enumerations.OrderType;
import com.mycompany.providerclient.controller.HomeController;
import com.mycompany.providerclient.view.HomeView;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * Represents the state of a pending order.
 * @author aferr
 */
public class SelectedOrderStatePending extends SelectedOrderState {
    
    /**
     * Initialize the SelectedOrderStatePending
     * @param homeView an instance of HomeView
     * @param selectedOrder the order currently selected by the user from the table
     * @param selectedOrderIndex the row of the order currently selected by the user
     */
    public SelectedOrderStatePending(HomeView homeView, OrderDto selectedOrder, int selectedOrderIndex){
        super(homeView, selectedOrder, selectedOrderIndex);
    }
    
    /**
     * Enable the "accept" and "refuse" buttons and disable the "ship" and
     * "complete" buttons.
     */
    @Override
    public void updateButton(){
        acceptBtn.setEnabled(true);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(true);
    }
    
    /**
     * Change the selected order's state to "SEMI_ACCEPTED" if the order's type is 
     * "DELIVERY_RIDERS", otherwise change the former into "ACCEPTED".
     * Moreover, update the view by:
     * <ol>
     *   <li> Deleting the row associated to the selected order, 
     *   because the table is actually displaying the "pending" order but 
     *   the order state has been changed to "accepted" or "semi_accepted" </li>
     *   <li> Disabling all buttons </li>
     *   <li> Clearing the selected order </li>
     * </ol>
     */
    @Override
    public void accept(){
        // update order's state according to order's type
        // and choose next state
        SelectedOrderState nextState = null;
        OrderType orderType = selectedOrder.getOrderType();
        if(orderType.equals(OrderType.DELIVERY_RIDERS)){
            selectedOrder.setOrderState(OrderState.SEMI_ACCEPTED);
        }
        else {
            selectedOrder.setOrderState(OrderState.ACCEPTED);
        }
        updateView();
    }
    
    /**
     * Change the selected order's state to "REFUSED".
     * Moreover, update the view by:
     * <ol>
     *   <li> Deleting the row associated to the selected order, 
     *   because the table is actually displaying the "pending" order but 
     *   the order state has been changed to "refused" </li>
     *   <li> Disabling all buttons </li>
     *   <li> Clearing the selected order </li>
     * </ol>
     */
    @Override
    public void refuse(){
        selectedOrder.setOrderState(OrderState.REFUSED);
        updateView();
    }
}
