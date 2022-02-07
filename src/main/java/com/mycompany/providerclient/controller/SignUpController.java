/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller;

import com.google.common.hash.Hashing;
import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.providerclient.api.ServiceApi;
import com.mycompany.providerclient.model.ProviderEntity;
import com.mycompany.providerclient.navigator.Navigator;
import com.mycompany.providerclient.view.SignUpView;
import com.sun.tools.javac.Main;
import com.toedter.calendar.JDateChooser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents the controller associated to SignUpView class.
 * It is responsible of taking the user's input to create a new Provider account.
 * Whenever it's necessary to send or retrieve datas to/from server, the
 * API endpoint (ServiceApi class) is being used.
 * @author aferr
 */
public class SignUpController {
    
    private SignUpView signUpView;
    private JTextFieldPlaceholder usernameTextField;
    private JTextFieldPlaceholder passwordTextField;
    private JTextFieldPlaceholder nameTextField;
    private JTextFieldPlaceholder surnameTextField;
    private JDateChooser birthDateChooser;
    private JTextFieldPlaceholder ibanTextField;
    private JTextFieldPlaceholder addressTextField;
    private JTextFieldPlaceholder telephoneNumberTextField;
    private JTextFieldPlaceholder providerNameTextField;
    private JTextFieldPlaceholder cuisineTextField;
    private JCheckBox deliveringCheckBox;
    private JCheckBox takeAwayCheckBox;
    private JCheckBox ownRiderCheckBox;
    private JButton signUpBtn;
    private JButton logInBtn;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private Navigator navigator;
    
    /**
     * Initialize both the view and the controller by attaching all necessary
     * listeners to the buttons.
     */
    public SignUpController(){
        // initialize view
        signUpView = new SignUpView();
        signUpView.setVisible(true);
        
        // get components from view
        usernameTextField = signUpView.getUsernameTextField();
        passwordTextField = signUpView.getPasswordTextField();
        nameTextField = signUpView.getNameTextField();
        surnameTextField = signUpView.getSurnameTextField();
        birthDateChooser = signUpView.getBirthDateChooser();
        ibanTextField = signUpView.getIbanTextField();
        addressTextField = signUpView.getAddressTextField();
        telephoneNumberTextField = signUpView.getTelephoneTextField();
        providerNameTextField = signUpView.getProviderNameTextField();
        cuisineTextField = signUpView.getCuisineTextField();
        deliveringCheckBox = signUpView.getDeliveringCheckBox();
        takeAwayCheckBox = signUpView.getTakeAwayCheckBox();
        ownRiderCheckBox = signUpView.getOwnRiderCheckBox();
        signUpBtn = signUpView.getSignUpBtn();
        logInBtn = signUpView.getLogInBtn();
        
        // get navigator
        navigator = Navigator.getInstance();
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // attach listeners
        attachListenerToSignUpBtn();
        attachListenerToLogInBtn();
    }
    
    /**
     * Destroy the homeView instance and all its components.
     */
    public void disposeView(){
        signUpView.dispose();
    }
    
    /**
     * Send a POST request to the server to create a new provider.
     * If the response is successful, the provider has been created successfully
     * and the user will be redirected to the "login" window;
     * otherwise, an error message is shown to the user.
     * @param provider The new provider who has to be created
     */
    private void createNewProviderCall(ProviderEntity provider){
        Call<ProviderEntity> createNewProviderCall = serviceApi.createNewProvider(provider);
        createNewProviderCall.enqueue(new Callback<ProviderEntity>(){
            @Override
            public void onResponse(Call<ProviderEntity> call, Response<ProviderEntity> response) {

                if (response.isSuccessful()) { // status code tra 200-299
                   JOptionPane.showMessageDialog(signUpView, 
                           "NOW YOU ARE A PROVIDER OF OUR SYSTEM", 
                           "Sign up success", 
                           JOptionPane.INFORMATION_MESSAGE);

                   //If sign up procedure is correct change current view with log in one
                   navigator.fromSignUpToLogIn(SignUpController.this);
                } else { // if server error occurs
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(signUpView,
                                jObjError.get("message"), 
                                "Server error", 
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProviderEntity> call, Throwable t) {
                // Log error here since request failed
                System.out.println("failed");
            }
        });
    }
    
    /**
     * Attach an ActionListener to signUpButton in order to create a new Provider
     * using the infos provided by the user through the input fields, 
     * whenever the button is pressed.
     */
    private void attachListenerToSignUpBtn(){
        signUpBtn.addActionListener((event) -> {
            String username = usernameTextField.getText(true).trim();
            if (username.isBlank()) {
                    fieldErrorPane("Username cannot be blank");
                    return;
            }
            
            String password = passwordTextField.getText(true).trim();
            if (password.isBlank()) {
                    fieldErrorPane("Password cannot be blank");
                    return;
            }
            password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            
            String name = nameTextField.getText(true).trim();
            if (name.isBlank()) {
                    fieldErrorPane("Name cannot be blank");
                    return;
            }
            
            String surname = surnameTextField.getText(true).trim();
            if (surname.isBlank()) {
                    fieldErrorPane("Surname cannot be blank");
                    return;
            }
            
            Date birthDate = birthDateChooser.getDate();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedBirthDate = df.format(birthDate);
            if (formattedBirthDate.isBlank()) {
                fieldErrorPane("Birth date cannot be blank");
                return;
            }
            
            String iban = ibanTextField.getText(true).trim();
            if (iban.isBlank()) {
                    fieldErrorPane("IBAN cannot be blank");
                    return;
            }
            
            String address = addressTextField.getText(true).trim();
            if (address.isBlank()) {
                    fieldErrorPane("Address cannot be blank");
                    return;
            }
            
            String telephoneNumber = telephoneNumberTextField.getText(true).trim();
            if (telephoneNumber.isBlank() || (!telephoneNumber.matches("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"))) {
                    fieldErrorPane("TelephoneNumber cannot be blank and has to be of the folliwing types\n"
                            + "368 734 8201\n"
                            + "368-734-8201\n"
                            + "+39 368 734 8201\n"
                            + "+39-368-734-8201\n");
                    return;
            }
            
            String providerName = providerNameTextField.getText(true).trim();
            if (providerName.isBlank()) {
                    fieldErrorPane("Provider Name cannot be blank");
                    return;
            }
            
            String cuisine = cuisineTextField.getText(true).trim();
            if (cuisine.isBlank()) {
                    fieldErrorPane("Cuisine cannot be blank");
                    return;
            }
            
            Boolean doDelivering = deliveringCheckBox.isSelected();
            Boolean doTakeAway = takeAwayCheckBox.isSelected();
            Boolean hasOwnRiders = ownRiderCheckBox.isSelected();
            
            // if all checkboxes are unchecked pop up an error dialog,
            // otherwise send data to server
            if(!doTakeAway && !hasOwnRiders && !doDelivering){
                    fieldErrorPane("You must check at least one of "
                            + "\"take away\", \"own riders\" or \"delivering\"");
            }
            else{
                ProviderEntity provider = new ProviderEntity(
                        username, password, 
                        name, surname, formattedBirthDate, iban, telephoneNumber, 
                        providerName, cuisine, address, 
                        doDelivering, doTakeAway, hasOwnRiders);
                createNewProviderCall(provider);
            }
        });
    }
    
    /**
     * Attach an ActionListener to logInButton in order to switch to
     * the "log in" window, when the button is pressed.
     */
    private void attachListenerToLogInBtn(){
        logInBtn.addActionListener((event) -> {
            navigator.fromSignUpToLogIn(SignUpController.this);
        });
    }
    
    /**
     * Shows a pop up to inform the user that the informations that is typing are not correct
     */
    private void fieldErrorPane(String errorMessage){
        JOptionPane.showMessageDialog(signUpView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
}
