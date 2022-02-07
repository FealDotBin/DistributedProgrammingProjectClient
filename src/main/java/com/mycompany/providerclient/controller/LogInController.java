/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller;

import com.google.common.hash.Hashing;
import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.common.model.Credentials;
import com.mycompany.providerclient.navigator.Navigator;
import com.mycompany.providerclient.view.LogInView;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.mycompany.providerclient.api.ServiceApi;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 * This class represents LogInView GUI controller. 
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restful server when needed:
 * <ul>
 *   <li> To verify provider credentials </li>
 *   <li> To decide which controller to invoke after that provider
 *   successfully log in </li>
 * </ul>
 * @author aferr
 */
public class LogInController {
    
    private LogInView logInView;
    private JTextFieldPlaceholder usernameTextField;
    private JTextFieldPlaceholder passwordTextField;
    private JButton logInBtn;
    private JButton signUpBtn;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private Long providerId;
    private Navigator navigator;
    
    /**
     * Add listener to update button to log in a provider
     * Determine the next interface to show after a successful login communicating with the server
     * Add listener to sign up button to navigate to singUp view
     */
    public LogInController() {
        // initialize view
        logInView = new LogInView();
        logInView.setVisible(true);
        
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
        
        // attach listeners
        attachListenerToLogInBtn();
        attachListenerToSignUpBtn();
    }

    /**
     * Getter for providerId
     * @return the provider's id
     */
    public Long getProviderId(){
        return providerId;
    }
    
    /**
     * Destroy the logInView instance and all its components.
     */
    public void disposeView(){
        logInView.dispose();
    }
    
    /**
     * Send a POST request to the server to authenticate the provider sending
     * the provided credentials.
     * If the response is successful, the provider is logged in and he is
     * redirected to the home page;
     * otherwise, an error message is shown to the user.
     */
    private void loginCall(Credentials credentials){
        Call<Long> loginCall = serviceApi.login(credentials);
        loginCall.enqueue(new Callback<Long>(){

            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    providerId = response.body();
                    navigator.fromLogInToHome(LogInController.this);
                }
                else{ // in caso di errori
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
                            "Something went wrong...",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    /**
     * Attach an ActionListener to logInButton in order to authenticate
     * the provider, when the button is pressed
     */
    private void attachListenerToLogInBtn(){
        logInBtn.addActionListener((event) -> {
            String username = usernameTextField.getText(true).trim();
            String password = passwordTextField.getText(true).trim();
            
            if(username.isBlank()){
                JOptionPane.showMessageDialog(logInView,
                                "Username cannot be blank.",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(password.isBlank()){
                JOptionPane.showMessageDialog(logInView,
                                "Password cannot be blank.",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                return;
            }
            password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            
            Credentials credentials = new Credentials(username, password);
            
            // send call to API
            loginCall(credentials);
        });
    }
    
    /**
     * Attach an ActionListener to signUpButton in order to switch to
     * the "sign up" window, when the button is pressed.
     */
    private void attachListenerToSignUpBtn(){
        signUpBtn.addActionListener((event) -> {
            navigator.fromLogInToSignUp(LogInController.this);
        });
    }
    
    /**
     * Create a new LogInController
     * @param args 
     */
    public static void main(String args[]) {
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               new LogInController();
           }
       });
    }
}