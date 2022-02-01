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

    public CustomerProviderSelectionController(Long customerId) {
        // initialize view
        providerSelectionView = new ProviderSelection();
        this.customerId = customerId;
        // get components from view
        providerTable = providerSelectionView.getProviderTable();
        //Action perform when button in providerTable is pressed: Retrieve providerId of provider selected and display 
        //menù selection view
        JButtonEditor providerTableEditor = new JButtonEditor("View Menù", new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = providerTable.getSelectedRow();
                selectedProvider = providerList.get(selectedRow);
                providerId = selectedProvider.getId();
                nav.fromProviderSelectionToOrderCreation(CustomerProviderSelectionController.this);
            }
            
        });
        providerTable.setDefaultEditor(JButton.class, providerTableEditor);
        logOutButton = providerSelectionView.getLogoutBtn();
        historyButton = providerSelectionView.getHistoryBtn();
        updateButton = providerSelectionView.getAccountBtn();
        balanceButton = providerSelectionView.getBalanceBtn();
        //action performed when log out is pressed: return to log out view
        logOutButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromProviderSelectionToLogIn(CustomerProviderSelectionController.this);
            }
            
        });
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // get navigator
        nav= Navigator.getInstance();
        
        //Request to server for obtaining available providers
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
        providerSelectionView.setVisible(true);
        // When history button is pressed display customer order history view
        historyButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromProviderSelectiontoHistory(CustomerProviderSelectionController.this);
            }
            
        });
        
    }
    
    public void disposeView(){
        providerSelectionView.dispose();
    }
    public Long getProviderId(){
        return providerId;
    }
    
    public ProviderDto getSelectedProvider(){
        return selectedProvider;
    }

    public Long getCustomerId() {
        return customerId;
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProviderSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProviderSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProviderSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProviderSelection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerProviderSelectionController(2L);
            }
        });
    }
}
