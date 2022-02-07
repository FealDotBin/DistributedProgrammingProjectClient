/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller;

import com.google.common.hash.Hashing;
import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.customerclient.controller.CustomerUpdateController;
import com.mycompany.providerclient.navigator.Navigator;
import com.mycompany.providerclient.api.ServiceApi;
import com.mycompany.providerclient.model.ProviderEntity;
import com.mycompany.providerclient.view.UpdateView;
import com.toedter.calendar.JDateChooser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents the controller associated to UpdateView class.
 * It is responsible of taking the user's input to update the provider's infos.
 * Whenever it's necessary to send or retrieve datas to/from server, the
 * API endpoint (ServiceApi class) is being used.
 * @author aferr
 */
public class UpdateController {
    
    private Long providerId;
    private ProviderEntity provider;
    private UpdateView updateView;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JTextField ibanTextField;
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JTextField telephoneNumberTextField;
    private JDateChooser birthDateChooser;
    private JTextField addressTextField;
    private JTextField providerNameTextField;
    private JTextField cuisineTextField;
    private JCheckBox takeAwayCheckBox;
    private JCheckBox ownRidersCheckBox;
    private JCheckBox deliveringCheckBox;
    private JButton updateBtn;
    private JButton manageMenuBtn;
    private JButton homeBtn;
    private JButton logOutBtn;
    private Navigator nav;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    
    /**
     * Initialize both the view and the controller by filling all the input fields
     * (except the password) with the provider's infos and attaching all 
     * necessary listeners to the buttons.
     */
    public UpdateController(Long providerId){
        this.providerId = providerId;
        
        // initialize View
        updateView = new UpdateView();
        updateView.setVisible(true);
        
        // get view's components
        usernameTextField = updateView.getUsernameTextField();
        passwordTextField = updateView.getPasswordTextField();
        ibanTextField = updateView.getIbanTextField();
        nameTextField = updateView.getNameTextField();
        surnameTextField = updateView.getSurnameTextField();
        telephoneNumberTextField = updateView.getTelephomeTextField();
        birthDateChooser = updateView.getBirthDateChooser();
        addressTextField = updateView.getAddressTextField();
        providerNameTextField = updateView.getProviderNameTextField();
        cuisineTextField = updateView.getCuisineTextField();
        takeAwayCheckBox = updateView.getTakeAwayCheckBox();
        ownRidersCheckBox = updateView.getOwnRidersCheckBox();
        deliveringCheckBox = updateView.getDeliveringCheckBox();
        updateBtn = updateView.getUpdateBtn();
        manageMenuBtn = updateView.getManageMenuBtn();
        homeBtn = updateView.getHomeBtn();
        logOutBtn = updateView.getLogOutBtn();
        
        // view navigator creation
        nav = Navigator.getInstance();
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // get provider's info from server
        getMyInfoCall();
        
        // attach listeners to buttons
        attachListenerToUpdateBtn();
        attachListenerToManageMenuBtn();
        attachListenerToHomeBtn();
        attachListenerToLogOutBtn();
    }
    
    /**
     * Getter for providerId
     * @return the provider's id
     */
    public Long getProviderId(){
        return providerId;
    }
    
    /**
     * Destroy the homeView instance and all its components.
     */
    public void disposeView(){
        updateView.dispose();
    }
    
    /**
     * Shows a pop up to inform the user that the informations that is typing are not correct
     */ 
    private void fieldErrorPane(String errorMessage) {
        JOptionPane.showMessageDialog(updateView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Fill all the input fields with the provider's infos
     */
    private void setMyInfoOnView(){
        usernameTextField.setText(provider.getUsername());
        ibanTextField.setText(provider.getIban());
        nameTextField.setText(provider.getName());
        surnameTextField.setText(provider.getSurname());
        telephoneNumberTextField.setText(provider.getTelephoneNumber());
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = null;
        try {
            birthDate = formatter.parse(provider.getBirthDate());
        } catch (ParseException ex) {
            Logger.getLogger(CustomerUpdateController.class.getName()).log(Level.SEVERE, null, ex);
        }
        birthDateChooser.setDate(birthDate);
        addressTextField.setText(provider.getAddress());
        providerNameTextField.setText(provider.getProviderName());
        cuisineTextField.setText(provider.getCuisine());
        takeAwayCheckBox.setSelected(provider.getDoTakeAway());
        ownRidersCheckBox.setSelected(provider.getHasOwnRiders());
        deliveringCheckBox.setSelected(provider.getDoDelivering());
    }
    
    /**
     * Send a GET request to the server to get the provider's account.
     * If the response is successful, the provider's account is successfully 
     * retrieved and his infos are displayed on the input fields;
     * otherwise, an error message is shown to the user.
     */
    private void getMyInfoCall(){
        Call<ProviderEntity> getMyInfoCall = serviceApi.getMyInfo(providerId);
        getMyInfoCall.enqueue(new Callback<ProviderEntity>(){
            @Override
            public void onResponse(Call<ProviderEntity> call, Response<ProviderEntity> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    provider = response.body();
                    setMyInfoOnView();
                }
                else{ // in caso di errori
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(
                                updateView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
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
     * Send a PUT request to the server to update the provider's account.
     * If the response is successful, the provider's account has been updated
     * successfully;
     * otherwise, an error message is shown to the user.
     */
    private void putProviderCall(ProviderEntity updatedProvider){
        Call<ProviderEntity> putProviderCall = serviceApi.putProvider(updatedProvider);
        putProviderCall.enqueue(new Callback<ProviderEntity>(){
            @Override
            public void onResponse(Call<ProviderEntity> call, Response<ProviderEntity> response) {

                if (response.isSuccessful()) { // status code tra 200-299
                    JOptionPane.showMessageDialog(updateView,
                            "ACCOUNT INFORMATIONS ARE BEEN UPTATED",
                            "Update success",
                            JOptionPane.INFORMATION_MESSAGE);
                    provider = updatedProvider;
                    passwordTextField.setText("");
                } 
                else { // if server error occurs
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(updateView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(UpdateController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProviderEntity> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(updateView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);

            }
        });
    }
    
    /**
     * Attach an ActionListener to updateButton in order to update the Provider
     * account using the infos provided by the user through the input fields, 
     * whenever the button is pressed.
     */
    private void attachListenerToUpdateBtn(){
        updateBtn.addActionListener(event -> {
            String username = usernameTextField.getText().trim();
                if (username.isBlank()) {
                    fieldErrorPane("Username cannot be blank");
                    return;
                }

                String password = passwordTextField.getText().trim();
                if(!password.isBlank()){
                    password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
                }

                String name = nameTextField.getText().trim();
                if (name.isBlank()) {
                    fieldErrorPane("Name cannot be blank");
                    return;
                }

                String surname = surnameTextField.getText().trim();
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

                String iban = ibanTextField.getText().trim();
                if (iban.isBlank()) {
                    fieldErrorPane("IBAN cannot be blank");
                    return;
                }

                String address = addressTextField.getText().trim();
                if (address.isBlank()) {
                    fieldErrorPane("Address cannot be blank");
                    return;
                }

                String telephoneNumber = telephoneNumberTextField.getText().trim();
                if (telephoneNumber.isBlank() || (!telephoneNumber.matches("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"))) {
                    fieldErrorPane("TelephoneNumber cannot be blank and has to be of the folliwing types\n"
                            + "368 734 8201\n"
                            + "368-734-8201\n"
                            + "+39 368 734 8201\n"
                            + "+39-368-734-8201\n");
                    return;
                }
                
                String providerName = providerNameTextField.getText().trim();
                if (providerName.isBlank()) {
                    fieldErrorPane("Provider name cannot be blank");
                    return;
                }
                
                String cuisine = cuisineTextField.getText().trim();
                if (cuisine.isBlank()) {
                    fieldErrorPane("Cuisine cannot be blank");
                    return;
                }
                
                Boolean takeAway = takeAwayCheckBox.isSelected();
                Boolean ownRiders = ownRidersCheckBox.isSelected();
                Boolean delivering = deliveringCheckBox.isSelected();
                
                // if all checkboxes are unchecked pop up an error dialog,
                // otherwise send data to server
                if(!takeAway && !ownRiders && !delivering){
                    fieldErrorPane("You must check at least one of "
                            + "\"take away\", \"own riders\" or \"delivering\"");
                }
                else {
                    // create a new provider entity with user's inputs
                    ProviderEntity updatedProvider = new ProviderEntity(
                            provider.getId(), username, password, name, surname, 
                            formattedBirthDate, iban, telephoneNumber, providerName,
                            cuisine, address, delivering, takeAway, ownRiders,
                            provider.getIsAvailable());
                    putProviderCall(updatedProvider);
                }
        });
    }
    
    /**
     * Attach an ActionListener to manageMenuButton in order to switch to
     * the "manage menu" window, when the button is pressed.
     */
    private void attachListenerToManageMenuBtn(){
        manageMenuBtn.addActionListener(event -> {
            nav.fromUpdateToMenuManager(UpdateController.this);
        });
    }
    
    /**
     * Attach an ActionListener to homeButton in order to switch to
     * the "home" window, when the button is pressed.
     */
    private void attachListenerToHomeBtn(){
        homeBtn.addActionListener(event -> {
            nav.fromUpdateToHome(UpdateController.this);
        });
    }
    
    /**
     * Attach an ActionListener to logOutButton in order to switch to
     * the "login" window, when the button is pressed.
     */
    private void attachListenerToLogOutBtn(){
        logOutBtn.addActionListener(event -> {
            nav.fromUpdateToLogIn(UpdateController.this);
        });
    }
}
