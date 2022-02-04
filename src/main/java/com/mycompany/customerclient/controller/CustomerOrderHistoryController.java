/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JButtonEditor;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.CustomerFrameHistory1;
import com.mycompany.providerclient.controller.LogInController;
import com.sun.tools.javac.Main;
import java.awt.Color;
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
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 * This class represents CustomerFrameHistory1 GUI controller. 
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restfull server when needed:
 * -To obtain customer's orders history 
 * -To decide which controller to invoke after home button has been pressed
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
    private OrderDto selectedOrder;
    private OrderDto currentOrder;
    private List<OrderDto> orderHistory;

    
   /**
     * Initialize order table history obtaining it from server.
     * Add listener to buttons in order table to select and order that you want know more
     * Add listeners to other buttons to navigate from this controller to the next
     * @param customerId represents the logged customer's id
     * 
     */
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
        addHomeButtonLogic();
        
        //Fill table with order history
        fillOrderTable();

        //When a button in row is pressed show GUI with more information about the information order
        JButtonEditor orderTableEditor = new JButtonEditor("View Menù", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTableButtonLogic(e);
            }

        });
        orderTable.setDefaultEditor(JButton.class, orderTableEditor);

        historyView.setVisible(true);

    }
    
    /**
     *The method sets the appearance of the button after it is pressed.
     *Also the GUI will be shown which describes the order more in depth.
     * @param e events that occurs when a table button is pressed
     */
    private void addTableButtonLogic(ActionEvent e){
        JButton button = (JButton) e.getSource();
        button.setBackground(new java.awt.Color(44, 73, 129));
        button.setFont(new java.awt.Font("Segoe UI", 1, 14));
        button.setForeground(new Color(200, 200, 200));
        int selectedRow = orderTable.getSelectedRow();
        selectedOrder = orderHistory.get(selectedRow);
        nav.fromHistory1ToMoreInfo(CustomerOrderHistoryController.this);
    }
    
    /**
    * The method sends a GET request to the server to get the customer's order history.
    * Once obtained, it fills the view table with a row for each order in the history.
    * Each line includes only some information about the order such as: 
    * id, name of the selected provider, name of the rider who completed the order (if any), delivery / take-away time of the order.
    * * In case of a communication error with the server, messages are displayed to inform the customer.
    */
    private void fillOrderTable(){
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
                        row[2] = order.getRider() != null ? order.getRider().getName() : "";
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
    }
    
    /**
     * This method sends three calls to the server: the first requires the current order of the costumer to set the appropriate text to be displayed on the home button. 
     * If the current order is not present, a listener is added to the button that allows you to view the GUI relating to the choice of providers.
     * If, on the other hand, the current order is present at the moment of the construction of the button this could have been refused or completed at the moment of its pressure. 
     * So when this event occurs, a new call is made to get the current order again. 
     * If this is present, the interface that allows you to view information about it will be shown. 
     * Instead, if not present, a third call is made this time to retrieve the last status of the order. Which is notified to the user together with any refund for a rejected order. 
     * After that the GUI for the choice of providers will be shown.
     * Furthermore case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void addHomeButtonLogic(){
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
                    homeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            nav.fromHistory1ToProviderSelection(CustomerOrderHistoryController.this);
                        }

                    });

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
    }

    /**
     * The method sends a GET to the server to get the current order. If present, the GUI for its visualization will be shown.
     * Otherwise a new GET is performed. This time the status of the order is obtained (which has been completed or rejected), which is communicated to the costumer through a specific notification.
     * Finally the GUI for the choice of providers will be shown
     * Furthermore in case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void changeToHome() {
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(CustomerOrderHistoryController.this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    currentOrder = response.body();
                    nav.fromHistory1ToOrderViewer(CustomerOrderHistoryController.this);
                } else {
                    getOrderById();
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
    }
    
    /**
     *The method sends a GET to get the status of the last current order.
     *If this is completed or refused, the user is notified together with any refund for the order placed by him.
     *Finally the GUI for the provide interface will be displayed.
     *Furthermore in case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void getOrderById() {
        Call<OrderDto> getOrderById = apiService.getOrderDTO(CustomerOrderHistoryController.this.currentOrder.getId());
        getOrderById.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    OrderDto orderGhost = response.body();
                    if (orderGhost.getOrderState().equals(OrderState.REFUSED)) {
                        JOptionPane.showMessageDialog(historyView, "YOUR CURRENT ORDER HAVE BEEN REFUSED BUT YOU GOT: " + orderGhost.getPrice() + " MONEY BACK", "Order Refused", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(historyView, "YOUR CURRENT ORDER HAVE BEEN COMPLETED", "Order Completed", JOptionPane.INFORMATION_MESSAGE);
                    }
                    File file = new File("customer"+customerId+"/persistentOrder.txt");
                    if(file.delete()){
                        System.out.println("File cancellato con successo");
                    }
                    else
                        System.out.println("Problemi con la cancellazione del file");
                    nav.fromHistory1ToProviderSelection(CustomerOrderHistoryController.this);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(historyView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

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
    }
    
    
    /**
     * Remove the CustomerFrameHistory1 view from the visualization
     */
    public void disposeView() {
        historyView.dispose();
    }

    /**
     * Returns the logged customer's id
     * @return String that represent the logged customer's id 
     */
    public Long getCustomerId() {
        return customerId;
    }
    /**
     * Returns selected order by costumer through view
     * @return selected order by costumer
     */
    public OrderDto getSelectedOrder() {
        return selectedOrder;
    }

}
