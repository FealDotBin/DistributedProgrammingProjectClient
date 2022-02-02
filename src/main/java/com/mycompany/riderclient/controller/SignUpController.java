/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.riderclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.riderclient.api.ServiceApi;
import com.mycompany.riderclient.model.RiderEntity;
import com.mycompany.riderclient.navigator.Navigator;
import com.mycompany.riderclient.view.RiderSignUpView;
import com.sun.tools.javac.Main;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @author Amos
 */
public class SignUpController {
    
    private RiderSignUpView signUpView;
    
    private JTextFieldPlaceholder usernameField;
    private JTextFieldPlaceholder passwordField;
    private JTextFieldPlaceholder nameField;
    private JTextFieldPlaceholder surnameField;
    private JDateChooser birthDateChooser;
    private JTextFieldPlaceholder ibanField;
    private JTextFieldPlaceholder vehicleTypeField;
    private JTextFieldPlaceholder telephoneField;
    private JButton signUpButton;
    private JButton logInButton;    
    private RiderSignUpView signView;    
    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator navigator;

    
    
    public SignUpController(){
        signView = new RiderSignUpView();
        signView.setVisible(true);
        
        
        //Component getter
        usernameField = signView.getUsernameTextField();
        passwordField = signView.getPasswordTextField();
        nameField = signView.getNameTextField();
        surnameField = signView.getSurnameTextField();
        birthDateChooser = signView.getBirthDateChooser();
        ibanField = signView.getIbanTextField();
        telephoneField = signView.getTelephoneTextField();
        signUpButton = signView.getSignUpBtn();
        logInButton = signView.getLogInBtn();
        vehicleTypeField = signView.getVehicleTextField();
        //View navigator creation
        navigator = Navigator.getInstance();
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);
        
        
        signUpButton.addMouseListener(signUpAction);
        logInButton.addMouseListener(loginAction);
        
    }
    
          //Shows a pop up to inform the user that the informations that is typing are not correct
    private void fieldErrorPane(String errorMessage){
        JOptionPane.showMessageDialog(signUpView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }     

    
    
    
    private MouseAdapter signUpAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
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
                if (birthDate==null) {
                    fieldErrorPane("Birth date cannot be blank");
                    return;
                }
                
                
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

                String vehicle = vehicleTypeField.getText(true).trim();
                if (vehicle.isBlank()) {
                    fieldErrorPane("Vehicle cannot be blank");
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
                
                //Request to server for creating a new Customer
                
                RiderEntity rider = new RiderEntity(username, password, name, surname, formattedBirthDate , iban, telephoneNumber, vehicle);
                Call<RiderEntity> call2 = apiService.createRider(rider);

                call2.enqueue(new Callback<RiderEntity>() {

                    @Override
                    public void onResponse(Call<RiderEntity> call, Response<RiderEntity> response) {

                        if (response.isSuccessful()) { // status code tra 200-299
                           JOptionPane.showMessageDialog(signUpView, "NOW YOU ARE  A RIDER OF OUR SYSTEM", "Sign up success", JOptionPane.INFORMATION_MESSAGE);
                           
                           //If sign up procedure is correct change current view with log in one
                              navigator.fromSignUpToLogIn(SignUpController.this);
                
                
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
                    public void onFailure(Call<RiderEntity> call, Throwable t) {
                        // Log error here since request failed
                        JOptionPane.showMessageDialog(signUpView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

                    }

                });

            }
        
            
           
        
        
        
    };
            



    
 
    
      //Shows a pop up to inform the user that the informations that is typing are not correct
    
       private MouseAdapter loginAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
              navigator.fromSignUpToLogIn(SignUpController.this);
        }
           
    };
        
    public void disposeView(){
        signView.dispose();
    }
    
    
     public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SignUpController c = new SignUpController();
            }
        });
    }
    
               
               
}

