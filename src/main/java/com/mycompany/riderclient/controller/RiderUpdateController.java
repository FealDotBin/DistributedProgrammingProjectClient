/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.riderclient.controller;

import com.google.common.hash.Hashing;
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
import java.nio.charset.StandardCharsets;
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
 * This class represents the controller of the view RiderUpdateView. 
 * Allows to control and manage all the GUI components.
 * It also manages the logic needed to call the APIs asynchronously, based on the user request on the GUI.
 * 
 * The controller, given the rider ID, allows you to perform the following operations:
 * - update your profile, by entering all the necessary personal information
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
    /**
      * Initialize:
      * - the view and its components;
      * - the retrofitBuilder and the serviceApi in order to invoke the API calls.
      * - the Navigator to switch between views
      * - attach listeners to buttons in order to manage events
      * 
     * @param riderId is the rider ID that uniquely identifies the rider 
     */
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
        
         // add listeners to button
        updateBtn.addMouseListener(updateAction);
        
        fillField();
        
    }
    
    /**
     * This method allows to fill text fields;
     * The method calls an asynchronous api in order to get the rider info
     */
    private void fillField(){
        
        //API CALL
        //Fill fields with customer current information saved on server
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

    
    
    /**
     * It's a mouse listener that allows you to update the rider profile, given all 
     * the necessary personal information.
     * 
     * The method calls an asynchronous API to update. 
     */
    private MouseAdapter updateAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
                // Getting all the info
                
               String username = usernameField.getText().trim();
                if (username.isBlank()) {
                    fieldErrorPane("Username cannot be blank");
                    return;
                }
                
                
                String password = passwordField.getText().trim();
                if(!password.isBlank()){
                    password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
                }


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
                
                //API CALL
                
                RiderEntity rider = new RiderEntity(riderId,username, password, name, surname, formattedBirthDate , iban, telephoneNumber, vehicle);
                Call<RiderEntity> call2 = apiService.updateRider(rider);

                call2.enqueue(new Callback<RiderEntity>() {

                    @Override
                    public void onResponse(Call<RiderEntity> call, Response<RiderEntity> response) {

                        if (response.isSuccessful()) { // status code tra 200-299
                           JOptionPane.showMessageDialog(updateView, "ACCOUNT INFORMATIONS ARE BEEN UPTATED", "Sign up success", JOptionPane.INFORMATION_MESSAGE);
                                           
                
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
            
        
    public void disposeView(){
        updateView.dispose();
    }
    
   
               
}

