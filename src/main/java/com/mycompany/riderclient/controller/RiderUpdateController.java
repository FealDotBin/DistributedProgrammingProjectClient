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
import com.mycompany.riderclient.view.RiderUpdateView;
import com.sun.tools.javac.Main;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 *
 * @author Amos
 */
public class RiderUpdateController {
    
    
    private static RiderUpdateController instance ;
    private RiderUpdateView updateView;
    
    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField nameField;
    private JTextField surnameField;
    private JDateChooser birthDateChooser;
    private JTextField ibanField;
    private JTextField vehicleTypeField;
    private JTextField telephoneField;
    private JButton updateBtn;    
   
    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator navigator;
    
    private Long riderId;
//
//     public synchronized static RiderUpdateController getInstance(Long riderId) {
//            if (updateView == null) {
//
//                instance = new RiderUpdateController(riderId);
//   
//            }
//            
//            return instance;
//        }
    
    
    public RiderUpdateController(Long riderId){
        
      
        
        this.riderId = riderId;
        updateView = new RiderUpdateView();
        updateView.setVisible(true);
        
        //Component getter
        usernameField = updateView.getUsernameTextField();
        passwordField = updateView.getPasswordTextField();
        nameField = updateView.getNameTextField();
        surnameField = updateView.getSurnameTextField();
        birthDateChooser = updateView.getBirthDateChooser();
        ibanField = updateView.getIbanTextField();
        telephoneField = updateView.getTelephoneTextField();
        updateBtn = updateView.getUpdateBtn();
        vehicleTypeField = updateView.getVehicleTextField();
        //View navigator creation
        navigator = Navigator.getInstance();
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);
        
        
        updateBtn.addMouseListener(signUpAction);
        
        fillField();
        
    }
    
    
    private void fillField(){
        
                //Fill fields with customer actual information saved on server
        Call<RiderEntity> call = apiService.getMyInfo(this.riderId);
        call.enqueue(new Callback<RiderEntity>() {
            @Override
            public void onResponse(Call<RiderEntity> call, Response<RiderEntity> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                    RiderEntity rider = response.body();
                    usernameField.setText(rider.getUsername());
                    nameField.setText(rider.getName());
                    surnameField.setText(rider.getSurname());
                    vehicleTypeField.setText(rider.getVehicleType());
                    telephoneField.setText(rider.getTelephoneNumber());
                    ibanField.setText(rider.getIban());
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthDate = null;
                    try {
                        birthDate = formatter.parse(rider.getBirthDate());
                    } catch (ParseException ex) {
                        Logger.getLogger(RiderUpdateController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    birthDateChooser.setDate(birthDate);
                } else { // if server error occurs
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(updateView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<RiderEntity> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(updateView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

            }

        });
        
        
        
    }
    
          //Shows a pop up to inform the user that the informations that is typing are not correct
    private void fieldErrorPane(String errorMessage){
        JOptionPane.showMessageDialog(updateView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }     

    
    
    
    private MouseAdapter signUpAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
               String username = usernameField.getText().trim();
                if (username.isBlank()) {
                    fieldErrorPane("Username cannot be blank");
                    return;
                }

                String password = passwordField.getText().trim();
//                if (password.isBlank()) {
//                    fieldErrorPane("Password cannot be blank");
//                    return;
//                }

                String name = nameField.getText().trim();
                if (name.isBlank()) {
                    fieldErrorPane("Name cannot be blank");
                    return;
                }

                String surname = surnameField.getText().trim();
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

                String iban = ibanField.getText().trim();
                if (iban.isBlank()) {
                    fieldErrorPane("IBAN cannot be blank");
                    return;
                }

                String vehicle = vehicleTypeField.getText().trim();
                if (vehicle.isBlank()) {
                    fieldErrorPane("Vehicle cannot be blank");
                    return;
                }

                String telephoneNumber = telephoneField.getText().trim();
                if (telephoneNumber.isBlank() || (!telephoneNumber.matches("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"))) {
                    fieldErrorPane("TelephoneNumber cannot be blank and has to be of the folliwing types\n"
                            + "368 734 8201\n"
                            + "368-734-8201\n"
                            + "+39 368 734 8201\n"
                            + "+39-368-734-8201\n");
                    return;
                }
                
                //Request to server for creating a new Customer
                
                RiderEntity rider = new RiderEntity(riderId,username, password, name, surname, formattedBirthDate , iban, telephoneNumber, vehicle);
                Call<RiderEntity> call2 = apiService.updateRider(rider);

                call2.enqueue(new Callback<RiderEntity>() {

                    @Override
                    public void onResponse(Call<RiderEntity> call, Response<RiderEntity> response) {

                        if (response.isSuccessful()) { // status code tra 200-299
                           JOptionPane.showMessageDialog(updateView, "ACCOUNT INFORMATIONS ARE BEEN UPTATED", "Sign up success", JOptionPane.INFORMATION_MESSAGE);
                           
                           System.out.println("Gestire navigator");
                           // GESTIRE NAVIGATOR 
                           //If sign up procedure is correct change current view with log in one
                           //   navigator.fromSignUpToLogIn(RiderUpdateController.this); 
                
                
                        } else { // if server error occurs
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                JOptionPane.showMessageDialog(updateView,jObjError.get("message") , "Server error", JOptionPane.ERROR_MESSAGE);
                            } catch (IOException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RiderEntity> call, Throwable t) {
                        // Log error here since request failed
                        JOptionPane.showMessageDialog(updateView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

                    }

                });

            }
        
          
        
        
    };

    public RiderUpdateView getUpdateView() {
        return updateView;
    }
            



    
 
    
      //Shows a pop up to inform the user that the informations that is typing are not correct
    
//       private MouseAdapter loginAction = new MouseAdapter() {
//        @Override
//        public void mouseClicked(MouseEvent mouseEvent) {
//            
//              navigator.fromSignUpToLogIn(RiderUpdateController.this);
//        }
//           
//    };
        
    public void disposeView(){
        updateView.dispose();
    }
    
    
     public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RiderUpdateController c = new RiderUpdateController(30L);
            }
        });
    }
    
               
               
}

