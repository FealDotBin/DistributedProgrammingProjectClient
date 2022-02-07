/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.riderclient.controller;

import com.google.common.hash.Hashing;
import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.common.model.Credentials;



import com.mycompany.riderclient.api.ServiceApi;
import com.mycompany.riderclient.navigator.Navigator;
import com.mycompany.riderclient.view.RiderLoginView;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class represents the controller of the view RiderLoginView. 
 * Allows to control and manage all the GUI components.
 * It also manages the logic needed to call the APIs asynchronously, based on the user request on the GUI.
 * 
 * The controller allows you to perform the following operations:
 * - login with username and password
 * - if the login is successfull, move on to the next view (RiderHomeView) 
 * - open the sign-up form
 * @author Amos
 */
public class RiderLoginController {
    
    private RiderLoginView loginView;
    private JTextFieldPlaceholder usernameTextField;
    private JTextFieldPlaceholder  passwordTextField;
    private JButton logInBtn;
    private JButton signUpBtn;
    
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private Long riderId;
    private Navigator navigator;
    
    /**
     * * Initialize:
      * - the view and its components;
      * - the retrofitBuilder and the serviceApi in order to invoke the API calls.
      * - the Navigator to switch between views
      * - attach listeners to buttons in order to manage events
      * 
     */
    public RiderLoginController(){
        
        // initialize view
        loginView = new RiderLoginView();
        loginView.setVisible(true);   
        
        // inizialize components
        usernameTextField = loginView.getUsernameTextField();
        passwordTextField = loginView.getPasswordTextField();
        logInBtn = loginView.getLogInBtn();
        signUpBtn = loginView.getSignUpBtn();
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // initialize Navigator to switch between views
        navigator = Navigator.getInstance();
        
        // add listeners to button
        logInBtn.addMouseListener(loginInAction);
        signUpBtn.addMouseListener(signUpAction);
        
        
    }
    
    /**
     * It's a mouse listener that allows you to login on the app, using 
     * credentials. If the login is successfull, it calls a navigator method in 
     * order to pass the control to the next view 
     * controlled by RiderHomeController
     * 
     * The method calls an asynchronous API to login. 
     */
    private MouseAdapter loginInAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
            System.out.println(usernameTextField.getText()+passwordTextField.getText());
            
            String username = usernameTextField.getText(true).trim();
            String password = passwordTextField.getText(true).trim();
            
            if(username.isBlank()){
                JOptionPane.showMessageDialog(loginView,
                                "Username cannot be blank.",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(password.isBlank()){
                JOptionPane.showMessageDialog(loginView,
                                "Password cannot be blank.",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            //API CALL
            password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            Credentials credentials = new Credentials(username, password);
            Call<Long> loginCall = serviceApi.login(credentials);
            
            loginCall.enqueue(new Callback<Long>(){
                
                @Override
                public void onResponse(Call<Long> call, Response<Long> response) {

                    if(response.isSuccessful()){ // status code tra 200-299
                        riderId = response.body();
                        System.out.println("Login effettuato correttamente, id: "+riderId);
                       navigator.fromLogInToHome(RiderLoginController.this);
                     
                    }
                    else{ // in caso di errori
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            JOptionPane.showMessageDialog(loginView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        } catch (IOException ex) {
                            Logger.getLogger(RiderLoginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Long> call, Throwable t) {
                    // Log error here since request failed
                      JOptionPane.showMessageDialog(loginView,
                                "Something went wrong...",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                }
            });            
        }
    };
    
    
    /**
     * It's an action listener that calls a navigator method in order to to pass 
     * the control to the next view, controlled by SignUpController
     */
    private MouseAdapter signUpAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
            navigator.fromLogInToSignUp(RiderLoginController.this);
        }
            
         
    };
    
    /**
     * Dispose the controlled view
     */
    public void disposeView(){
        loginView.dispose();
    }

    public Long getRiderId() {
        return riderId;
    }
    
    
}
