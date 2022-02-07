/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.model.CustomerEntity;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.UpdateBalance;
import com.mycompany.customerclient.view.customerUpdate;
import com.sun.tools.javac.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 * This class represents UpdateBalance GUI controller.
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restfull server when needed:
 *  -To retrieve the customer current balance
 *  -To set text on home button
 *  -To decide which controller to invoke after the home button has been pressed
 */
public class CustomerBalanceController {

    private UpdateBalance balanceView;

    private JTextField ibanField;
    private JTextField currentBalanceField;
    private JTextField incrementField;

    private JButton accountButton;
    private JButton homeButton;
    private JButton confirmButton;
    private JButton logOutButton;
    private JButton historyButton;

    private Long customerId;
    private Double currentBalance;
    private OrderDto currentOrder;

    private Navigator nav;

    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;

    /**
     * Initialize view's obtaing customer balance information from server.
     * Add listener to update button to send an update balance request to the server
     * Add listeners to other buttons to navigate from this controller to the next
     * @param customerId represents the logged customer's id
     * 
     */
    public CustomerBalanceController(Long customerId) {
        this.customerId = customerId;

        //View creation
        balanceView = new UpdateBalance();
        //Component getter
        ibanField = balanceView.getIbanTextField();
        incrementField = balanceView.getIncrementTextField();
        currentBalanceField = balanceView.getCurrentBalanceTextField();
        confirmButton = balanceView.getConfirmBtn();
        homeButton = balanceView.getHomeBtn();
        logOutButton = balanceView.getLogOutBtn();
        accountButton = balanceView.getAccountBtn();
        historyButton = balanceView.getHistoryBtn();

        //View navigator creation
        nav = Navigator.getInstance();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);

        //Fill fields with customer actual information saved on server
        fillFields();
        
        //When logout button is pressed try to update customer's balance
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBalance();
            }

        });

        // When logout button is pressed display logIn view
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromBalancetoLogOut(CustomerBalanceController.this);
            }

        });
        // When logout account button is pressed display customer update view
        accountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromBalancetoUpdate(CustomerBalanceController.this);
            }

        });

        // When logout history button is pressed display customer order history view
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromBalancetoHistory(CustomerBalanceController.this);
            }

        });

        //Set home button text and his action listener based on the presence of current order
        addHomeButtonLogic();

        balanceView.setVisible(true);

    }
    
    /**
     * Method used to communicate to the customer an error in filling in the fields
     * @param errorMessage error message that is displayed to the customer
     */
    private void fieldErrorPane(String errorMessage) {
        JOptionPane.showMessageDialog(balanceView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    /**
     * Remove the updateBalance view from the visualization
     */
    public void disposeView() {
        balanceView.dispose();
    }
    
    /**
     * Return the logged customer's id
     * @return String that represent the logged customer's id 
     */
    public Long getCustomerId() {
        return customerId;
    }
    
    /**
     * The method send a GET request to the server to fill the interface fields. 
     * In particular, the current account and the iban of the logged in customer are obtained. 
     * In case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void fillFields() {
        Call<CustomerEntity> call = apiService.getCustomer(this.customerId);
        call.enqueue(new Callback<CustomerEntity>() {
            @Override
            public void onResponse(Call<CustomerEntity> call, Response<CustomerEntity> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                    CustomerEntity customer = response.body();
                    currentBalance = customer.getBalance();
                    currentBalanceField.setText(currentBalance.toString());
                    ibanField.setText(customer.getIban());
                } else { // if server error occurs
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(balanceView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerEntity> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(balanceView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);

            }
        });
    }
    /**
     * The method send a PUT request to the server to update the account of the logged-in costumer 
     * with the increment entered by the interface's appropriate field. 
     * If this increment is less than zero, the method ends by communicating the error to the customer. 
     * Furthermore case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void updateBalance(){
        Double increment = Double.parseDouble(incrementField.getText());
                if (increment < 0) {
                    fieldErrorPane("Increment balance cannot be negative");
                    return;
                }
                Call<Void> updateBalanceCall = apiService.increaseBalance(customerId, increment);
                updateBalanceCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) { // status code tra 200-299

                            Double newCurrentBalance = increment + currentBalance;
                            JOptionPane.showMessageDialog(balanceView, "Now your balance is: " + newCurrentBalance, "Update balance success", JOptionPane.INFORMATION_MESSAGE);

                            currentBalanceField.setText(newCurrentBalance.toString());
                            incrementField.setText("");
                        } else { // if server error occurs
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                JOptionPane.showMessageDialog(balanceView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                            } catch (IOException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Log error here since request failed
                        JOptionPane.showMessageDialog(balanceView,
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
                            nav.fromBalancetoProviderSelection(CustomerBalanceController.this);
                        }

                    });
                }
            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(balanceView,
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
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(CustomerBalanceController.this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    currentOrder = response.body();
                    nav.fromBalancetoOrderViewer(CustomerBalanceController.this);
                } else {
                    getOrderById();
                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(balanceView,
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
        Call<OrderDto> getOrderById = apiService.getOrderDTO(CustomerBalanceController.this.currentOrder.getId());
        getOrderById.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    OrderDto orderGhost = response.body();
                    if (orderGhost.getOrderState().equals(OrderState.REFUSED)) {
                        JOptionPane.showMessageDialog(balanceView, "YOUR CURRENT ORDER HAVE BEEN REFUSED BUT YOU GOT: " + orderGhost.getPrice() + " MONEY BACK", "Order Refused", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(balanceView, "YOUR CURRENT ORDER HAVE BEEN COMPLETED", "Order Completed", JOptionPane.INFORMATION_MESSAGE);
                    }
                    File file = new File("customer" + customerId + "/persistentOrder.txt");
                    if (file.delete()) {
                        System.out.println("File cancellato con successo");
                    } else {
                        System.out.println("Problemi con la cancellazione del file");
                    }
                    nav.fromBalancetoProviderSelection(CustomerBalanceController.this);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(balanceView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(balanceView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
    }

}
