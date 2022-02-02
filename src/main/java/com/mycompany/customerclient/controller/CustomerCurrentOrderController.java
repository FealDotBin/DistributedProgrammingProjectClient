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

        //When a dish in the table are clicked more informations are showd in below text areas
        dishTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
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
        });

        //Action Perform when refresh button is pressed: request server for costumer's current order and if it is uptadete modify
        //view
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(CustomerCurrentOrderController.this.customerId);
                currentOrderCall.enqueue(new Callback<OrderDto>() {
                    @Override
                    public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                        if (response.isSuccessful()) {
                            OrderDto newCurrentOrder = response.body();
                            currentOrder = newCurrentOrder;
                            if (newCurrentOrder.getOrderState().equals(CustomerCurrentOrderController.this.currentOrder.getOrderState())) {
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
                        }else{
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
        });

        //When logout button is pressed logIn view is displayed
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderViewertoLogIn(CustomerCurrentOrderController.this);
            }
        });

        //When update account button is pressed update customer information view is displayed
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderViewertoUpdate(CustomerCurrentOrderController.this);
            }
        });

        //When balance button is pressed balance update view is displayed
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderViewertoToBalance(CustomerCurrentOrderController.this);
            }
        });

        // When logout history button is pressed display customer order history view
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderViewertoHistory(CustomerCurrentOrderController.this);
            }

        });
        orderView.setVisible(true);
    }
    
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
                    File file = new File("persistentOrder.txt");
                    if(file.delete()){
                        System.out.println("File cancellato con successo");
                    }
                    else
                        System.out.println("Problemi con la cancellazione del file");
                    nav.fromOrderViewerToProviderSelection(CustomerCurrentOrderController.this);
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
    
    public void disposeView() {
        this.orderView.dispose();
    }

    public Long getCustomerId() {
        return this.customerId;
    }

}
