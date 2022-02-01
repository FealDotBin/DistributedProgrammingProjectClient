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
import com.mycompany.common.model.dto.user.RiderDto;
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
import com.mycompany.providerclient.model.ProviderEntity;
import com.mycompany.providerclient.navigator.Navigator;
import com.mycompany.providerclient.view.HomeView;
import java.awt.Color;
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

    private Long providerId;
    private ProviderEntity provider;
    private HomeView homeView;
    private JButton acceptBtn;
    private JButton shipBtn;
    private JButton completeBtn;
    private JButton refuseBtn;
    private JButton refreshBtn;
    private JButton availableBtn;
    private JTable allOrdersTable;
    private JTable selectedOrderTable;
    private JButton manageMenuBtn;
    private JButton updateAccountBtn;
    private JButton logOutBtn;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
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
        availableBtn = homeView.getAvailableBtn();
        allOrdersTable = homeView.getAllOrdersTable();
        selectedOrderTable = homeView.getSelectedOrderTable();
        manageMenuBtn = homeView.getManageMenuBtn();
        updateAccountBtn = homeView.getUpdateAccountBtn();
        logOutBtn = homeView.getLogOutBtn();
        
        // disable all buttons
        acceptBtn.setEnabled(false);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(false);
        
        // initialize orderList, selectedOrderState and selectedOrder
        orderList = new LinkedList<>();
        selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        selectedOrder = null;
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // fetch provider's infos from server
        fetchProviderFromServer();
        
        // fetch all orders from server and show them on allOrdersTable
        fetchAllOrdersFromServer();
        
        // get navigator
        navigator = Navigator.getInstance();
        
        // attach listeners
        attachListenerToAllOrdersTable();
        attachListenerToAcceptBtn();
        attachListenerToShipBtn();
        attachListenerToCompleteBtn();
        attachListenerToRefuseBtn();
        attachListenerToRefreshBtn();
        attachListenerToManageMenuBtn();
        attachListenerToUpdateAccountBtn();
        attachListenerToLogOutBtn();
        attachListenerToAvailableBtn();
    }
    
    private void fetchProviderFromServer(){
        Call<ProviderEntity> getMyInfoCall = serviceApi.getMyInfo(providerId);
        getMyInfoCall.enqueue(new Callback<ProviderEntity>(){
            @Override
            public void onResponse(Call<ProviderEntity> call, Response<ProviderEntity> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    provider = response.body();
                    updateAvailableBtn();
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
            public void onFailure(Call<ProviderEntity> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
            }
        });
    }
    
    private void fetchAllOrdersFromServer(){
        // fetch all pending orders
        Call<List<OrderDto>> getPendingOrdersCall = serviceApi.getPendingOrders(providerId);
        getPendingOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    List<OrderDto> pendingOrders = response.body();
                    synchronized(orderList){
                        orderList.addAll(pendingOrders);
                        addOrdersOnTable(pendingOrders);
                    }
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
        getAcceptedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        List<OrderDto> acceptedOrders = (List<OrderDto>) response.body();
                        synchronized(orderList){
                            orderList.addAll(acceptedOrders);
                            addOrdersOnTable(acceptedOrders);
                        }
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
        getSemiAcceptedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        List<OrderDto> semiAcceptedOrders = (List<OrderDto>) response.body();
                        synchronized(orderList){
                            orderList.addAll(semiAcceptedOrders);
                            addOrdersOnTable(semiAcceptedOrders);
                        }
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
        getShippedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        List<OrderDto> shippedOrders = (List<OrderDto>) response.body();
                        synchronized(orderList){
                            orderList.addAll(shippedOrders);
                            addOrdersOnTable(orderList);
                        }
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
        getCompletedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        List<OrderDto> completedOrders = (List<OrderDto>) response.body();
                        synchronized(orderList){
                            orderList.addAll(completedOrders);
                            addOrdersOnTable(orderList);
                        }
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
    }
    
    private void addOrdersOnTable(List<OrderDto> orderList){
        NoEditableTableModel allOrdersTableModel = (NoEditableTableModel) allOrdersTable.getModel();
        for(OrderDto order : orderList){
            RiderDto rider = null;
            Object[] orderRow = new Object[5];
            orderRow[0] = order.getCustomer().getName();
            orderRow[1] = ((rider = order.getRider()) == null ? "" : rider.getName());
            orderRow[2] = order.getOrderType();
            orderRow[3] = order.getOrderState();
            orderRow[4] = order.getDeliveryTime();
            allOrdersTableModel.addRow(orderRow);
        }
    }
    
    private void attachListenerToAllOrdersTable(){
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
    }
    
    private void attachListenerToRefreshBtn(){
        refreshBtn.addActionListener((event) -> {
            // <- qua ci devo mettere il codice per resettare alcune
            // robe, tipo tabelle
            
            // <- qua ci va il codice per ricaricare 
            // gli ordini dal backend
        });
    }
    
    private void attachListenerToAcceptBtn(){
        acceptBtn.addActionListener((event) -> {
            OrderType orderType = selectedOrder.getOrderType();
            
            if(!orderType.equals(OrderType.TAKE_AWAY)){
                // ask user if he wants to use riders
                int wantRiders = JOptionPane.showConfirmDialog(homeView,
                    "Do you want to use our riders?", 
                    "Question", 
                    JOptionPane.YES_NO_OPTION);
                
                // update order's type according to user response
                if(wantRiders == JOptionPane.YES_OPTION){
                    putRiderOrderCall();
                }
                else if(wantRiders == JOptionPane.NO_OPTION) {
                    putNoRiderDeliveringOrderCall();
                }
                else {
                    return;
                }
            }
            putTakeAwayOrderCall();
        });
    }
    
    private void putRiderOrderCall(){
        Call<Void> putRiderOrderCall = serviceApi.putRiderOrder(selectedOrder.getId());
        putRiderOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrder.setOrderType(OrderType.DELIVERY_RIDERS);
                    selectedOrderState.accept();
                    selectedOrderState = new SelectedOrderStateNotSelected(homeView);
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
            public void onFailure(Call<Void> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
            }
        });
    }
    
    private void putNoRiderDeliveringOrderCall(){
        Call<Void> putNoRiderDeliveringOrderCall = serviceApi.putNoRiderDeliveringOrder(selectedOrder.getId());
        putNoRiderDeliveringOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrder.setOrderType(OrderType.DELIVERY_NORIDER);
                    selectedOrderState.accept();
                    selectedOrderState = new SelectedOrderStateNotSelected(homeView);
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
            public void onFailure(Call<Void> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
            }
        });
    }
    
    private void putTakeAwayOrderCall(){
        Call<Void> putTakeAwayOrderCall = serviceApi.putTakeAwayOrder(selectedOrder.getId());
        putTakeAwayOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrderState.accept();
                    selectedOrderState = new SelectedOrderStateNotSelected(homeView);
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
            public void onFailure(Call<Void> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
            }
        });
    }
    
    private void attachListenerToShipBtn(){
        shipBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.ship();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
    }
    
    private void attachListenerToCompleteBtn(){
        completeBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.complete();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
    }
    
    private void attachListenerToRefuseBtn(){
        refuseBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.refuse();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
    }
    
    private void attachListenerToManageMenuBtn(){
        manageMenuBtn.addActionListener(event -> {
            navigator.fromHomeToMenuManager(HomeController.this);
        });
    }
    
    private void attachListenerToUpdateAccountBtn(){
        updateAccountBtn.addActionListener(event -> {
            navigator.fromHomeToUpdateAccount(HomeController.this);
        });
    }
    
    private void attachListenerToLogOutBtn(){
        logOutBtn.addActionListener((event) -> {
            navigator.fromHomeToLogIn(HomeController.this);
        });
    }
    
    private void attachListenerToAvailableBtn(){
        availableBtn.addActionListener(event -> {
            Boolean isAvailable = provider.getIsAvailable();
            Call<Void> setAvailCall = serviceApi.setAvail(providerId, !isAvailable);
            setAvailCall.enqueue(new Callback<Void>(){
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        provider.setIsAvailable(!isAvailable);
                        updateAvailableBtn();
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
                public void onFailure(Call<Void> call, Throwable t) {
                    // Log error here since request failed
                      System.out.println("failed");
                }
            });   
        });
    }
    
    private void updateAvailableBtn(){
        if(provider.getIsAvailable()){
            availableBtn.setText("AVAILABLE");
            availableBtn.setBackground(new Color(44,73,129));
        }
        else{
            availableBtn.setText("NOT AVAILABLE");
            availableBtn.setBackground(new Color(146,43,32));
        }
    }
    
    public Long getProviderId(){
        return providerId;
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
