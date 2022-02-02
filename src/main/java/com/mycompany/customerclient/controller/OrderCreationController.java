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

    public OrderCreationController(Long customerId, ProviderDto selectProvider) {
        this.customerId = customerId;
        this.selectedProvider = selectProvider;
        MenuEntity menu = selectProvider.getMenu();
        providerDishList = menu.getDishEntities();
        cart = new LinkedList<>();
        orderCreationView = new orderCreation();
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
        total = 0.0;

        nav = Navigator.getInstance();

        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        apiService = retroBuild.getRetrofit().create(ServiceApi.class);

        DefaultTableModel menuTableModel = (DefaultTableModel) menuTable.getModel();
        for (DishEntity dish : providerDishList) {
            Object[] row = new Object[3];
            row[0] = dish.getName();
            row[1] = dish.getPrice();
            row[2] = new JButton();
            menuTableModel.addRow(row);
        }
        
        //Auto setting delivery mode if provider does't support take away or home delivery
        if(selectedProvider.getDoDelivering() && (!selectProvider.getDoTakeAway())){
            homeDeliveryButton.setSelected(true);
            homeDeliveryButton.setEnabled(false);
        }
        if(selectedProvider.getDoTakeAway() && (!selectedProvider.getDoDelivering())){
            takeAwayButton.setSelected(true);
            takeAwayButton.setEnabled(false);
        }
        
        //Retrive costumer info from server
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

        JButtonEditor menuButtonEditor = new JButtonEditor("Add to your order", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
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
        });
        menuTable.setDefaultEditor(JButton.class, menuButtonEditor);

        JButtonDeleteItemEditor cartButtonEditor = new JButtonDeleteItemEditor("Remove from your order", new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = cartTable.getSelectedRow();
                cart.remove(selectedRow);
                JButtonDeleteItemEditor editor = (JButtonDeleteItemEditor) cartTable.getDefaultEditor(JButton.class);
                total -= Double.parseDouble(cartTable.getValueAt(selectedRow, 1).toString());
                totalField.setText(total.toString());
                editor.setIsButtonPressed();
                editor.stopCellEditing();

            }

        });
        cartTable.setDefaultEditor(JButton.class, cartButtonEditor);

        menuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = menuTable.getSelectedRow();
                    System.out.println("Riga menu" + menuTable.getSelectedRow());
                    DishEntity selectOrder = providerDishList.get(selectedRow);
                    dishDescription.setText(selectOrder.getDescription());
                    dishIngredient.setText(selectOrder.getIngredients().toString());
                    menuLastSelectionRow = selectedRow;
                }
            }
        });

        cartTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row

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
        });

        //Action performed when confirm button is pressed: read necessary field, validate them and make a request fot the server
        //to create the order
        confirmOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    fieldErrorPane("Birth date cannot be blank");
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

                Call<OrderDto> orderCall = apiService.createOrder(order);
                orderCall.enqueue(new Callback<OrderDto>() {
                    @Override
                    public void onResponse(Call<OrderDto> call, Response<OrderDto> response) {
                        if (response.isSuccessful()) { // status code tra 200-299
                            JOptionPane.showMessageDialog(orderCreationView, "ORDER CREATED", "ORDER CREATE SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                            ObjectOutputStream file = null;
                            try {
                                file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("persistentOrder.txt")));
                                file.writeObject(response.body());
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(OrderCreationController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(OrderCreationController.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                try {
                                    if(file!=null)
                                    file.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(CustomerLogInController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

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

        }
        );

        balanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderCreationToBalance(OrderCreationController.this);
            }

        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderCreationToUpdate(OrderCreationController.this);
            }

        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nav.fromOrderCreationToLogOut(OrderCreationController.this);
            }

        });

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

    public void disposeView() {
        this.orderCreationView.dispose();
    }

    public Long getCustomerId() {
        return customerId;
    }

    private void fieldErrorPane(String errorMessage) {
        JOptionPane.showMessageDialog(orderCreationView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    /*
    public static void main(String args[]){
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OrderCreationController c = null;
                c = new OrderCreationController();
            }
        });
        
    }
     */
}
