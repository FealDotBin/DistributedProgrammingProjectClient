/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.google.common.hash.Hashing;
import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.model.CustomerEntity;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.customerSignUp;
import com.mycompany.customerclient.view.customerUpdate;
import com.sun.tools.javac.Main;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 * This class represents customerUpdate GUI controller. 
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restfull server when needed:
 * -To update customer information
 * -To decide which controller to invoke after that home button has been pressed.
 */
public class CustomerUpdateController {

    private JTextField usernameField;
    private JTextField passwordField;
    private JTextField nameField;
    private JTextField surnameField;
    private JDateChooser birthDateChooser;
    private JTextField ibanField;
    private JTextField addressField;
    private JTextField telephoneField;
    private JButton updateButton;
    private JButton homeButton;
    private JButton balanceButton;
    private JButton historyButton;
    private JButton logOutButton;
    private customerUpdate updateView;
    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator nav;
    private Long customerId;
    private OrderDto currentOrder;

    /**
     * Initialize view's obtaing customer account information from server.
     * Send a customer update request to the server with information taken from view
     * Add listeners to other buttons to navigate from this controller to the next 
     * @param customerId represents the logged customer's id 
     */
    public CustomerUpdateController(Long customerId) {
        this.customerId = customerId;

        //View creation
        updateView = new customerUpdate();
        //Component getter
        usernameField = updateView.getUsernameTextField();
        passwordField = updateView.getPasswordTextField();
        nameField = updateView.getNameTextField();
        surnameField = updateView.getSurnameTextField();
        birthDateChooser = updateView.getBirthDateChooser();
        ibanField = updateView.getIbanTextField();
        addressField = updateView.getAddressTextField();
        telephoneField = updateView.getTelephoneTextField();
        updateButton = updateView.getUpdateBtn();
        homeButton = updateView.getHomeBtn();
        logOutButton = updateView.getLogOutBtn();
        historyButton = updateView.getHistoryBtn();
        balanceButton = updateView.getBalanceBtn();
        
        //View navigator creation
        nav = Navigator.getInstance();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);
        
         //Set home button text and his action listener based on the presence of current order
        addHomeButtonLogic();

        // When logout button is pressed diplay logIn view
        logOutButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromUpdatetoLogOut(CustomerUpdateController.this);
            }
            
        });

        //Fill fields with customer actual information saved on server
        fillFields();

        //Action perfomed when update confitm button is pressed: read increment from view, validate it, calculate new balance,
        //send it to server and wait for response.
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCustomer();

                //Request to server for update Customer with customerId
                

            }

        });
        
        
        // When history button is pressed display customer order history view
        historyButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromUpdatetoHistory(CustomerUpdateController.this);
            }
            
        });
        
        // When balance button is pressed display update balance view
        balanceButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromUpdatetoBalance(CustomerUpdateController.this);
            }
            
        });
        updateView.setVisible(true);
    }
    
    /**
     * The method sends a GET request to the server to obtain the information currently saved on the server about the logged in customer.
     * After that it uses them to fill in the fields of the view, except the password one which remains empty for security reasons.
     * In case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void fillFields(){
        Call<CustomerEntity> call = apiService.getCustomer(this.customerId);
        call.enqueue(new Callback<CustomerEntity>() {
            @Override
            public void onResponse(Call<CustomerEntity> call, Response<CustomerEntity> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                    CustomerEntity customer = response.body();
                    usernameField.setText(customer.getUsername());
                    nameField.setText(customer.getName());
                    surnameField.setText(customer.getSurname());
                    addressField.setText(customer.getAddress());
                    telephoneField.setText(customer.getTelephoneNumber());
                    ibanField.setText(customer.getIban());
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthDate = null;
                    try {
                        birthDate = formatter.parse(customer.getBirthDate());
                    } catch (ParseException ex) {
                        Logger.getLogger(CustomerUpdateController.class.getName()).log(Level.SEVERE, null, ex);
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
            public void onFailure(Call<CustomerEntity> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(updateView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

            }

        });
    }
    
    /**
     * The method reads and validates the inputs entered by the user via the GUI.
     * After that it sends a PUT request to the server to update logged in customer'information.
     * In case of a communication error with the server, messages are displayed to inform the customer.
     * @param customer object that represents the information about the new customer that wants to sing up
     */
    private void updateCustomer(){
        String username = usernameField.getText().trim();
        if (username.isBlank()) {
            fieldErrorPane("Username cannot be blank");
            return;
        }

        String password = passwordField.getText().trim();
        if(!password.isBlank())
            password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

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

        String address = addressField.getText().trim();
        if (address.isBlank()) {
            fieldErrorPane("Address cannot be blank");
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
        CustomerEntity newCustomer = new CustomerEntity(CustomerUpdateController.this.customerId,username, password, name, surname, formattedBirthDate, iban, telephoneNumber, address);
        updateRequest(newCustomer);
    }
  
    /**
     * The methos sends a PUT request to the server to update costumer information with those contained in the input parameter
     * In case of a communication error with the server, messages are displayed to inform the customer.
     * @param customer object that contains the information about the customer that you want to update.
     */
    
    private void updateRequest(CustomerEntity customer){
        Call<CustomerEntity> call2 = apiService.updateCustomer(customer);

        call2.enqueue(new Callback<CustomerEntity>() {

            @Override
            public void onResponse(Call<CustomerEntity> call, Response<CustomerEntity> response) {

                if (response.isSuccessful()) { // status code tra 200-299
                    JOptionPane.showMessageDialog(updateView, "ACCOUNT INFORMATIONS ARE BEEN UPTATED", "Update success", JOptionPane.INFORMATION_MESSAGE);
                    passwordField.setText("");
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
            public void onFailure(Call<CustomerEntity> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(updateView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);

            }

        });
    }
    
    /**
     * This method sends three calls to the server: the first requires the current order of the costumer to set the appropriate text to be displayed on the home button. 
     * If the current order is not present, a listener is added to the button that allows you to view the GUI relating to the choice of providers.
     * If, on the other hand, the current order is present at the moment of the construction of the button this could have been refused or completed at the moment of its pressure. 
     * So when this event occurs, a new call is made to get the current order again. 
     * If this is present, the interface that allows you to view information about it will be shown. 
     * Instead, if not present, a third call is made this time to retrieve the last status of the order. Which is notified to the user together with any refund for a rejected order. 
     * After that the GUI for the choice of providers will be shown.
     * Furthermore case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void addHomeButtonLogic(){
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>(){
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()){
                    currentOrder = response.body();
                    homeButton.setText("Current Order");
                    homeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            changeToHome();
                        }

                    });
                }
                else{
                    homeButton.setText("Create Order");
                    homeButton.addActionListener(new ActionListener(){
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            nav.fromUpdatetoProviderSelection(CustomerUpdateController.this);
                        }
                        
                    });
                }
            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(updateView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);
            }
            
        });
    }
    
     /**
     * The method sends a GET to the server to get the current order. If present, the GUI for its visualization will be shown.
     * Otherwise a new GET is performed. This time the status of the order is obtained (which has been completed or rejected), which is communicated to the costumer through a specific notification.
     * Finally the GUI for the choice of providers will be shown
     * Furthermore in case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void changeToHome() {
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(CustomerUpdateController.this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    currentOrder = response.body();
                    nav.fromUpdatetoOrderViewer(CustomerUpdateController.this);
                } else {
                    getOrderById();
                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(updateView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
    }
    /**
     *The method sends a GET to get the status of the last current order.
     *If this is completed or refused, the user is notified together with any refund for the order placed by him.
     *Finally the GUI for the provide interface will be displayed.
     *Furthermore in case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void getOrderById() {
        Call<OrderDto> getOrderById = apiService.getOrderDTO(CustomerUpdateController.this.currentOrder.getId());
        getOrderById.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    OrderDto orderGhost = response.body();
                    if (orderGhost.getOrderState().equals(OrderState.REFUSED)) {
                        JOptionPane.showMessageDialog(updateView, "YOUR CURRENT ORDER HAVE BEEN REFUSED BUT YOU GOT: " + orderGhost.getPrice() + " MONEY BACK", "Order Refused", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(updateView, "YOUR CURRENT ORDER HAVE BEEN COMPLETED", "Order Completed", JOptionPane.INFORMATION_MESSAGE);
                    }
                    File file = new File("customer"+customerId+"/persistentOrder.txt");
                    if(file.delete()){
                        System.out.println("File cancellato con successo");
                    }
                    else
                        System.out.println("Problemi con la cancellazione del file");
                    nav.fromUpdatetoProviderSelection(CustomerUpdateController.this);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(updateView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(updateView,
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
    private void fieldErrorPane(String errorMessage) {
        JOptionPane.showMessageDialog(updateView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    
     /**
     * Remove customerUpdate view from the visualization
     */
    public void disposeView(){
        updateView.dispose();
    }

    /**
     * Returns the logged customer's id
     * @return String that represent the logged customer's id 
     */
    public Long getCustomerId() {
        return customerId;
    }

}
