/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.riderclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.model.dao.order.DishEntity;
import com.mycompany.common.model.dao.order.DishOrderAssociation;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.riderclient.api.ServiceApi;
import com.mycompany.riderclient.navigator.Navigator;
import com.mycompany.riderclient.view.RiderHomeView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author Amos
 */
public class RiderHomeController {
    
    
    private RiderHomeView homeView;
    private JTable orderTable;
    private JButton acceptBtn;
    private JButton showBtn;
    private JRadioButton semiAcceptedRadio;
    private JRadioButton acceptRadio;
    
    private ButtonGroup btnGroup;
    
    private JMenuItem updateItem;
    private JMenuItem logOutItem;
    
      
    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator navigator;
    
    private Long riderId;
    
    
     public void disposeView(){
        homeView.dispose();
    }
     
     public RiderHomeController(Long riderId){
        this.riderId = riderId;
        homeView = new RiderHomeView();
        homeView.setVisible(true);
        
        
        semiAcceptedRadio = homeView.getRadioSemiAcc();
        acceptRadio = homeView.getRadioAccepted();
        semiAcceptedRadio.setSelected(true);
        acceptBtn = homeView.getAcceptBtn();
        showBtn = homeView.getViewOrderBtn();

        showBtn.setEnabled(false);
        orderTable = homeView.getRiderTable();
        
        btnGroup = homeView.getBtnRadioGroup();
        
        updateItem = homeView.getMyInfoItem();
        logOutItem = homeView.getLogoutItem();
        
        
               // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);
        
        navigator = Navigator.getInstance();
        
        
       acceptBtn.addMouseListener(acceptOrderAction); 
       showBtn.addMouseListener(showOrderAction);           
       acceptRadio.addActionListener(acceptRadioAction);
       semiAcceptedRadio.addActionListener(semiAcceptRadioAction);
       
       updateItem.addActionListener(updateAction);
       
       logOutItem.addActionListener(logoutAction);
        
        fillOrderTable();
     }
     
     
    public void fillOrderTable(){
         
          Call<List<OrderDto>> call = null;
         if(semiAcceptedRadio.isSelected()){         
             call = apiService.getSemiAcceptedOrders();
         }
         else if(acceptRadio.isSelected()){
             call = apiService.getAtLeastAccepted(riderId);
         }
        
        call.enqueue(new Callback<List<OrderDto>>() {
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                    List<OrderDto> ordersDto = response.body();
                    DefaultTableModel ordersTableModel = (DefaultTableModel) orderTable.getModel();
                    ordersTableModel.setRowCount(0);
                    Object row[] = new Object[6];
                    DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm");
                     
                    for(OrderDto ord: ordersDto){
                        row[0] = ord.getId();
                        row[1]= ord.getProvider().getProviderName();
                        row[2] = ord.getProvider().getAddress();
                        row[3] = ord.getCustomer().getName()+" "+ord.getCustomer().getSurname();
                        row[4] = ord.getCustomer().getAddress();
                      
                        try {
                            row[5] = formatter.parse(ord.getDeliveryTime()).toLocaleString();
                        } catch (ParseException ex) {
                            Logger.getLogger(RiderHomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                          ordersTableModel.addRow(row);
                        
                    }
                    orderTable.setModel(ordersTableModel); 
                 
                } else { // if server error occurs
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                    } catch (IOException ex) {
                        Logger.getLogger(RiderOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(homeView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                }
                
              
                
                
            }

            @Override
            public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(homeView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

            }

        });
        
     }
     
    private MouseAdapter acceptOrderAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
            
            int selectedRow = orderTable.getSelectedRow();
            
            if(!acceptBtn.isEnabled())
                return;
            
            if(selectedRow==-1){ //se ha selezionato almeno una riga nella tabella semiaccepted
                JOptionPane.showMessageDialog(homeView,
                                "Order not selected!",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            //recuperare Id dalla tabella - 30L
             Call<Void> call2 = apiService.acceptOrder(riderId,(Long) orderTable.getModel().getValueAt(selectedRow, 0));
             
              call2.enqueue(new Callback<Void>() {
                  
                   @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                        fillOrderTable();
                          JOptionPane.showMessageDialog(homeView, "Order accepted succesfully", "Order shipped", JOptionPane.INFORMATION_MESSAGE);
                      }
                  
                else { // if server error occurs
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                    } catch (IOException ex) {
                        Logger.getLogger(RiderOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(homeView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                }
                
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(homeView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

            }
                  
              });
          
        
            
        }};
    
    
     private MouseAdapter showOrderAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
            int selectedRow = orderTable.getSelectedRow();
            if(!showBtn.isEnabled())
                return;
            
            if(selectedRow==-1){ //se ha selezionato almeno una riga nella tabella semiaccepted
                 JOptionPane.showMessageDialog(homeView,
                                "Order not selected!",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

                return;
            }
            
            navigator.fromHomeToOrderView(RiderHomeController.this, (Long) orderTable.getModel().getValueAt(selectedRow, 0));
            
            //col navigator passare a RiderOrderController
            
        }
        
     };
    
    private ActionListener acceptRadioAction = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {            
            acceptBtn.setEnabled(false);
            showBtn.setEnabled(true);
            fillOrderTable();
        }
        
        
        
    };
    
     private ActionListener updateAction = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {            
            navigator.showUpdateView(riderId);
        }
        
        
        
    };
     
        private ActionListener logoutAction = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {            
            navigator.fromHomeToLogin(RiderHomeController.this);
        }
        
        
        
    };
    
    
     
    private ActionListener semiAcceptRadioAction = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
            showBtn.setEnabled(false);
            acceptBtn.setEnabled(true);
            fillOrderTable();
        }
        
    };

    public Long getRiderId() {
        return riderId;
    }
  
  
     

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RiderHomeController contr = new RiderHomeController(35L);
    }
    
}
