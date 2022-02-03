/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.google.common.hash.Hashing;
import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.common.model.Credentials;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.customerLogIn;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.providerclient.controller.LogInController;
import com.mycompany.providerclient.view.LogInView;
import com.sun.tools.javac.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
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
    private Long customerId;

    private Navigator navigator;

    public CustomerLogInController() {
        // initialize view
        logInView = new LogInView();
        logInView.pack();
        logInView.setLocationRelativeTo(null);
        logInView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
                return;
            }
            if (password.isBlank()) {
                fieldErrorPane("Password cannot be blank");
                return;
            }
            password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            Credentials credentials = new Credentials(username, password);

            // send call to API
            Call<Long> loginCall = serviceApi.login(credentials);
            loginCall.enqueue(new Callback<Long>() {

                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {

                    if (response.isSuccessful()) { // status code tra 200-299
                        customerId = response.body();

                        Call<OrderDto> currentOrderCall = serviceApi.getCurrentOrderDTO(CustomerLogInController.this.customerId);
                        currentOrderCall.enqueue(new Callback<OrderDto>() {
                            @Override
                            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                                OrderDto newCurrentOrder = response.body();
                                if (response.isSuccessful()) {
                                    ObjectOutputStream file = null;
                                    File dir = null;
                                    try {
                                        String path = "customer"+customerId;
                                        dir = new File(path);
                                        if(dir.mkdir())
                                            System.out.println("Nuova cartella creata");
                                        else
                                            System.out.println("Impossibile creare la cartella");
                                        file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path+"/persistentOrder.txt")));
                                        file.writeObject(newCurrentOrder);
                                    } catch (IOException ex) {
                                        Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                                    } finally {
                                        try {
                                            if(file!=null)
                                            file.close();
                                        } catch (IOException ex) {
                                            Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    navigator.fromLogInToOrderViewer(CustomerLogInController.this);
                                } else {

                                    ObjectInputStream file = null;
                                    try {
                                        file = new ObjectInputStream(new BufferedInputStream(new FileInputStream("customer"+customerId+"/persistentOrder.txt")));
                                        newCurrentOrder = (OrderDto) file.readObject();
                                        getOrderById(newCurrentOrder.getId());

                                    } catch (FileNotFoundException ex) {
                                        navigator.fromLogInToProviderSelection(CustomerLogInController.this);
                                    } catch (IOException ex) {
                                        Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ClassNotFoundException ex) {
                                        Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                                    } finally {
                                        try {
                                            if(file!=null)
                                            file.close();
                                        } catch (IOException ex) {
                                            Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }

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
        return customerId;
    }

    public void disposeView() {
        logInView.dispose();
    }

    private void getOrderById(Long orderId) {
        Call<OrderDto> getOrderById = serviceApi.getOrderDTO(orderId);
        getOrderById.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    OrderDto orderGhost = response.body();
                    if (orderGhost.getOrderState().equals(OrderState.REFUSED)) {
                        JOptionPane.showMessageDialog(logInView, "YOUR CURRENT ORDER HAVE BEEN REFUSED BUT YOU GOT: " + orderGhost.getPrice() + " MONEY BACK", "Order Refused", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(logInView, "YOUR CURRENT ORDER HAVE BEEN COMPLETED", "Order Completed", JOptionPane.INFORMATION_MESSAGE);
                    }
                    File file = new File("customer"+customerId+"/persistentOrder.txt");
                    if(file.delete()){
                        System.out.println("File cancellato con successo");
                    }
                    else
                        System.out.println("Problemi con la cancellazione del file");
                    navigator.fromLogInToProviderSelection(CustomerLogInController.this);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(logInView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(logInView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
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
