/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller.selectedorderstate;

import com.mycompany.common.components.NoEditableTableModel;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.providerclient.view.HomeView;
import javax.swing.JButton;
import javax.swing.JTable;

/**
 *
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
    
    public SelectedOrderState(HomeView homeView){
        this.homeView = homeView;
    }
    
    public SelectedOrderState(HomeView homeView, OrderDto selectedOrder, int selectedOrderIndex){
        this.homeView = homeView;
        this.selectedOrder = selectedOrder;
        this.selectedOrderIndex = selectedOrderIndex;
        acceptBtn = homeView.getAcceptBtn();
        shipBtn = homeView.getShipBtn();
        completeBtn = homeView.getCompleteBtn();
        refuseBtn = homeView.getRefuseBtn();
    }
    
    protected void updateView(){
        // update allOrdersTable
        JTable allOrdersTable = homeView.getAllOrdersTable();
        NoEditableTableModel allOrdersTableModel = (NoEditableTableModel) allOrdersTable.getModel();
        allOrdersTableModel.removeRow(selectedOrderIndex);
        Object[] orderRow = new Object[5];
        orderRow[0] = selectedOrder.getCustomer().getName();
        orderRow[1] = selectedOrder.getRider().getName();
        orderRow[2] = selectedOrder.getOrderType();
        orderRow[3] = selectedOrder.getOrderState();
        orderRow[4] = selectedOrder.getDeliveryTime();
        allOrdersTableModel.insertRow(selectedOrderIndex, orderRow);
        
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
    
    public void updateButton(){ }
    
    public void accept() { }
    
    public void refuse() { }
    
    public void ship() { }
    
    public void complete() { }
    
    
}
