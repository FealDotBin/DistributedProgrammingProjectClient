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
 *
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

    public Long getProviderId(){
        return providerId;
    }
    
    public void disposeView(){
        logInView.dispose();
    }
    
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
    
    private void attachListenerToSignUpBtn(){
        signUpBtn.addActionListener((event) -> {
            navigator.fromLogInToSignUp(LogInController.this);
        });
    }
    
    public static void main(String args[]) {
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               new LogInController();
           }
       });
    }
}