/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.model.dao.order.DishEntity;
import com.mycompany.common.model.dao.order.DishOrderAssociation;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.RiderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.CurrentOrderView;
import com.mycompany.customerclient.view.customerUpdate;
import com.sun.tools.javac.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 * This class represents CurrentOrderView GUI controller.
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restfull server when needed:
 *  -To retrieve information about current order
 *  -To obtain current order's state update
 *  -To decide which controller to invoke after a button has been pressed
 */
public class CustomerCurrentOrderController {

    private CurrentOrderView orderView;

    private JTextField deliveryTimeField;
    private JTextField orderStateField;
    private JTextField providerField;
    private JTextField riderField;
    private JTextField totalField;
    private JTextField orderTypeField;
    private JButton refreshButton;
    private JButton updateButton;
    private JButton balanceButton;
    private JButton historyButton;
    private JButton logOutButton;
    private JTextArea dishDescriptionTextArea;
    private JTextArea dishIngredientTextArea;
    private JTable dishTable;

    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator nav;

    private Long customerId;
    private OrderDto currentOrder;

    /**
     * Initialize view's obtaing customer current order information from server.
     * Initialize dish table with dishes selected by the customer
     * Add listener to refresh button to get from server updata about current order
     * Add listener to dish table to show more information about the dish selected in the dedicated text areas
     * Add listeners to other buttons to navigate from this controller to the next
     * @param customerId represents the logged customer's id
     * 
     */
    public CustomerCurrentOrderController(Long customerId) {
        this.customerId = customerId;

        //View creation
        orderView = new CurrentOrderView();
        //Component getter
        deliveryTimeField = orderView.getDeliveryTimeField();
        orderStateField = orderView.getOrderStateField();
        providerField = orderView.getProviderField();
        riderField = orderView.getRiderField();
        totalField = orderView.getTotalField();
        orderTypeField = orderView.getOrderTypeField();
        refreshButton = orderView.getRefreshBtn();
        updateButton = orderView.getAccountBtn();
        logOutButton = orderView.getLogoutBtn();
        balanceButton = orderView.getBalanceBtn();
        historyButton = orderView.getHistoryBtn();
        dishDescriptionTextArea = orderView.getDishDescriptionTextArea();
        dishIngredientTextArea = orderView.getDishIngredientTextArea();
        dishTable = orderView.getDishTable();

        //View navigator creation
        nav = Navigator.getInstance();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);

        //Fill GUI's field with current order's information
        fillFields();

        //When a dish in the table are clicked more informations are showd in below text areas
        dishTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                dishTableRowClicked(event);
            }
        });

        //Action Perform when refresh button is pressed: request server for costumer's current order and if it is uptadete modify
        //view.
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });

        //When log out  button is pressed check if the current order is completed or refuse and then 
        //display log in view
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherButtonExit();
                nav.fromOrderViewertoLogIn(CustomerCurrentOrderController.this);
            }
        });

        //When update account button is pressed check if the current order is completed or refuse and then 
        //display update customer information view 
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherButtonExit();
                nav.fromOrderViewertoUpdate(CustomerCurrentOrderController.this);
            }
        });

        //When balance button is pressed check if the current order is completed or refuse and then 
        //display manage balance view
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherButtonExit();
                nav.fromOrderViewertoToBalance(CustomerCurrentOrderController.this);
            }
        });

        //When update history button is pressed check if the current order is completed or refuse and then 
        //display customer's order history view
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherButtonExit();
                nav.fromOrderViewertoHistory(CustomerCurrentOrderController.this);
            }

        });
        orderView.setVisible(true);
    }
     /**
     *The method sends a GET to get the status of the last current order.
     *If this is completed or refused, the user is notified together with any refund for the order placed by him.
     *Furthermore in case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void getOrderById() {
        Call<OrderDto> getOrderById = apiService.getOrderDTO(this.currentOrder.getId());
        getOrderById.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    OrderDto orderGhost = response.body();
                    if (orderGhost.getOrderState().equals(OrderState.REFUSED)) {
                        JOptionPane.showMessageDialog(orderView, "YOUR CURRENT ORDER HAVE BEEN REFUSED BUT YOU GOT: " + orderGhost.getPrice() + " MONEY BACK", "Order Refused", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(orderView, "YOUR CURRENT ORDER HAVE BEEN COMPLETED", "Order Completed", JOptionPane.INFORMATION_MESSAGE);
                    }
                    File file = new File("customer"+customerId+"/persistentOrder.txt");
                    if (file.delete()) {
                        System.out.println("File cancellato con successo");
                    } else {
                        System.out.println("Problemi con la cancellazione del file");
                    }
                    
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(orderView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(orderView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
    }
    /**
    * The method send a GET request to the server to fill the interface fields. 
    * In particular, the table is filled with the list of dishes present in the current order 
    * while the other fields are filled with the total cost, the delivery / take-away time, the type of delivery,
    * status, the name of the rider to whom the order has been assigned (if any), 
    * the name of the provider with whom the order was placed.
    * In case of a communication error with the server, messages are displayed to inform the customer.
    */
    private void fillFields(){
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    currentOrder = response.body();
                    //Fill dish table
                    List<DishOrderAssociation> associations = currentOrder.getDishOrderAssociations();
                    DefaultTableModel dishTableModel = (DefaultTableModel) dishTable.getModel();
                    DishEntity dish;
                    Object row[] = new Object[3];
                    for (DishOrderAssociation ass : associations) {
                        ass.getQuantity();
                        dish = ass.getDish();
                        row[0] = dish.getName();
                        row[1] = dish.getPrice();
                        row[2] = ass.getQuantity();
                        dishTableModel.addRow(row);
                    }
                    //fill information field about current order
                    Double orderPrice = currentOrder.getPrice();
                    totalField.setText(orderPrice.toString());
                    providerField.setText(currentOrder.getProvider().getProviderName());
                    RiderDto rider = currentOrder.getRider();
                    String riderName = "";
                    if (rider != null) {
                        riderName = rider.getName();
                    } else {
                        riderName = "";
                    }
                    riderField.setText(riderName);
                    deliveryTimeField.setText(currentOrder.getDeliveryTime());
                    orderStateField.setText(currentOrder.getOrderState().toString());
                    orderTypeField.setText(currentOrder.getOrderType().toString());
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(orderView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(orderView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
    }
    
    /**
     * The method shows in the appropriate text area the description and the list of ingredients corresponding to the dish selected in the table.
     * @param event represents the event fired when the selected row on the table changes.
     */
    private void dishTableRowClicked(ListSelectionEvent event){
        if (!event.getValueIsAdjusting()) {
            int selectedRow = dishTable.getSelectedRow();
            if (selectedRow >= 0) {
                DishEntity selectOrder = currentOrder.getDishOrderAssociations().get(selectedRow).getDish();
                dishDescriptionTextArea.setText(selectOrder.getDescription());
                dishIngredientTextArea.setText(selectOrder.getIngredients().toString());

            } else {
                dishDescriptionTextArea.setText("");
                dishIngredientTextArea.setText("");
            }
        }
    }
    
    /**
     * The method sends a GET to the server to get updates on the current order.
     * After that it updates the GUI fields that show its information.
     * In case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void refresh(){
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(CustomerCurrentOrderController.this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    OrderDto newCurrentOrder = response.body();
                    currentOrder = newCurrentOrder;
                    if (!newCurrentOrder.getOrderState().equals(CustomerCurrentOrderController.this.currentOrder.getOrderState())) {
                        JOptionPane.showMessageDialog(orderView, "NO UPDATE", "Information", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(orderView, "UPDATE: New state" + newCurrentOrder.getOrderState(), "Information", JOptionPane.INFORMATION_MESSAGE);
                    }
                    CustomerCurrentOrderController.this.currentOrder = newCurrentOrder;
                    Double orderPrice = newCurrentOrder.getPrice();
                    totalField.setText(orderPrice.toString());
                    providerField.setText(newCurrentOrder.getProvider().getProviderName());
                    RiderDto rider = currentOrder.getRider();
                    String riderName = "";
                    if (rider != null) {
                        riderName = rider.getName();
                    } else {
                        riderName = "";
                    }
                    riderField.setText(riderName);
                    deliveryTimeField.setText(newCurrentOrder.getDeliveryTime());
                    orderStateField.setText(newCurrentOrder.getOrderState().toString());
                    orderTypeField.setText(newCurrentOrder.getOrderType().toString());
                } else {
                    getOrderById();
                    nav.fromOrderViewerToProviderSelection(CustomerCurrentOrderController.this);
                }
            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(orderView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    /**
     * The method sends a GET to the server to get the current order. If this is not present it is completed or rejected.
    I* In this case, another GET is made to obtain the latest status of the current order.
     * The latter is shown to the costumer with a notification together with any refund.
     * In case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void otherButtonExit() {
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(CustomerCurrentOrderController.this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (!response.isSuccessful()) {
                    getOrderById();
                }
            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(orderView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    /**
     * Remove the CurrentOrderView view from the visualization
     */
    public void disposeView() {
        this.orderView.dispose();
    }
    
    /**
     * Return the logged customer's id
     * @return String that represent the logged customer's id 
     * 
     */
    public Long getCustomerId() {
        return this.customerId;
    }

}
