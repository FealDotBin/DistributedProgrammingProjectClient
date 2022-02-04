/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.model.dao.order.DishEntity;
import com.mycompany.common.model.dao.order.DishOrderAssociation;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.RiderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.CustomerFrameHistory1;
import com.mycompany.customerclient.view.MoreOrderInformation;
import com.sun.tools.javac.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author CATELLO
 * This class represents MoreOrderInformation GUI controller.
 * This class is responsible for capturing input from the user and updating the view.
 * It also makes calls to the restfull server when needed:
 *  -To decide which controller to invoke after the home button has been pressed
 */
public class OrderMoreInformationController {

    private MoreOrderInformation moreInformationView;

    private JButton updateButton;
    private JButton balanceButton;
    private JButton homeButton;
    private JButton logOutButton;
    private JButton backButton;
    private JTextField deliveryField;
    private JTextField orderTypeField;
    private JTextField orderStateField;
    private JTextField providerField;
    private JTextField riderField;
    private JTextField totalField;
    private JTextArea dishDescriptionTextArea;
    private JTextArea dishIngredientTextArea;
    private JTable dishTable;

    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;
    private Navigator nav;

    private Long customerId;
    private OrderDto selectedOrder;
    private OrderDto currentOrder;

    /**
     * Initialize view's obtaing customer past selected order information.
     * Initialize dish table with dishes selected in the past by the customer
     * Add listeners to other buttons to navigate from this controller to the next
     * @param customerId represents the logged customer's id
     * @param selectedOrder represents the order which you want to view more information
     */
    public OrderMoreInformationController(Long customerId, OrderDto selectedOrder) {
        this.customerId = customerId;
        this.selectedOrder = selectedOrder;
        //View creation
        moreInformationView = new MoreOrderInformation();
        //Component getter
        updateButton = moreInformationView.getAccountBtn();
        logOutButton = moreInformationView.getLogOutBtn();
        balanceButton = moreInformationView.getBalanceBtn();
        homeButton = moreInformationView.getHomeBtn();
        backButton = moreInformationView.getBackBtn();
        deliveryField = moreInformationView.getDeliveryField();
        orderStateField = moreInformationView.getStateField();
        orderTypeField = moreInformationView.getOrderTypeField();
        providerField = moreInformationView.getProviderField();
        riderField = moreInformationView.getRiderField();
        totalField = moreInformationView.getTotalTextField();
        dishDescriptionTextArea = moreInformationView.getDishDescriptionTextArea();
        dishIngredientTextArea = moreInformationView.getDishIngredientTextArea();
        dishTable = moreInformationView.getDishTable();

        //View navigator creation
        nav = Navigator.getInstance();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);

        //When logout button is pressed logIn view is displayed
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromMoreInfoToLogIn(OrderMoreInformationController.this);
            }
        });

        //When update account button is pressed update customer information view is displayed
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromMoreInfoToUpdate(OrderMoreInformationController.this);
            }
        });

        //When balance button is pressed balance update view is displayed
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromMoreInfoToBalance(OrderMoreInformationController.this);
            }
        });

        //When back button is pressed, order history view si displayed
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromMoreInfoToHistory(OrderMoreInformationController.this);
            }
        });

        //Set home button text and his action listener based on the presence of current order
        addHomeButtonLogic();
        
        //Fill dish table
        List<DishOrderAssociation> associations = selectedOrder.getDishOrderAssociations();
        DefaultTableModel dishTableModel = (DefaultTableModel) dishTable.getModel();
        DishEntity dish;
        Object row[] = new Object[3];
        for (DishOrderAssociation ass : associations) {
            ass.getQuantity();
            dish = ass.getDish();
            row[0] = dish.getName();
            row[1] = dish.getPrice();
            row[2] = ass.getQuantity();
            dishTableModel.addRow(row);
        }
        
        //fill information field about selected order
        Double orderPrice = selectedOrder.getPrice();
        totalField.setText(orderPrice.toString());
        providerField.setText(selectedOrder.getProvider().getProviderName());
        RiderDto rider = selectedOrder.getRider();
        String riderName = "";
        if (rider != null) {
            riderName = rider.getName();
        } else {
            riderName = "";
        }
        riderField.setText(riderName);
        deliveryField.setText(selectedOrder.getDeliveryTime());
        orderStateField.setText(selectedOrder.getOrderState().toString());
        orderTypeField.setText(selectedOrder.getOrderType().toString());

        //When a dish in the table are clicked more informations are showd in below text areas
        dishTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                dishTableRowClicked(event, associations);
            }
        });

        moreInformationView.setVisible(true);

    }
    
    /**
     * The method shows in the appropriate text area the description and the list of ingredients corresponding to the dish selected in the table.
     * @param event represents the event fired when the selected row on the table changes.
     * @param associations list of associations between order and dishes
     */
    private void dishTableRowClicked(ListSelectionEvent event, List<DishOrderAssociation> associations){
        if (!event.getValueIsAdjusting()) {
            int selectedRow = dishTable.getSelectedRow();
            if (selectedRow >= 0) {
                DishEntity selectOrder = associations.get(selectedRow).getDish();
                dishDescriptionTextArea.setText(selectOrder.getDescription());
                dishIngredientTextArea.setText(selectOrder.getIngredients().toString());

            } else {
                dishDescriptionTextArea.setText("");
                dishIngredientTextArea.setText("");
            }
        }
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
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {

                    currentOrder = response.body();
                    homeButton.setText("Current Order");
                    homeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            changeToHome();
                        }

                    });
                } else {
                    homeButton.setText("Create Order");
                    homeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            nav.fromMoreInfoToProviderSelection(OrderMoreInformationController.this);
                        }

                    });
                    
                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(moreInformationView,
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
     * In case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void changeToHome() {
        Call<OrderDto> currentOrderCall = apiService.getCurrentOrderDTO(OrderMoreInformationController.this.customerId);
        currentOrderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    currentOrder = response.body();
                    nav.fromMoreInfoToOrderViewer(OrderMoreInformationController.this);
                } else {
                    getOrderById();
                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(moreInformationView,
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
        Call<OrderDto> getOrderById = apiService.getOrderDTO(OrderMoreInformationController.this.currentOrder.getId());
        getOrderById.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) {
                    OrderDto orderGhost = response.body();
                    if (orderGhost.getOrderState().equals(OrderState.REFUSED)) {
                        JOptionPane.showMessageDialog(moreInformationView, "YOUR CURRENT ORDER HAVE BEEN REFUSED BUT YOU GOT: " + orderGhost.getPrice() + " MONEY BACK", "Order Refused", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(moreInformationView, "YOUR CURRENT ORDER HAVE BEEN COMPLETED", "Order Completed", JOptionPane.INFORMATION_MESSAGE);
                    }
                    File file = new File("customer"+customerId+"/persistentOrder.txt");
                    if (file.delete()) {
                        System.out.println("File cancellato con successo");
                    } else {
                        System.out.println("Problemi con la cancellazione del file");
                    }
                    nav.fromMoreInfoToProviderSelection(OrderMoreInformationController.this);
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(moreInformationView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable thrwbl) {
                JOptionPane.showMessageDialog(moreInformationView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }

        });
    }

    /**
     * Remove the updateBalance view from the visualization
     */
    public void disposeView() {
        moreInformationView.dispose();
    }

    /**
     * Return the logged customer's id
     * @return String that represent the logged customer's id 
     */
    public Long getCustomerId() {
        return customerId;
    }

}
