/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.google.common.hash.Hashing;
import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.model.CustomerEntity;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.customerSignUp;
import com.sun.tools.javac.Main;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 * This class represents CustomerSingUp GUI controller. 
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restfull server when needed:
 * - To create a new customer 
 */
public class CustomerSignUpController {

    private JTextFieldPlaceholder usernameField;
    private JTextFieldPlaceholder passwordField;
    private JTextFieldPlaceholder nameField;
    private JTextFieldPlaceholder surnameField;
    private JDateChooser birthDateChooser;
    private JTextFieldPlaceholder ibanField;
    private JTextFieldPlaceholder addressField;
    private JTextFieldPlaceholder telephoneField;
    private JButton signUpButton;
    private JButton logInButton;
    private customerSignUp signUpView;
    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator nav;

    /**
     * 
     * Send a customer creation request to the server with information taken from view
     * Add listener to log in button to navigate from this to log in controller 
     */
    public CustomerSignUpController() {
        //View creation
        signUpView = new customerSignUp();
        //Component getter
        usernameField = signUpView.getUsernameTextField();
        passwordField = signUpView.getPasswordTextField();
        nameField = signUpView.getNameTextField();
        surnameField = signUpView.getSurnameTextField();
        birthDateChooser = signUpView.getBirthDateChooser();
        ibanField = signUpView.getIbanTextField();
        addressField = signUpView.getAddressTextField();
        telephoneField = signUpView.getTelephoneTextField();
        signUpButton = signUpView.getSignInBtn();
        logInButton = signUpView.getLogInBtn();
        //View navigator creation
        nav = Navigator.getInstance();
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);

        //Action perfomed when sign up button is pressed: read all field from view, validate them, create a new CustomerEnity,
        //send it to server and wait for response.
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                addSignUpButtonLogic();
                
                //Request to server for creating a new Customer
                
                

            }

        });
        //Action perfomerd when log in button is pressed:
        //Destroy this view and visualize log in view
        logInButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromSignUpToLogIn(CustomerSignUpController.this);
            }
        
    });
        //Start view visualization
        signUpView.setVisible(true);
    }
    /**
     *The method reads and validates the inputs entered by the user via the GUI.
     *After that it sends a POST request to the server to create a new costumer with the credentials entered by the user.
     * In case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void addSignUpButtonLogic(){
        String username = usernameField.getText(true).trim();
            if (username.isBlank()) {
                fieldErrorPane("Username cannot be blank");
                return;
            }

            String password = passwordField.getText(true).trim();
            if (password.isBlank()) {
                fieldErrorPane("Password cannot be blank");
                return;
            }
            password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

            String name = nameField.getText(true).trim();
            if (name.isBlank()) {
                fieldErrorPane("Name cannot be blank");
                return;
            }

            String surname = surnameField.getText(true).trim();
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

            String iban = ibanField.getText(true).trim();
            if (iban.isBlank()) {
                fieldErrorPane("IBAN cannot be blank");
                return;
            }

            String address = addressField.getText(true).trim();
            if (address.isBlank()) {
                fieldErrorPane("Address cannot be blank");
                return;
            }

            String telephoneNumber = telephoneField.getText(true).trim();
            if (telephoneNumber.isBlank() || (!telephoneNumber.matches("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"))) {
                fieldErrorPane("TelephoneNumber cannot be blank and has to be of the folliwing types\n"
                        + "368 734 8201\n"
                        + "368-734-8201\n"
                        + "+39 368 734 8201\n"
                        + "+39-368-734-8201\n");
                return;
            }
            CustomerEntity customer = new CustomerEntity(username, password, name, surname, formattedBirthDate , iban, telephoneNumber, address);
            requestForCustomerCreation(customer);
    }
    
    /**
     * The methos sends a POST request to the server to create a new costumer with the information contained in the input parameter
     * In case of a communication error with the server, messages are displayed to inform the customer.
     * @param customer object that represents the information about the new customer that wants to sing up
     */
    private void requestForCustomerCreation(CustomerEntity customer){
        Call<CustomerEntity> call2 = apiService.createCustomer(customer);

        call2.enqueue(new Callback<CustomerEntity>() {

            @Override
            public void onResponse(Call<CustomerEntity> call, Response<CustomerEntity> response) {

                if (response.isSuccessful()) { // status code tra 200-299
                   JOptionPane.showMessageDialog(signUpView, "NOW YOU ARE  A COSTUMER OF OUR SYSTEM", "Sign up success", JOptionPane.INFORMATION_MESSAGE);

                   //If sign up procedure is correct change current view with log in one
                   nav.fromSignUpToLogIn(CustomerSignUpController.this);
                } else { // if server error occurs
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(signUpView,jObjError.get("message") , "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerEntity> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(signUpView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);

            }

        });
    }
    
    /**
     * Method used to communicate to the customer an error in filling in the fields
     * @param errorMessage error message that is displayed to the customer
     */
    private void fieldErrorPane(String errorMessage){
        JOptionPane.showMessageDialog(signUpView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    
     /**
     * Remove customerSignUp  view from the visualization
     */
    public void disposeView(){
        signUpView.dispose();
    }
    
}
