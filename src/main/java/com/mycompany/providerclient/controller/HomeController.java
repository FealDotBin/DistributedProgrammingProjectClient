/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateCompleted;
import com.mycompany.common.components.NoEditableTableModel;
import com.mycompany.common.model.dao.order.DishEntity;
import com.mycompany.common.model.dao.order.DishOrderAssociation;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.common.model.enumerations.OrderType;
import com.mycompany.providerclient.api.ServiceApi;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderState;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateAccepted;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateNotSelected;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStatePending;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateRefused;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateSemiAccepted;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateShipped;
import com.mycompany.providerclient.navigator.Navigator;
import com.mycompany.providerclient.view.HomeView;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author aferr
 */
public class HomeController {

    private HomeView homeView;
    private JButton acceptBtn;
    private JButton shipBtn;
    private JButton completeBtn;
    private JButton refuseBtn;
    private JButton refreshBtn;
    private JButton logOutBtn;
    private JButton availableBtn;
    private JTable allOrdersTable;
    private JTable selectedOrderTable;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private Long providerId;
    private List<OrderDto> orderList;
    private SelectedOrderState selectedOrderState;
    private OrderDto selectedOrder;
    private Navigator navigator;

    public HomeController(Long providerId) {
        this.providerId = providerId;
        
        // initialize view
        homeView = new HomeView();
        homeView.setVisible(true);
        
        // get view components
        acceptBtn = homeView.getAcceptBtn();
        shipBtn = homeView.getShipBtn();
        completeBtn = homeView.getCompleteBtn();
        refuseBtn = homeView.getRefuseBtn();
        refreshBtn = homeView.getRefreshBtn();
        logOutBtn = homeView.getLogOutBtn();
        availableBtn = homeView.getAvailableBtn();
        allOrdersTable = homeView.getAllOrdersTable();
        selectedOrderTable = homeView.getSelectedOrderTable();
        
        // disable all buttons
        acceptBtn.setEnabled(false);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(false);
        
        // initialize selectedOrderState and selectedOrder
        selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        selectedOrder = null;
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // fetch all orders from server
        orderList = fetchAllOrders();
        
        // show orders on allOrdersTable
        NoEditableTableModel allOrdersTableModel = (NoEditableTableModel) allOrdersTable.getModel();
        for(OrderDto order : orderList){
            Object[] orderRow = new Object[5];
            orderRow[0] = order.getCustomer().getName();
            orderRow[1] = order.getRider().getName();
            orderRow[2] = order.getOrderType();
            orderRow[3] = order.getOrderState();
            orderRow[4] = order.getDeliveryTime();
            allOrdersTableModel.addRow(orderRow);
        }
        
        // get navigator
        navigator = Navigator.getInstance();
        
        // attach listener on allOrdersTable to update selectedOrderTable and
        // all associated buttons
        allOrdersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            
            @Override
            public void valueChanged(ListSelectionEvent event) {
                
                int selectedOrderIndex = allOrdersTable.getSelectedRow();
                if(!event.getValueIsAdjusting() && selectedOrderIndex >= 0){
                    NoEditableTableModel selectedOrderTableModel = (NoEditableTableModel) selectedOrderTable.getModel();
                    selectedOrderTableModel.setRowCount(0);
                    
                    // get selected Order
                    selectedOrder = orderList.get(selectedOrderIndex);
                    List<DishOrderAssociation> dishOrderAssociationList = selectedOrder.getDishOrderAssociations();
                    
                    // put order's info in selectedOrderTable
                    for(DishOrderAssociation dishOrderAssociation : dishOrderAssociationList){
                        DishEntity dish = dishOrderAssociation.getDish();
                        Object[] orderRow = new Object[2];
                        orderRow[0] = dish.getName();
                        orderRow[1] = dishOrderAssociation.getQuantity();
                        selectedOrderTableModel.addRow(orderRow);
                    }
                    
                    // change current state based on selected order
                    OrderState orderState = selectedOrder.getOrderState();
                    switch(orderState){
                        case PENDING:
                            selectedOrderState = new SelectedOrderStatePending(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case SEMI_ACCEPTED:
                            selectedOrderState = new SelectedOrderStateSemiAccepted(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case ACCEPTED:
                            selectedOrderState = new SelectedOrderStateAccepted(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case SHIPPED:
                            selectedOrderState = new SelectedOrderStateShipped(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case COMPLETED:
                            selectedOrderState = new SelectedOrderStateCompleted(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case REFUSED:
                            selectedOrderState = new SelectedOrderStateRefused(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        default:
                            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
                    }
                    selectedOrderState.updateButton();
                }
            }
        });
        
        // attach listener to accept button
        acceptBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            OrderType orderType = selectedOrder.getOrderType();
            
            if(!orderType.equals(OrderType.TAKE_AWAY)){
                // ask user if he wants to use riders
                int wantRiders = JOptionPane.showConfirmDialog(homeView,
                    "Do you want to use our riders?", 
                    "Question", 
                    JOptionPane.YES_NO_OPTION);
                
                // update order's type according to user response
                if(wantRiders == JOptionPane.YES_OPTION){
                    selectedOrder.setOrderType(OrderType.DELIVERY_RIDERS);
                }
                else if(wantRiders == JOptionPane.NO_OPTION) {
                    selectedOrder.setOrderType(OrderType.DELIVERY_NORIDER);
                }
                else {
                    return;
                }
            }
            selectedOrderState.accept();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
        
        // attach listener to ship button
        shipBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.ship();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
        
        // attach listener to complete button
        completeBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.complete();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
        
        // attach listener to refuse button
        refuseBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.refuse();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
        
        // attach listener to log out button
        logOutBtn.addActionListener((event) -> {
            navigator.fromHomeToLogIn(HomeController.this);
        });
        
        // attach listener to refresh button
        refreshBtn.addActionListener((event) -> {
            // <- qua ci devo mettere il codice per resettare alcune
            // robe, tipo tabelle
            
            // <- qua ci va il codice per ricaricare 
            // gli ordini dal backend
        });
    }
    
    private List<OrderDto> fetchAllOrders(){
        LinkedList<OrderDto> allOrders = new LinkedList<>();
        
        // fetch all pending orders
        Call<List<OrderDto>> getPendingOrdersCall = serviceApi.getPendingOrders(providerId);
        getPendingOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        LinkedList<OrderDto> pendingOrders = (LinkedList<OrderDto>) response.body();
                        allOrders.addAll(pendingOrders);
                    }
                    else{ // in caso di errori
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            JOptionPane.showMessageDialog(homeView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                    // Log error here since request failed
                      System.out.println("failed");
                }
        });
        
        // fetch all accepted orders
        Call<List<OrderDto>> getAcceptedOrdersCall = serviceApi.getAcceptedOrders(providerId);
        getPendingOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        LinkedList<OrderDto> acceptedOrders = (LinkedList<OrderDto>) response.body();
                        allOrders.addAll(acceptedOrders);
                    }
                    else{ // in caso di errori
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            JOptionPane.showMessageDialog(homeView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                    // Log error here since request failed
                      System.out.println("failed");
                }
        });
        
        // fetch all semi-accepted orders
        Call<List<OrderDto>> getSemiAcceptedOrdersCall = serviceApi.getSemiAcceptedOrders(providerId);
        getPendingOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        LinkedList<OrderDto> semiAcceptedOrders = (LinkedList<OrderDto>) response.body();
                        allOrders.addAll(semiAcceptedOrders);
                    }
                    else{ // in caso di errori
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            JOptionPane.showMessageDialog(homeView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                    // Log error here since request failed
                      System.out.println("failed");
                }
        });
        
        // fetch all shipped orders
        Call<List<OrderDto>> getShippedOrdersCall = serviceApi.getShippedOrders(providerId);
        getPendingOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        LinkedList<OrderDto> shippedOrders = (LinkedList<OrderDto>) response.body();
                        allOrders.addAll(shippedOrders);
                    }
                    else{ // in caso di errori
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            JOptionPane.showMessageDialog(homeView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                    // Log error here since request failed
                      System.out.println("failed");
                }
        });
        
        // fetch all completed orders
        Call<List<OrderDto>> getCompletedOrdersCall = serviceApi.getCompletedOrders(providerId);
        getPendingOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        LinkedList<OrderDto> completedOrders = (LinkedList<OrderDto>) response.body();
                        allOrders.addAll(completedOrders);
                    }
                    else{ // in caso di errori
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            JOptionPane.showMessageDialog(homeView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                    // Log error here since request failed
                      System.out.println("failed");
                }
        });
        
        return allOrders;
    }
    
    public void disposeView(){
        homeView.dispose();
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HomeController c = new HomeController(1L);
            }
        });
    }
    
}
