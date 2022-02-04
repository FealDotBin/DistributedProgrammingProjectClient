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
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents the controller associated to HomeView class.
 * It is responsible of taking the user's input to execute the appropriate
 * actions and updating the view.
 * Whenever it's necessary to send or retrieve datas to/from server, the
 * API endpoint (ServiceApi class) is being used.
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
    private JRadioButton pendingRadioButton;
    private JRadioButton semiAcceptedRadioButton;
    private JRadioButton acceptedRadioButton;
    private JRadioButton shippedRadioButton;
    private JRadioButton completedRadioButton;
    private JRadioButton refusedRadioButton;
    private JRadioButton selectedRadioButton;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private List<OrderDto> orderList;
    private SelectedOrderState selectedOrderState;
    private OrderDto selectedOrder;
    private Navigator navigator;

    /**
     * Initialize both the view and the controller by disabling "accept", "ship",
     * "complete" and "refuse" buttons, showing the pending orders on the table
     * and attaching all the necessary listeners to buttons.
     * @param providerId The id of the logged-in provider.
     */
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
        pendingRadioButton = homeView.getPendingRadioButton();
        semiAcceptedRadioButton = homeView.getSemiAcceptedRadioButton();
        acceptedRadioButton = homeView.getAcceptedRadioButton();
        shippedRadioButton = homeView.getShippedRadioButton();
        completedRadioButton = homeView.getCompletedRadioButton();
        refusedRadioButton = homeView.getRefusedRadioButton();
        
        // disable all buttons
        acceptBtn.setEnabled(false);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(false);
        
        // initialize orderList, selectedOrderState, selectedOrder, selectdRadioButton
        orderList = new LinkedList<>();
        selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        selectedOrder = null;
        selectedRadioButton = pendingRadioButton;
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // fetch provider's infos from server
        getMyInfoCall();
        
        // fetch pending orders from server and show them on allOrdersTable
        getPendingOrdersCall();
        
        // get navigator
        navigator = Navigator.getInstance();
        
        // attach listeners
        attachListenerToPendingRadioButton();
        attachListenerToAcceptedRadioButton();
        attachListenerToSemiAcceptedRadioButton();
        attachListerToShippedRadioButton();
        attachListenerToCompletedRadioButton();
        attachListenerToRefusedRadioButton();
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
    
    /**
     * Getter for providerId
     * @return the provider's id
     */
    public Long getProviderId(){
        return providerId;
    }
    
    /**
     * Destroy the homeView instance and all its components.
     */
    public void disposeView(){
        homeView.dispose();
    }
    
    /**
     * Add all orders given as input on the allOrdersTable.
     * The following infos are being displayed on the table: 
     * <ul>
     *  <li>Customer's name</li>
     *  <li>Rider's name</li>
     *  <li>Order's type</li>
     *  <li>Order's state</li>
     *  <li>Delivery time</li>
     * </ul>
     * @param orderList the list of orders to be shown on the table
     */
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
    
    /**
     * Update the "availableBtn" according to provider's isAvailable attribute.
     * If it's true, a green button with "AVAILABLE" text is shown; otherwise
     * a red button with "NOT AVAILABLE" text.
     */
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
    
    /**
     * Set controller to its initial state.
     * The orderList, allOrdersTable and selectedOrderTable are being cleared.
     */
    private void clearControllerState(){
        // clear orderList
        orderList.clear();
        
        // removes all rows from allOrdersTable
        NoEditableTableModel allOrdersTableModel = (NoEditableTableModel) allOrdersTable.getModel();
        allOrdersTableModel.setRowCount(0);
        
        // set order state to "no order selected"
        selectedOrder = null;
        selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        
        // remove all rows from selectedOrderTable
        NoEditableTableModel selectedOrderTableModel = (NoEditableTableModel) selectedOrderTable.getModel();
        selectedOrderTableModel.setRowCount(0);
    }
    
    /**
     * Send a GET request to the server to retrieve provider's infos.
     * If the response is successful, the provider's infos are successfully
     * retrieved and the "AVAILABLE" button is updated; 
     * otherwise, an error message is shown to the user.
     */
    private void getMyInfoCall(){
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
    
    /**
     * Send a GET request to the server to retrieve all pending orders.
     * If the response is successful, the pending orders are shown on the
     * "allOrdersTable", after it has been cleared out;
     * otherwise, an error message is shown to the user.
     */
    private void getPendingOrdersCall(){
        Call<List<OrderDto>> getPendingOrdersCall = serviceApi.getPendingOrders(providerId);
        getPendingOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    synchronized(orderList){
                        clearControllerState();
                        orderList = response.body();
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
    
    /**
     * Send a GET request to the server to retrieve all accepted orders.
     * If the response is successful, the accepted orders are shown on the
     * "allOrdersTable", after it has been cleared out;
     * otherwise, an error message is shown to the user.
     */
    private void getAcceptedOrdersCall(){
        Call<List<OrderDto>> getAcceptedOrdersCall = serviceApi.getAcceptedOrders(providerId);
        getAcceptedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        synchronized(orderList){
                            clearControllerState();
                            orderList = response.body();
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
    
    /**
     * Send a GET request to the server to retrieve all semi-accepted orders.
     * If the response is successful, the semi-accepted orders are shown on the
     * "allOrdersTable", after it has been cleared out;
     * otherwise, an error message is shown to the user.
     */
    private void getSemiAcceptedOrdersCall(){
        Call<List<OrderDto>> getSemiAcceptedOrdersCall = serviceApi.getSemiAcceptedOrders(providerId);
        getSemiAcceptedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        synchronized(orderList){
                            clearControllerState();
                            orderList = response.body();
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
    
    /**
     * Send a GET request to the server to retrieve all shipped orders.
     * If the response is successful, the shipped orders are shown on the
     * "allOrdersTable", after it has been cleared out;
     * otherwise, an error message is shown to the user.
     */
    private void getShippedOrdersCall(){
        Call<List<OrderDto>> getShippedOrdersCall = serviceApi.getShippedOrders(providerId);
        getShippedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
                public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        synchronized(orderList){
                            clearControllerState();
                            orderList = response.body();
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
    
    /**
     * Send a GET request to the server to retrieve all completed orders.
     * If the response is successful, the completed orders are shown on the
     * "allOrdersTable", after it has been cleared out;
     * otherwise, an error message is shown to the user.
     */
    private void getCompletedOrdersCall(){
        Call<List<OrderDto>> getCompletedOrdersCall = serviceApi.getCompletedOrders(providerId);
        getCompletedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    synchronized(orderList){
                        clearControllerState();
                        orderList = response.body();
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
    
    /**
     * Send a GET request to the server to retrieve all refused orders.
     * If the response is successful, the refused orders are shown on the
     * "allOrdersTable", after it has been cleared out;
     * otherwise, an error message is shown to the user.
     */
    private void getRefusedOrdersCall(){
        Call<List<OrderDto>> getRefusedOrdersCall = serviceApi.getRefusedOrders(providerId);
        getRefusedOrdersCall.enqueue(new Callback<List<OrderDto>>(){
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    synchronized(orderList){
                        clearControllerState();
                        orderList = response.body();
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
    
    /**
     * Send a PUT request to the server to accept an order that has to be
     * delivered by the riders.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void putRiderOrderCall(){
        Call<Void> putRiderOrderCall = serviceApi.putRiderOrder(selectedOrder.getId());
        putRiderOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrder.setOrderType(OrderType.DELIVERY_RIDERS);
                    selectedOrderState.accept();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to accept an order that has to be 
     * delivered by the deliver man.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void putNoRiderDeliveringOrderCall(){
        Call<Void> putNoRiderDeliveringOrderCall = serviceApi.putNoRiderDeliveringOrder(selectedOrder.getId());
        putNoRiderDeliveringOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrder.setOrderType(OrderType.DELIVERY_NORIDER);
                    selectedOrderState.accept();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to accept an order that has to be 
     * taken away by the customer.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void putTakeAwayOrderCall(){
        Call<Void> putTakeAwayOrderCall = serviceApi.putTakeAwayOrder(selectedOrder.getId());
        putTakeAwayOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrderState.accept();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to notify that the order is in shipping.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void putShipOrderCall(){
        Call<Void> putShipOrderCall = serviceApi.putShipOrder(selectedOrder.getId());
        putShipOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrderState.ship();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to notify that the order has been
     * taken away by the customer, and thus is completed.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void completeTakeAwayCall(){
        Call<Void> putCompletedHandOrderCall = serviceApi.putCompletedHandOrder(selectedOrder.getId());
        putCompletedHandOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrderState.complete();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to notify that the order has been
     * delivered to the customer by the delivery man, and thus is completed.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void completeNoRiderCall(){
        Call<Void> putCompletedOrderCall = serviceApi.putCompletedOrder(selectedOrder.getId());
        putCompletedOrderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrderState.complete();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to refuse an order that has to be
     * delivered by the riders.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void refuseRiderCall(){
        Call<Void> refuseRiderCall = serviceApi.refuseRider(selectedOrder.getId());
        refuseRiderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrderState.refuse();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to refuse an order that has to be
     * delivered by the delivery man.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void refuseNoRiderCall(){
        Call<Void> refuseNoRiderCall = serviceApi.refuseNoRider(selectedOrder.getId());
        refuseNoRiderCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrderState.refuse();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to refuse an order that has to be
     * taken away by the customer.
     * If the response is successful, the order's state is being updated;
     * otherwise, an error message is shown to the user.
     */
    private void refuseTakeAwayCall(){
        Call<Void> refuseTakeAwayCall = serviceApi.refuseTakeAway(selectedOrder.getId());
        refuseTakeAwayCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    selectedOrderState.refuse();
                    
                    // remove order from list and remove order selection
                    orderList.remove(selectedOrder);
                    selectedOrder = null;
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
    
    /**
     * Send a PUT request to the server to change the provider's availability.
     * Thus if they are available, they will not be anymore, after the request; 
     * otherwise they will.
     * If the response is successful, the provider's availability has been changed;
     * otherwise, an error message is shown to the user.
     * @param isAvailable true if the provider is available for the customers;
     * false otherwise
     */
    private void setAvailCall(Boolean isAvailable){
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
    }
    
    /**
     * Attach an ActionListener to pendingRadioButton in order to get
     * the pending orders when the button is pressed.
     */
    private void attachListenerToPendingRadioButton(){
        pendingRadioButton.addActionListener(event -> {
            selectedRadioButton = pendingRadioButton;
            getPendingOrdersCall();
        });
    }
    
    /**
     * Attach an ActionListener to acceptedRadioButton in order to get
     * the accepted orders when the button is pressed.
     */
    private void attachListenerToAcceptedRadioButton(){
        acceptedRadioButton.addActionListener(event -> {
            selectedRadioButton = acceptedRadioButton;
            getAcceptedOrdersCall();
        });
    }
    
    /**
     * Attach an ActionListener to semiAcceptedRadioButton in order to get
     * the semi-accepted orders when the button is pressed.
     */
    private void attachListenerToSemiAcceptedRadioButton(){
        semiAcceptedRadioButton.addActionListener(event -> {
            selectedRadioButton = semiAcceptedRadioButton;
            getSemiAcceptedOrdersCall();
        });
    }
    
    /**
     * Attach an ActionListener to shippedRadioButton in order to get
     * the shipped orders when the button is pressed.
     */
    private void attachListerToShippedRadioButton(){
        shippedRadioButton.addActionListener(event -> {
            selectedRadioButton = shippedRadioButton;
            getShippedOrdersCall();
        });
    }
    
    /**
     * Attach an ActionListener to completeddRadioButton in order to get
     * the completed orders when the button is pressed.
     */
    private void attachListenerToCompletedRadioButton(){
        completedRadioButton.addActionListener(event -> {
            selectedRadioButton = completedRadioButton;
            getCompletedOrdersCall();
        });
    }
    
    /**
     * Attach an ActionListener to refusedRadioButton in order to get
     * the refused orders when the button is pressed.
     */
    private void attachListenerToRefusedRadioButton(){
        refusedRadioButton.addActionListener(event -> {
            selectedRadioButton = refusedRadioButton;
            getRefusedOrdersCall();
        });
    }
    
    /**
     * Attach a ListSelectionListener to allOrdersTable in order to 
     * show the dishes and their respective quantities on the 
     * selectedOrderTable.
     */
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
    
    /**
     * Attach an ActionListener to semiAcceptedRadioButton in order to refresh
     * the table's content with updated orders, when the button is pressed.
     */
    private void attachListenerToRefreshBtn(){
        refreshBtn.addActionListener((event) -> {
            clearControllerState();
            
            // disable all buttons
            acceptBtn.setEnabled(false);
            shipBtn.setEnabled(false);
            completeBtn.setEnabled(false);
            refuseBtn.setEnabled(false);
            
            // trigger listener on currently selected radio button
            selectedRadioButton.doClick();
        });
    }
    
    /**
     * Attach an ActionListener to acceptButton in order to accept the selected
     * order, when the button is pressed.
     * In particular, if the order has to be delivered, a dialog is being shown
     * to the user, asking them if they want to deliver the order by riders or
     * delivery man.
     */
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
            else{
                putTakeAwayOrderCall();
            }
        });
    }
    
    /**
     * Attach an ActionListener to shipButton in order to ship the selected
     * order, when the button is pressed.
     */
    private void attachListenerToShipBtn(){
        shipBtn.addActionListener((event) -> {
            OrderType selectedOrderType = selectedOrder.getOrderType();
            if(selectedOrderType.equals(OrderType.DELIVERY_NORIDER)){
                putShipOrderCall();
            }
            else{
                selectedOrderState.ship();
                selectedOrderState = new SelectedOrderStateNotSelected(homeView);
            }
        });
    }
    
    /**
     * Attach an ActionListener to completeButton in order to complete the selected
     * order, when the button is pressed.
     */
    private void attachListenerToCompleteBtn(){
        completeBtn.addActionListener((event) -> {
            OrderType selectedOrderType = selectedOrder.getOrderType();
            switch(selectedOrderType){
                case TAKE_AWAY:
                    completeTakeAwayCall();
                    break;
                case DELIVERY_NORIDER:
                    completeNoRiderCall();
                    break;
                default:
                    selectedOrderState.complete();
                    selectedOrderState = new SelectedOrderStateNotSelected(homeView);
            }
        });
    }
    
    /**
     * Attach an ActionListener to refuseButton in order to refuse the selected
     * order, when the button is pressed.
     */
    private void attachListenerToRefuseBtn(){
        refuseBtn.addActionListener((event) -> {
            OrderType selectedOrderType = selectedOrder.getOrderType();
            switch(selectedOrderType){
                case TAKE_AWAY:
                    refuseTakeAwayCall();
                    break;
                case DELIVERY:
                    refuseNoRiderCall();
                    break;
                default:
                    selectedOrderState.refuse();
                    selectedOrderState = new SelectedOrderStateNotSelected(homeView);
            }
        });
    }
    
    /**
     * Attach an ActionListener to manageMenuButton in order to switch to
     * the "manage menu" window, when the button is pressed.
     */
    private void attachListenerToManageMenuBtn(){
        manageMenuBtn.addActionListener(event -> {
            navigator.fromHomeToMenuManager(HomeController.this);
        });
    }
    
    /**
     * Attach an ActionListener to updateAccountButton in order to switch to
     * the "update account" window, when the button is pressed.
     */
    private void attachListenerToUpdateAccountBtn(){
        updateAccountBtn.addActionListener(event -> {
            navigator.fromHomeToUpdateAccount(HomeController.this);
        });
    }
    
    /**
     * Attach an ActionListener to logOutButton in order to switch to
     * the "log in" window, when the button is pressed.
     */
    private void attachListenerToLogOutBtn(){
        logOutBtn.addActionListener((event) -> {
            navigator.fromHomeToLogIn(HomeController.this);
        });
    }
    
    /**
     * Attach an ActionListener to availableButton in order to toggle the
     * available status, when the button is pressed.
     */
    private void attachListenerToAvailableBtn(){
        availableBtn.addActionListener(event -> {
            Boolean isAvailable = provider.getIsAvailable();
            setAvailCall(isAvailable);
        });
    }
}
