/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.common.components.NoEditableTableModel;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.RiderDto;
import com.mycompany.providerclient.view.HomeView;
import javax.swing.JButton;
import javax.swing.JTable;

/**
 * Represents the state of the order currently selected by the user.
 * It has to be extended by a subclass to specify a concrete order's state
 * and some of its methods have to be overridden in order to 
 * reflect the right behaviour of the latter according to the former.
 * @author aferr
 */
public abstract class SelectedOrderState {
    
    protected HomeView homeView;
    protected OrderDto selectedOrder;
    protected int selectedOrderIndex;
    protected JButton acceptBtn;
    protected JButton shipBtn;
    protected JButton completeBtn;
    protected JButton refuseBtn;
    
    /**
     * Initialize the SelectedOrderState
     * @param homeView an instance of HomeView
     */
    public SelectedOrderState(HomeView homeView){
        this.homeView = homeView;
    }
    
    /**
     * Initialize the SelectedOrderState
     * @param homeView an instance of HomeView
     * @param selectedOrder the order currently selected by the user from the table
     * @param selectedOrderIndex the row of the order currently selected by the user
     */
    public SelectedOrderState(HomeView homeView, OrderDto selectedOrder, int selectedOrderIndex){
        this.homeView = homeView;
        this.selectedOrder = selectedOrder;
        this.selectedOrderIndex = selectedOrderIndex;
        acceptBtn = homeView.getAcceptBtn();
        shipBtn = homeView.getShipBtn();
        completeBtn = homeView.getCompleteBtn();
        refuseBtn = homeView.getRefuseBtn();
    }
    
    /**
     * Remove the selected order from the table, disable all the buttons
     * and clear the selectedOrderTable
     */
    protected void updateView(){
        // update allOrdersTable
        JTable allOrdersTable = homeView.getAllOrdersTable();
        NoEditableTableModel allOrdersTableModel = (NoEditableTableModel) allOrdersTable.getModel();
        allOrdersTableModel.removeRow(selectedOrderIndex);
        
        // disable all buttons
        acceptBtn.setEnabled(false);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(false);
        
        // clear selectedOrderTable
        JTable selectedOrderTable = homeView.getSelectedOrderTable();
        NoEditableTableModel selectedOrderTableModel = (NoEditableTableModel) selectedOrderTable.getModel();
        selectedOrderTableModel.setRowCount(0);
    }
    
    /**
     * This method does nothing.
     * Subclasses have to @Override it if they want to provide an implementation.
     */
    public void updateButton(){ }
    
    /**
     * This method does nothing.
     * Subclasses have to @Override it if they want to provide an implementation.
     */
    public void accept() { }
    
    /**
     * This method does nothing.
     * Subclasses have to @Override it if they want to provide an implementation.
     */
    public void refuse() { }
    
    /**
     * This method does nothing.
     * Subclasses have to @Override it if they want to provide an implementation.
     */
    public void ship() { }
    
    /**
     * This method does nothing.
     * Subclasses have to @Override it if they want to provide an implementation.
     */
    public void complete() { }
}
