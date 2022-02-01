/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JButtonEditor;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.CustomerFrameHistory1;
import com.mycompany.providerclient.controller.LogInController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 */
public class CustomerOrderHistoryController {

    private CustomerFrameHistory1 historyView;
    private JButton updateButton;
    private JButton homeButton;
    private JButton logOutButton;
    private JButton balanceButton;
    private JTable orderTable;

    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator nav;

    private Long customerId;
    private OrderDto currentOrder;
    private OrderDto selectedOrder;
    private List<OrderDto> orderHistory;

    public CustomerOrderHistoryController(Long customerId) {
        this.customerId = customerId;
        //View creation
        historyView = new CustomerFrameHistory1();
        //Component getter

        updateButton = historyView.getAccountBtn();
        logOutButton = historyView.getLogOutBtn();
        balanceButton = historyView.getBalanceBtn();
        homeButton = historyView.getHomeBtn();
        orderTable = historyView.getOrderTable();

        //View navigator creation
        nav = Navigator.getInstance();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);

        //When logout button is pressed logIn view is displayed
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromHistory1ToLogIn(CustomerOrderHistoryController.this);
            }
        });

        //When update account button is pressed update customer information view is displayed
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromHistory1ToUpdate(CustomerOrderHistoryController.this);
            }
        });

        //When balance button is pressed balance update view is displayed
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromHistory1ToBalance(CustomerOrderHistoryController.this);
            }
        });

        //Set home button text and his action listener based on the presence of current order
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    currentOrder = response.body();
                    homeButton.setText("Current Order");
                    homeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            nav.fromHistory1ToOrderViewer(CustomerOrderHistoryController.this);
                        }

                    });
                } else {
                    homeButton.setText("Create Order");
                    nav.fromHistory1ToProviderSelection(CustomerOrderHistoryController.this);
                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(historyView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        Call<List<OrderDto>> orderListCall = apiService.getCustomerHistory(this.customerId);
        orderListCall.enqueue(new Callback<List<OrderDto>>() {
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {
                if (response.isSuccessful()) {
                    orderHistory = response.body();
                    DefaultTableModel orderTableModel = (DefaultTableModel) orderTable.getModel();
                    Object[] row = new Object[4];
                    for (OrderDto order : orderHistory) {
                        row[0] = order.getId();
                        row[1] = order.getProvider().getProviderName();
                        row[2] = order.getRider()!=null ? order.getRider().getName():"";
                        row[3] = order.getDeliveryTime();
                        orderTableModel.addRow(row);
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(historyView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<OrderDto>> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(historyView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        JButtonEditor providerTableEditor = new JButtonEditor("View Menù", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                button.setBackground(new java.awt.Color(44, 73, 129));
                button.setFont(new java.awt.Font("Segoe UI", 1, 14));
                button.setForeground(new Color(200, 200, 200));
                int selectedRow = orderTable.getSelectedRow();
                selectedOrder = orderHistory.get(selectedRow);
                nav.fromHistory1ToMoreInfo(CustomerOrderHistoryController.this);
            }

        });

        historyView.setVisible(true);

    }

    public void disposeView() {
        historyView.dispose();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public OrderDto getCurrentOrder() {
        return currentOrder;
    }

    public OrderDto getSelectedOrder() {
        return selectedOrder;
    }

}
