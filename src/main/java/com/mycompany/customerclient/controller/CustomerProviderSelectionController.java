/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JButtonEditor;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.ProviderSelection;
import com.mycompany.providerclient.controller.LogInController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 * This class represents ProviderSelection GUI controller. 
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restfull server when needed:
 * -To obtain all available providers 
 * 
 */
public class CustomerProviderSelectionController {
    private ProviderSelection providerSelectionView;
    private JTable providerTable;
    private JButton logOutButton;
    private JButton historyButton;
    private JButton updateButton;
    private JButton balanceButton;
    private Long customerId;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private Navigator nav;
    private Long providerId;
    private ProviderDto selectedProvider;
    private List<ProviderDto> providerList;

    /**
     * Initialize provider table obtaining available providers from server.
     * Add listener to buttons in provider table to select providers where you wanna eat
     * Add listeners to other buttons to navigate from this controller to the next
     * @param customerId represents the logged customer's id
     */
    public CustomerProviderSelectionController(Long customerId) {
        // initialize view
        providerSelectionView = new ProviderSelection();
        this.customerId = customerId;
        // get components from view
        providerTable = providerSelectionView.getProviderTable();
        logOutButton = providerSelectionView.getLogoutBtn();
        historyButton = providerSelectionView.getHistoryBtn();
        updateButton = providerSelectionView.getAccountBtn();
        balanceButton = providerSelectionView.getBalanceBtn();
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // get navigator
        nav= Navigator.getInstance();
        
        
        //Action perform when button in providerTable is pressed: Retrieve providerId of provider selected and display 
        //menù selection view
        JButtonEditor providerTableEditor = new JButtonEditor("View Menù", new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                addTableButtonLogic();
            }
            
        });
        providerTable.setDefaultEditor(JButton.class, providerTableEditor);
        
        //action performed when log out is pressed: return to log out view
        logOutButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromProviderSelectionToLogIn(CustomerProviderSelectionController.this);
            }
            
        });
        
        
        //Request to server for obtaining available providers
        fillProvidersTable();
        
        //When logout button is pressed logIn view is displayed
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromProviderSelectionToLogIn(CustomerProviderSelectionController.this);
            }
        });

        //When update account button is pressed update customer information view is displayed
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromProviderSelectiontoUpdate(CustomerProviderSelectionController.this);
            }
        });

        //When balance button is pressed balance update view is displayed
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromProviderSelectiontoBalance(CustomerProviderSelectionController.this);
            }
        });

        // When logout history button is pressed display customer order history view
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromProviderSelectiontoHistory(CustomerProviderSelectionController.this);
            }

        });
        providerSelectionView.setVisible(true);
    }
    
    /**
     * The method gets the selected provider using the row selected by the custumer on the view.
     * After that it shows the GUI for displaying the menu of the chosen provider. 
     */
    private void addTableButtonLogic(){
        int selectedRow = providerTable.getSelectedRow();
        selectedProvider = providerList.get(selectedRow);
        providerId = selectedProvider.getId();
        nav.fromProviderSelectionToOrderCreation(CustomerProviderSelectionController.this);
    }
    
    /**
    * The method sends a GET request to the server to get all available providers.
    * Once obtained, it fills the view table with a row for each provider.
    * Each line includes only some information about the order such as: 
    * provider name, cuisine type, if it makes home deliveries, if it carries out a takeaway service, the address and telephone number.
    * In case of a communication error with the server, messages are displayed to inform the customer.
    */
    private void fillProvidersTable(){
        Call<List<ProviderDto>> availableProviderCall = serviceApi.getAvailableProviderDTO();
        availableProviderCall.enqueue(new Callback<List<ProviderDto>>(){
                
            @Override
            public void onResponse(Call<List<ProviderDto>> call, Response<List<ProviderDto>> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    providerList = response.body();
                    DefaultTableModel providerTableModel = (DefaultTableModel)providerTable.getModel();
                    for(ProviderDto provider: providerList){
                        Object [] row = new Object[7];
                        row[0] = provider.getProviderName();
                        row[1] = provider.getCuisine();
                        row[2] = provider.getDoDelivering();
                        row[3] = provider.getDoTakeAway();
                        row[4] = provider.getAddress();
                        row[5] = provider.getTelephoneNumber();
                        row[6] = new JButton();
                        providerTableModel.addRow(row);
                    }
                }
                else{ // in caso di errori
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(providerSelectionView,
                            jObjError.get("message"),
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProviderDto>> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(providerSelectionView,
                            "Contact you system administrator",
                            "CRITICAL ERROR",
                            JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    /**
     * Remove Provider selection view from the visualization
     */
    public void disposeView(){
        providerSelectionView.dispose();
    }
    /**
     * Return selected providerId
     * @return selected providerId 
     */
    public Long getProviderId(){
        return providerId;
    }
    /**
     * Returns selected provider by costumer through view
     * @return selected provider by costumer
     */        
    public ProviderDto getSelectedProvider(){
        return selectedProvider;
    }
    /**
     * Returns the logged customer's id
     * @return String that represent the logged customer's id 
     * 
     */
    public Long getCustomerId() {
        return customerId;
    }
    
}
