/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.common.model.Credentials;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.customerLogIn;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.providerclient.controller.LogInController;
import com.mycompany.providerclient.view.LogInView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 */
public class CustomerLogInController {

    private LogInView logInView;
    private JTextFieldPlaceholder usernameTextField;
    private JTextFieldPlaceholder passwordTextField;
    private JButton logInBtn;
    private JButton signUpBtn;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private Long costumerId;
    private OrderDto currentOrder;
    private Navigator navigator;

    public CustomerLogInController() {
        // initialize view
        logInView = new LogInView();

        // get components from view
        usernameTextField = logInView.getUsernameTextField();
        passwordTextField = logInView.getPasswordTextField();
        logInBtn = logInView.getLogInBtn();
        signUpBtn = logInView.getSignUpBtn();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);

        // get navigator
        navigator = Navigator.getInstance();

        //Action perfomed when log in button is pressed: read all field from view, validate them,
        //send a login request to server and wait for response. After that checks if the costumer have
        //already an order in a state that is different from completed or refused. This is information is
        //obtain through another request to server. At the end decide what view display.
        logInBtn.addActionListener((event) -> {
            String username = usernameTextField.getText(true).trim();
            String password = passwordTextField.getText(true).trim();

            if (username.isBlank()) {
                fieldErrorPane("Username cannot be blank");
            }
            if (password.isBlank()) {
                fieldErrorPane("Password cannot be blank");
            }
            Credentials credentials = new Credentials(username, password);

            // send call to API
            Call<Long> loginCall = serviceApi.login(credentials);
            loginCall.enqueue(new Callback<Long>() {

                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {

                    if (response.isSuccessful()) { // status code tra 200-299
                        costumerId = response.body();
                        

                        Call<OrderDto> currentOrderCall = serviceApi.getCurrentOrderDTO(CustomerLogInController.this.costumerId);
                        currentOrderCall.enqueue(new Callback<OrderDto>() {
                            @Override
                            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                                OrderDto newCurrentOrder = response.body();
                                if (response.isSuccessful()){
                                    currentOrder = newCurrentOrder;
                                    System.out.println(currentOrder);
                                    navigator.fromLogInToOrderViewer(CustomerLogInController.this);
                                }
                                else
                                    navigator.fromLogInToProviderSelection(CustomerLogInController.this);
                            }

                            @Override
                            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                                JOptionPane.showMessageDialog(logInView,
                                        "Contact you system administrator",
                                        "CRITICAL ERROR",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        });

                    } else { // in caso di errori
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            JOptionPane.showMessageDialog(logInView,
                                    jObjError.get("message"),
                                    "ERROR",
                                    JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    // Log error here since request failed
                    JOptionPane.showMessageDialog(logInView,
                            "Contact you system administrator",
                            "CRITICAL ERROR",
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        });

        // attach listener to signUpBtn
        signUpBtn.addActionListener((event) -> {
            navigator.fromLogInToSingUp(CustomerLogInController.this);
        });

        logInView.setVisible(true);
    }

    private void fieldErrorPane(String errorMessage) {
        JOptionPane.showMessageDialog(logInView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }

    public Long getCustomerId() {
        return costumerId;
    }
    
    public OrderDto getCurrentOrder() {
        return currentOrder;
    }

    public void disposeView() {
        logInView.dispose();
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CustomerLogInController c = new CustomerLogInController();
            }
        });
    }

}
