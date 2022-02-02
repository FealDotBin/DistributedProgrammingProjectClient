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
import com.mycompany.customerclient.view.CustomerFrameHistory1;
import com.mycompany.customerclient.view.MoreOrderInformation;
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
public class OrderMoreInformationController {

    private MoreOrderInformation moreInformationView;

    private JButton updateButton;
    private JButton balanceButton;
    private JButton homeButton;
    private JButton logOutButton;
    private JButton backButton;
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
    private OrderDto selectedOrder;
    private OrderDto currentOrder;

    public OrderMoreInformationController(Long customerId, OrderDto selectedOrder) {
        this.customerId = customerId;
        this.selectedOrder = selectedOrder;
        //View creation
        moreInformationView = new MoreOrderInformation();
        //Component getter

        updateButton = moreInformationView.getAccountBtn();
        logOutButton = moreInformationView.getLogOutBtn();
        balanceButton = moreInformationView.getBalanceBtn();
        homeButton = moreInformationView.getHomeBtn();
        backButton = moreInformationView.getBackBtn();
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

        //When back button is pressed, order history view si displayed
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromMoreInfoToHistory(OrderMoreInformationController.this);
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
                            changeToHome();
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

        //Fill dish table
        List<DishOrderAssociation> associations = selectedOrder.getDishOrderAssociations();
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
        //fill information field about selected order
        Double orderPrice = selectedOrder.getPrice();
        totalField.setText(orderPrice.toString());
        providerField.setText(selectedOrder.getProvider().getProviderName());
        RiderDto rider = selectedOrder.getRider();
        String riderName = "";
        if (rider != null) {
            riderName = rider.getName();
        } else {
            riderName = "";
        }
        riderField.setText(riderName);
        deliveryField.setText(selectedOrder.getDeliveryTime());
        orderStateField.setText(selectedOrder.getOrderState().toString());
        orderTypeField.setText(selectedOrder.getOrderType().toString());

        //When a dish in the table are clicked more informations are showd in below text areas
        dishTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = dishTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        DishEntity selectOrder = associations.get(selectedRow).getDish();
                        dishDescriptionTextArea.setText(selectOrder.getDescription());
                        dishIngredientTextArea.setText(selectOrder.getIngredients().toString());

                    } else {
                        dishDescriptionTextArea.setText("");
                        dishIngredientTextArea.setText("");
                    }
                }
            }
        });

        moreInformationView.setVisible(true);

    }

    private void changeToHome() {
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(OrderMoreInformationController.this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    currentOrder = response.body();
                    nav.fromMoreInfoToOrderViewer(OrderMoreInformationController.this);
                } else {
                    getOrderById();
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
    }

    private void getOrderById() {
        Call<OrderDto> getOrderById = apiService.getOrderDTO(OrderMoreInformationController.this.currentOrder.getId());
        getOrderById.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    OrderDto orderGhost = response.body();
                    if (orderGhost.getOrderState().equals(OrderState.REFUSED)) {
                        JOptionPane.showMessageDialog(moreInformationView, "YOUR CURRENT ORDER HAVE BEEN REFUSED BUT YOU GOT: " + orderGhost.getPrice() + " MONEY BACK", "Order Refused", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(moreInformationView, "YOUR CURRENT ORDER HAVE BEEN COMPLETED", "Order Completed", JOptionPane.INFORMATION_MESSAGE);
                    }
                    File file = new File("persistentOrder.txt");
                    if (file.delete()) {
                        System.out.println("File cancellato con successo");
                    } else {
                        System.out.println("Problemi con la cancellazione del file");
                    }
                    nav.fromMoreInfoToProviderSelection(OrderMoreInformationController.this);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(moreInformationView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

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
    }

    public void disposeView() {
        moreInformationView.dispose();
    }

    public Long getCustomerId() {
        return customerId;
    }

}
