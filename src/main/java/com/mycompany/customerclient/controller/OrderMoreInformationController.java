/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.CustomerFrameHistory1;
import com.mycompany.customerclient.view.MoreOrderInformation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 */
public class OrderMoreInformationController {
    private MoreOrderInformation moreInformationView;
    
    private JButton updateButton;
    private JButton balanceButton;
    private JButton homeButton;
    private JButton logOutButton;
    private JTextField deliveryField;
    private JTextField orderTypeField;
    private JTextField orderStateField;
    private JTextField providerField;
    private JTextField riderField;
    private JTextField totalField;
    private JTextArea dishDescriptionTextArea;
    private JTextArea dishIngredientTextArea;
    private JTable dishTable;
    
    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator nav;

    private Long customerId;
    private OrderDto currentOrder;

    public OrderMoreInformationController(Long customerId, OrderDto selectedOrder) {
        this.customerId = customerId;
        //View creation
        moreInformationView = new MoreOrderInformation();
        //Component getter

        updateButton = moreInformationView.getAccountBtn();
        logOutButton = moreInformationView.getLogOutBtn();
        balanceButton = moreInformationView.getBalanceBtn();
        homeButton = moreInformationView.getHomeBtn();
        deliveryField = moreInformationView.getDeliveryField();
        orderStateField = moreInformationView.getStateField();
        orderTypeField = moreInformationView.getOrderTypeField();
        providerField = moreInformationView.getProviderField();
        riderField = moreInformationView.getRiderField();
        totalField = moreInformationView.getTotalTextField();
        dishDescriptionTextArea = moreInformationView.getDishDescriptionTextArea();
        dishIngredientTextArea = moreInformationView.getDishIngredientTextArea();
        dishTable = moreInformationView.getDishTable();
        

        //View navigator creation
        nav = Navigator.getInstance();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);

        //When logout button is pressed logIn view is displayed
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromMoreInfoToLogIn(OrderMoreInformationController.this);
            }
        });

        //When update account button is pressed update customer information view is displayed
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromMoreInfoToUpdate(OrderMoreInformationController.this);
            }
        });

        //When balance button is pressed balance update view is displayed
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromMoreInfoToBalance(OrderMoreInformationController.this);
            }
        });
        
        
        
        //Set home button text and his action listener based on the presence of current order
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    currentOrder=response.body();
                    homeButton.setText("Current Order");
                    homeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        nav.fromMoreInfoToOrderViewer(OrderMoreInformationController.this);
                    }

                });
                } else {
                    homeButton.setText("Create Order");
                    nav.fromMoreInfoToProviderSelection(OrderMoreInformationController.this);
                }
                
            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(moreInformationView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });

        moreInformationView.setVisible(true);

    }

      public void disposeView() {
        moreInformationView.dispose();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public OrderDto getCurrentOrder() {
        return currentOrder;
    }

}
