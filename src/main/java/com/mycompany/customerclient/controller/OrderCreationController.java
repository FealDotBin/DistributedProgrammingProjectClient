/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JButtonDeleteItemEditor;
import com.mycompany.common.components.JButtonEditor;
import com.mycompany.common.model.dao.order.DishEntity;
import com.mycompany.common.model.dao.order.DishOrderAssociation;
import com.mycompany.common.model.dao.order.MenuEntity;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.CustomerDto;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.common.model.enumerations.OrderState;
import com.mycompany.common.model.enumerations.OrderType;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.model.CustomerEntity;
import com.mycompany.customerclient.model.OrderEntity;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.orderCreation;
import com.sun.tools.javac.Main;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
 * -To obtain selected provider menu
 * -To obtain customer's informations needed to place an order
 * -To create an order
 * 
 */
public class OrderCreationController {

    private JTable menuTable;
    private JTable cartTable;
    private JTextArea dishDescription;
    private JTextArea dishIngredient;
    private orderCreation orderCreationView;
    private int menuLastSelectionRow;
    private int cartLastSelectionRow;
    private JTextField totalField;
    private Double total;
    private JButton confirmOrderButton;
    private JButton updateButton;
    private JButton historyButton;
    private JButton balanceButton;
    private JButton logOutButton;
    private JButton goBackButton;
    private JRadioButton homeDeliveryButton;
    private JRadioButton takeAwayButton;
    private JDateChooser deliveryDateChooser;
    private JComboBox hourComboBox;
    private JComboBox minComboBox;
    private List<DishEntity> providerDishList;
    private List<DishEntity> cart;
    private ProviderDto selectedProvider;
    private CustomerEntity customer;
    private Long customerId;

    private Navigator nav;

    private RetrofitBuilder retroBuild;
    private ServiceApi apiService;

    /**
     * Initialize dish table obtaing selected provider's menù.
     * Handles adding and deleting dishes from the cart
     * Send a order creation request to the server filled with information taken from view
     * Add listeners to other buttons to navigate from this controller to the next 
     * @param customerId represents the logged customer's id
     * @param selectProvider object that represent provider where customer want place an order
     */
    public OrderCreationController(Long customerId, ProviderDto selectProvider) {
        //Variable initialization
        this.customerId = customerId;
        this.selectedProvider = selectProvider;
        MenuEntity menu = selectProvider.getMenu();
        providerDishList = menu.getDishEntities();
        cart = new LinkedList<>();
        total = 0.0;
        
        //View creation
        orderCreationView = new orderCreation();
        
        //Component getter
        menuTable = orderCreationView.getMenuTable();
        cartTable = orderCreationView.getCartTable();
        dishDescription = orderCreationView.getDishDescriptionTextArea();
        dishIngredient = orderCreationView.getDishIngredientTextArea();
        totalField = orderCreationView.getTotalField();
        confirmOrderButton = orderCreationView.getConfirmOrderBtn();
        homeDeliveryButton = orderCreationView.getHomeDeliveryRadioButton();
        takeAwayButton = orderCreationView.getTakeAwayRadioButton();
        updateButton = orderCreationView.getUpdateBtn();
        historyButton = orderCreationView.getHistoryBtn();
        balanceButton = orderCreationView.getBalanceBtn();
        logOutButton = orderCreationView.getLogOutBtn();
        goBackButton = orderCreationView.getGoBackBtn();
        deliveryDateChooser = orderCreationView.getDeliveryDateChooser();
        hourComboBox = orderCreationView.getHourComboBox();
        minComboBox = orderCreationView.getMinComboBox();
        
        
        //get navigator
        nav = Navigator.getInstance();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);
        
        //Fill menù table with dish contained in selected provider's menù
        DefaultTableModel menuTableModel = (DefaultTableModel) menuTable.getModel();
        for (DishEntity dish : providerDishList) {
            Object[] row = new Object[3];
            row[0] = dish.getName();
            row[1] = dish.getPrice();
            row[2] = new JButton();
            menuTableModel.addRow(row);
        }

        //Auto setting delivery mode if provider does't support take away or home delivery
        if (selectedProvider.getDoDelivering() && (!selectProvider.getDoTakeAway())) {
            homeDeliveryButton.setSelected(true);
            homeDeliveryButton.setEnabled(false);
            takeAwayButton.setEnabled(false);
        }
        if (selectedProvider.getDoTakeAway() && (!selectedProvider.getDoDelivering())) {
            takeAwayButton.setSelected(true);
            takeAwayButton.setEnabled(false);
            homeDeliveryButton.setEnabled(false);
        }

        //Retrive costumer info from server
        getCustomerById();

        JButtonEditor menuButtonEditor = new JButtonEditor("Add to your order", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                addMenuTableButtonLogic(ae);
            }
        });
        menuTable.setDefaultEditor(JButton.class, menuButtonEditor);

        //Add button logic in cart table for deleting element from the cart
        JButtonDeleteItemEditor cartButtonEditor = new JButtonDeleteItemEditor("Remove from your order", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addCartTableButtonLogic();
            }

        });
        cartTable.setDefaultEditor(JButton.class, cartButtonEditor);
        
        //when menu table row is selected write more information about the dishes in the corrisponding text areas
        menuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                manuTableRowClicked(event);
            }
        });
        
        //when menu table row is selected write more information about the dishes in the corrisponding text areas
        cartTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                cartTableRowClicked(event);
            }
        });

        //Action performed when confirm button is pressed: read necessary field, validate them and make a request fot the server
        //to create the order
        confirmOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmOrder();
            }

        });
        
        // When balance button is pressed display balance manager view
        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderCreationToBalance(OrderCreationController.this);
            }

        });
        
        // When update button is pressed display account update view
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderCreationToUpdate(OrderCreationController.this);
            }

        });
        
         // When log out button is pressed display log in view
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderCreationToLogOut(OrderCreationController.this);
            }

        });
        
        // When back button is pressed display provider selection view
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderCreationToProviderSelection(OrderCreationController.this);
            }

        });

        // When history button is pressed display customer order history view
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderCreationtoHistory(OrderCreationController.this);
            }

        });

        orderCreationView.setVisible(
                true);
    }
    /**
     * The method sends a GET to the server to retrieve more information about the logged user
     * In case of a communication error with the server, messages are displayed to inform the customer.
     */
    private void getCustomerById(){
         Call<CustomerEntity> call = apiService.getCustomer(customerId);
        call.enqueue(new Callback<CustomerEntity>() {
            @Override
            public void onResponse(Call<CustomerEntity> call, Response<CustomerEntity> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                    OrderCreationController.this.customer = response.body();

                } else { // if server error occurs
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(orderCreationView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerEntity> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(orderCreationView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);

            }

        });
    }
    /**
     * The method sets the appearance of the button after it is pressed.
     * Then he gets from the menu of the selected provider the dish desired by the customer based on the line where the button pressed is located.
     *Now the method adds that dish to the cart and its cost to the total.
     *Finally it updates the view showing the cart total dynamically and adds a row with the chosen dish to the cart table.
     * @param ae event that occur when a button in menu table has been presse
     */
    private void addMenuTableButtonLogic(ActionEvent ae){
        JButton button = (JButton) ae.getSource();
        button.setBackground(new java.awt.Color(44, 73, 129));
        button.setFont(new java.awt.Font("Segoe UI", 1, 14));
        button.setForeground(new Color(200, 200, 200));
        int selectedRow = menuTable.getSelectedRow();
        String dishName = (String) menuTable.getValueAt(selectedRow, 0);
        Double dishPrice = (Double) menuTable.getValueAt(selectedRow, 1);
        DefaultTableModel cartTableModel = (DefaultTableModel) cartTable.getModel();
        cartTableModel.addRow(new Object[]{dishName, dishPrice, new JButton()});
        DishEntity selectedDish = providerDishList.get(selectedRow);
        cart.add(selectedDish);
        total += Double.parseDouble(menuTable.getValueAt(selectedRow, 1).toString());
        totalField.setText(total.toString());
        System.out.println("SelecetedRow" + selectedRow);
    }
    
    /**
     *The method obtains from the selected trolley the dish that the customer wishes to eliminate based on the row where the button pressed is located.
     *Now the method removes that plate from the cart and subtracts its cost from the total.
     *Finally, it updates the view showing the cart total dynamically and deletes the row with the chosen dish from the cart table.
     */
    private void addCartTableButtonLogic(){
        int selectedRow = cartTable.getSelectedRow();
        cart.remove(selectedRow);
        JButtonDeleteItemEditor editor = (JButtonDeleteItemEditor) cartTable.getDefaultEditor(JButton.class);
        total -= Double.parseDouble(cartTable.getValueAt(selectedRow, 1).toString());
        totalField.setText(total.toString());
        editor.setIsButtonPressed();
        editor.stopCellEditing();
    }
    /**
     * The method shows in the appropriate text area the description and the list of ingredients corresponding to the dish selected in the menu table.
     * @param event represents the event fired when the selected row on the table changes.
     */
    private void manuTableRowClicked(ListSelectionEvent event){
        if (!event.getValueIsAdjusting()) {
            int selectedRow = menuTable.getSelectedRow();
            DishEntity selectOrder = providerDishList.get(selectedRow);
            dishDescription.setText(selectOrder.getDescription());
            dishIngredient.setText(selectOrder.getIngredients().toString());
            menuLastSelectionRow = selectedRow;
        }
    }
    /**
     * The method shows in the appropriate text area the description and the list of ingredients corresponding to the dish selected in the cart table.
     * If no valid selection is made, the method clean both text areas
     * @param event represents the event fired when the selected row on the table changes.
     */
    private void cartTableRowClicked(ListSelectionEvent event){
        if (!event.getValueIsAdjusting()) {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow >= 0) {
                DishEntity selectOrder = cart.get(selectedRow);
                dishDescription.setText(selectOrder.getDescription());
                dishIngredient.setText(selectOrder.getIngredients().toString());
            } else {
                dishDescription.setText("");
                dishIngredient.setText("");
            }
        }
    }
    /**
    *The method ensures that all the information necessary to create a new order are present and have been entered correctly by the costumer.
    *After that, for each dish in the cart, it adds a dish to the order being created.
    *Now the method sends a POST to the server for order creation.
    *Finally the GUI to view information on the order just placed will be shown.
    *In case of a communication error with the server, messages are displayed to inform the customer.
    *If the request to the server is made successfully, the method locally saves a file containing the customer's new current order
    */
    private void confirmOrder(){
        if (cart.isEmpty()) {
            fieldErrorPane("You must select some dishes");
            return;
        }
        if (!homeDeliveryButton.isSelected() && !takeAwayButton.isSelected()) {
            fieldErrorPane("You must specify if you want an home delivery or take away");
            return;
        }
        Date deliveryDate = deliveryDateChooser.getDate();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDeliveryDate = df.format(deliveryDate);
        if (formattedDeliveryDate.isBlank()) {
            fieldErrorPane("Delivery date cannot be blank");
            return;
        }
        String hour = hourComboBox.getSelectedItem().toString();
        String min = minComboBox.getSelectedItem().toString();
        formattedDeliveryDate += "T" + hour + ":" + min + ":00.000";

        List<DishOrderAssociation> assList = new LinkedList<>();
        DishOrderAssociation newAss;
        DishOrderAssociation oldAss = null;
        int index;
        for (DishEntity selectedDish : cart) {
            newAss = new DishOrderAssociation(selectedDish);
            index = assList.indexOf(newAss);

            if (index >= 0) {
                oldAss = assList.get(index);
                oldAss.increaseQuantity();
            } else {
                assList.add(newAss);
            }
        }
        System.out.println(assList);

        OrderType orderType;
        if (homeDeliveryButton.isSelected()) {
            orderType = OrderType.DELIVERY;
        } else {
            orderType = OrderType.TAKE_AWAY;
        }

        OrderEntity order = new OrderEntity(assList, customer, selectedProvider, orderType, OrderState.PENDING, formattedDeliveryDate, total);
        orderCreationRequest(order);
    }
    /**
     * The method sends a POST to the server to create a new order with the information contained in the input parameter.
     * Then the GUI to view information on the order just placed will be shown.
     * In case of a communication error with the server, messages are displayed to inform the customer.
     * @param order object that contains all the informations needed to create a new order
     */
    private void orderCreationRequest(OrderEntity order){
        Call<OrderDto> orderCall = apiService.createOrder(order);
        orderCall.enqueue(new Callback<OrderDto>() {
            @Override
            public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                if (response.isSuccessful()) { // status code tra 200-299
                    JOptionPane.showMessageDialog(orderCreationView, "ORDER CREATED", "ORDER CREATE SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                    ObjectOutputStream file = null;
                    File dir;
                    try {
                        String path = "customer" + customerId;
                        dir = new File(path);
                        if (dir.mkdir()) {
                            System.out.println("Nuova cartella creata");
                        } else {
                            System.out.println("Impossibile creare la cartella");
                        }
                        file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path + "/persistentOrder.txt")));

                        file.writeObject(response.body());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(OrderCreationController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(OrderCreationController.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            if (file != null) {
                                file.close();

                            }
                        } catch (IOException ex) {
                            Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    nav.fromOrderCreationToOrderViewer(OrderCreationController.this);

                } else { // if server error occurs
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(orderCreationView, jObjError.get("message"), "Server error", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderDto> call, Throwable t) {
                // Log error here since request failed
                JOptionPane.showMessageDialog(orderCreationView,
                        "Contact you system administrator",
                        "CRITICAL ERROR",
                        JOptionPane.ERROR_MESSAGE);

            }

        });
    }
    
    /**
     * Remove the orderCreation view from the visualization
     */
    public void disposeView() {
        this.orderCreationView.dispose();
    }
    
    /**
     * Return the logged customer's id
     * @return String that represent the logged customer's id 
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Method used to communicate to the customer an error in filling in the fields
     * @param errorMessage error message that is displayed to the customer
     */
    private void fieldErrorPane(String errorMessage) {
        JOptionPane.showMessageDialog(orderCreationView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    
}
