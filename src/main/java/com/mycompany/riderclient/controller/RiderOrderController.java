/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.riderclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.model.dao.order.DishEntity;
import com.mycompany.common.model.dao.order.DishOrderAssociation;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class represents the controller of the view DeliveryOrderView. 
 * Allows to control and manage all the GUI components.
 * It also manages the logic needed to call the APIs asynchronously, based on the user request on the GUI.
 * 
 * The controller, given the order ID and the rider ID, allows you to perform the following operations:
 * - view all informations of an accepted order, 
 * - set the order into a new status ('shipped' or 'completed') 
 * - move back to the previous view (RiderHomeView)
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
    private JButton backBtn;
    
    private JTable dishTable;
    
    
    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator navigator;
    
    private Long riderId;
    private Long orderId;
    
    /**
     * * Initialize:
      * - the view and its components;
      * - the retrofitBuilder and the serviceApi in order to invoke the API calls.
      * - the Navigator to switch between views
      * - attach listeners to buttons in order to manage events
      * 
     * @param riderId is the rider ID that uniquely identifies the rider 
     * @param orderId is the order ID that uniquely identifies the order 
     */
    public RiderOrderController(Long riderId, Long orderId){
        this.riderId = riderId;
        this.orderId = orderId;
        
        orderView = new DeliveryOrderView();
        orderView.setVisible(true);
        providerNameLabel = orderView.getProviderNameLabel();
        providerAddressLabel = orderView.getProviderAddressLabel();
        providerTelLabel = orderView.getProviderTelephoneLabel();
    
        customerNameLabel = orderView.getCustomerNameLabel();
        customerAddressLabel = orderView.getCustomerAddressLabel();
        customerTelLabel = orderView.getCustomerTelephoneLabel();

        deliveryTimeLabel = orderView.getDeliveryTimeLabel();
        backBtn = orderView.getBackBtn();
        orderStateLabel = orderView.getStateLabel();

        shipOrderBtn = orderView.getShippedBtn();
        shipOrderBtn.setEnabled(false);
        completeOrderBtn = orderView.getCompleteBtn();
        completeOrderBtn.setEnabled(false);
        
        dishTable = orderView.getDishTable();
        
        
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);
        
        navigator = Navigator.getInstance();
        
        //Add listeners
        shipOrderBtn.addMouseListener(shipOrderAction);        
        completeOrderBtn.addMouseListener(completeOrderAction);
        backBtn.addMouseListener(backToOrdersAction);
        
        fillOrderLabels();
        
        
    }
    
    /**
     * This method allows to fill the fields of the text labels and of the table in the view with
     * the current order informations.
     * The method will call an asynchronous api to get the order from server.
     */
    private void fillOrderLabels(){
        
        //API CALL
             Call<OrderDto> call = apiService.getOrderById(this.orderId);
        call.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                    OrderDto orderDto = response.body();
                    
                    //FILL FIELDS
                    providerNameLabel.setText(orderDto.getProvider().getProviderName());
                    providerAddressLabel.setText(orderDto.getProvider().getAddress());
                    providerTelLabel.setText(orderDto.getProvider().getTelephoneNumber());
                    
                    customerNameLabel.setText(orderDto.getCustomer().getName()+" "+orderDto.getCustomer().getSurname());
                    customerAddressLabel.setText(orderDto.getCustomer().getAddress());
                    customerTelLabel.setText(orderDto.getCustomer().getTelephoneNumber());
                    DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm");
                     
                    orderStateLabel.setText("Order state: "+orderDto.getOrderState());
                    
                    // Disabling/enabling buttons following theorder-status path: ACCEPTED->SHIPPED->COMPLETED
                    if(orderDto.getOrderState().toString().equals("ACCEPTED") ){
                        shipOrderBtn.setEnabled(true);
                        completeOrderBtn.setEnabled(false);
                    }
                    
                    else if(orderDto.getOrderState().toString().equals("SHIPPED")){
                        shipOrderBtn.setEnabled(false);
                        completeOrderBtn.setEnabled(true);
                    }
                    
                    else {
                        shipOrderBtn.setEnabled(false);
                        completeOrderBtn.setEnabled(false);
                    }
                    
                    List<DishOrderAssociation> associations = orderDto.getDishOrderAssociations();
                    DefaultTableModel dishTableModel = (DefaultTableModel) dishTable.getModel();
                    dishTableModel.setRowCount(0);
                    DishEntity dish;
                    Object row[] = new Object[2];
                    for (DishOrderAssociation ass : associations) {
                        ass.getQuantity();
                        dish = ass.getDish();
                        row[0] = dish.getName();
                        row[1] = ass.getQuantity();
                        dishTableModel.addRow(row);
                    }
                    dishTable.setModel(dishTableModel);                
                     
                     
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
        
    /**
     * It's a mouse listener that allows you to change the order state in 'shipped'.
     * The method call an asynchronous API to change the order state.
     */
    private MouseAdapter shipOrderAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
            if(!shipOrderBtn.isEnabled())
                return;
            
            //API CALL
             Call<Void> call2 = apiService.putOrderOnShipped(orderId);
             
              call2.enqueue(new Callback<Void>() {
                  
                   @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                        fillOrderLabels();
                          JOptionPane.showMessageDialog(orderView, "Order shipped succesfully", "Order shipped", JOptionPane.INFORMATION_MESSAGE);
                      }
                  
                else { // if server error occurs
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
            public void onFailure(Call<Void> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(orderView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

            }
                  
              });
          
        
            
        }};
        
    /**
     * It's a mouse listener that allows you to change the order state in 'comlpeted'.
     * The method call an asynchronous API to change the order state.
     */
    private MouseAdapter completeOrderAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            
             if(!completeOrderBtn.isEnabled())
                return;
            
             //API CALL
             Call<Void> call2 = apiService.completeOrder(orderId);
             
              call2.enqueue(new Callback<Void>() {
                  
                   @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                        fillOrderLabels();
                        JOptionPane.showMessageDialog(orderView, "Order completed succesfully", "Order completed", JOptionPane.INFORMATION_MESSAGE);
                      }
                  
                 else { // if server error occurs
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
            public void onFailure(Call<Void> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(orderView,
                                "Contact you system administrator",
                                "CRITICAL ERROR",
                                JOptionPane.ERROR_MESSAGE);

            }
                  
              });
          
        
        
        }
      };
      
    /**
     * It's a mouse listener that allows you to go back to the previous view RiderHomeView.
     * The method use a method of the navigator to perform the view-switch.
     */
    private MouseAdapter backToOrdersAction = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            navigator.fromOrderToHome(RiderOrderController.this);
        }
        };

    public Long getRiderId() {
        return riderId;
    }
      
}
