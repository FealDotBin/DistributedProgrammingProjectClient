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
 * This class represents LogInView GUI controller. 
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restfull server when needed:
 * -To verify customer credentials 
 * -To decide which controller to invoke after that customer
 * successfully log in.
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

    /**
     * 
     * Add listener to update button to log in a customer
     * Determine the next interface to show after a successful login communicating with the server
     * Add listener to sign up button to navigate to singUp view
     */
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
            logInProcedure();
        });

        // when sign in button is pressed display sign in view
        signUpBtn.addActionListener((event) -> {
            navigator.fromLogInToSingUp(CustomerLogInController.this);
        });

        logInView.setVisible(true);
    }
    
    
    /**
     * Method used to communicate to the customer an error in filling in the fields
     * @param errorMessage error message that is displayed to the customer
     */
    private void fieldErrorPane(String errorMessage) {
        JOptionPane.showMessageDialog(logInView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Return the logged customer's id
     * @return String that represent the logged customer's id 
     */
    public Long getCustomerId() {
        return customerId;
    }
    /**
     * Remove the CustomerLogIn view from the visualization
     */
    public void disposeView() {
        logInView.dispose();
    }
    
    /**
     * This method sends a GET to the server obtaining the order that the id coincides with the one in input.
     * If the status of the order is completed or rejected, a notification is shown to the costumer along with any refunds.
     * After that the GUI for choosing the providers is shown.
     * In case of a communication error with the server, messages are displayed to inform the customer.
     * @param orderId represent order'id that we want to obtain from the server
     */
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
                    File file = new File("customer" + customerId + "/persistentOrder.txt");
                    if (file.delete()) {
                        System.out.println("File cancellato con successo");
                    } else {
                        System.out.println("Problemi con la cancellazione del file");
                    }
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

    /**
     * The method first validates the username and password fields filled in by the user.
     * After that it hashes the password and sends a POST to the server to log it with the credentials it entered.
     * If no match is found with the credentials stored on the server, the method terminates.
     * While if this is present the user is logged in.
     * To decide which GUI to show next, the method sends a GET to the server to obtain the current order of the costumer when he logged out.
     * If the current order is present, the GUI for its visualization is shown. 
     * While if it is not present it may have been refused or completed when the customer was not online.
     * In this case, a GET is sent to the server to obtain the last status of the current order 
     * (the id necessary to make this request is read from a file, which is created at the time the order is created) 
     * and notify the costumer together with any refund. 
     * After that the GUI for selecting the providers is shown.
     * In case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void logInProcedure() {
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
                                    String path = "customer" + customerId;
                                    dir = new File(path);
                                    if (dir.mkdir()) {
                                        System.out.println("Nuova cartella creata");
                                    } else {
                                        System.out.println("Impossibile creare la cartella");
                                    }
                                    file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path + "/persistentOrder.txt")));
                                    file.writeObject(newCurrentOrder);
                                } catch (IOException ex) {
                                    Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                                } finally {
                                    try {
                                        if (file != null) {
                                            file.close();
                                        }
                                    } catch (IOException ex) {
                                        Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                navigator.fromLogInToOrderViewer(CustomerLogInController.this);
                            } else {

                                ObjectInputStream file = null;
                                try {
                                    file = new ObjectInputStream(new BufferedInputStream(new FileInputStream("customer" + customerId + "/persistentOrder.txt")));
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
                                        if (file != null) {
                                            file.close();
                                        }
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
    }
    public static void main(String args[]) {
        


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerLogInController();
            }
        });
    }

}
