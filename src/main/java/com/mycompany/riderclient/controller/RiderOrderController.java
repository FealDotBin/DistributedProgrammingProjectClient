/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.riderclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.riderclient.api.ServiceApi;
import com.mycompany.riderclient.navigator.Navigator;
import com.mycompany.riderclient.view.DeliveryOrderView;
import com.sun.tools.javac.Main;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author Amos
 */
public class RiderOrderController {
    
    private DeliveryOrderView orderView;
    private JLabel providerNameLabel;
    private JLabel providerAddressLabel;
    private JLabel providerTelLabel;
    
    private JLabel customerNameLabel;
    private JLabel customerAddressLabel;
    private JLabel customerTelLabel;
    
    private JLabel deliveryTimeLabel;
    
    private JLabel orderStateLabel;
    
    private JButton shipOrderBtn;
    private JButton completeOrderBtn;
    
    private JTable dishTable;
    
    
    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator navigator;
    
    private Long riderId=35L;
    private Long orderId=33L;
    
    public RiderOrderController(){
        orderView = new DeliveryOrderView();
        orderView.setVisible(true);
        providerNameLabel = orderView.getProviderNameLabel();
        providerAddressLabel = orderView.getProviderAddressLabel();
        providerTelLabel = orderView.getProviderTelephoneLabel();
    
        customerNameLabel = orderView.getCustomerNameLabel();
        customerAddressLabel = orderView.getCustomerAddressLabel();
        customerTelLabel = orderView.getCustomerTelephoneLabel();

        deliveryTimeLabel = orderView.getDeliveryTimeLabel();

        orderStateLabel = orderView.getStateLabel();

        shipOrderBtn = orderView.getShippedBtn();
        completeOrderBtn = orderView.getCompleteBtn();
        
        
        
         // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);
        
        //Add listeners
        shipOrderBtn.addMouseListener(shipOrderAction);        
        completeOrderBtn.addMouseListener(completeOrderAction);
        
        fillOrderLabels();
    }
    
    
    private void fillOrderLabels(){
        
             Call<OrderDto> call = apiService.getOrderById(this.orderId);
        call.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                    OrderDto orderDto = response.body();
                    providerNameLabel.setText("Provider: "+orderDto.getProvider().getProviderName());
                    providerAddressLabel.setText("Provider address: "+orderDto.getProvider().getAddress());
                    providerTelLabel.setText("Provider telephone: "+orderDto.getProvider().getTelephoneNumber());
                    
                    customerNameLabel.setText("Customer Name: "+orderDto.getCustomer().getName()+" "+orderDto.getCustomer().getSurname());
                    customerAddressLabel.setText("Customer address: "+orderDto.getCustomer().getAddress());
                    customerTelLabel.setText("Customer telephone: "+orderDto.getCustomer().getTelephoneNumber());
                    DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm");
                     
                    orderStateLabel.setText("Order state: "+orderDto.getOrderState());
                     
                     
                    try {
                        deliveryTimeLabel.setText("Delivery time: "+formatter.parse(orderDto.getDeliveryTime()).toLocaleString());
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(RiderOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else { // if server error occurs
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                    } catch (IOException ex) {
                        Logger.getLogger(RiderOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(orderView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(orderView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

            }

        });
        
        
    }
    
    
    public void disposeView(){
        orderView.dispose();
    }
    
    
        
       private MouseAdapter shipOrderAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
             //chiamare ship order
        }
           
    };
    
    
      private MouseAdapter completeOrderAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
             //chiamare complete order
        }
           
    };
    
      
      
          
     public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RiderOrderController c = new RiderOrderController();
            }
        });
    }
}
