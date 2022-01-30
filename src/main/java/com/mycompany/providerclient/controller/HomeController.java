/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller;

import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateCompleted;
import com.mycompany.common.components.NoEditableTableModel;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderState;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateAccepted;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateNotSelected;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStatePending;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateRefused;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateSemiAccepted;
import com.mycompany.providerclient.controller.selectedorderstate.SelectedOrderStateShipped;
import com.mycompany.providerclient.navigator.Navigator;
import com.mycompany.providerclient.view.HomeView;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author aferr
 */
public class HomeController {

    private HomeView homeView;
    private JButton acceptBtn;
    private JButton shipBtn;
    private JButton completeBtn;
    private JButton refuseBtn;
    private JButton refreshBtn;
    private JButton logOutBtn;
    private JButton availableBtn;
    private JTable allOrdersTable;
    private JTable selectedOrderTable;
    private Long providerId;
    private LinkedList<Order> orderList;
    private SelectedOrderState selectedOrderState;
    private Order selectedOrder;
    private Navigator navigator;

    public HomeController(Long providerId) {
        this.providerId = providerId;
        
        // initialize view
        homeView = new HomeView();
        homeView.setVisible(true);
        
        // get view components
        acceptBtn = homeView.getAcceptBtn();
        shipBtn = homeView.getShipBtn();
        completeBtn = homeView.getCompleteBtn();
        refuseBtn = homeView.getRefuseBtn();
        refreshBtn = homeView.getRefreshBtn();
        logOutBtn = homeView.getLogOutBtn();
        availableBtn = homeView.getAvailableBtn();
        allOrdersTable = homeView.getAllOrdersTable();
        selectedOrderTable = homeView.getSelectedOrderTable();
        
        // disable all buttons
        acceptBtn.setEnabled(false);
        shipBtn.setEnabled(false);
        completeBtn.setEnabled(false);
        refuseBtn.setEnabled(false);
        
        // initialize orderList
        orderList = new LinkedList<>();
        initOrderList();
        
        // initialize selectedOrderState and selectedOrder
        selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        selectedOrder = null;
        
        // show orders on allOrdersTable
        NoEditableTableModel allOrdersTableModel = (NoEditableTableModel) allOrdersTable.getModel();
        for(Order order : orderList){
            Object[] orderRow = new Object[5];
            orderRow[0] = order.customerName;
            orderRow[1] = order.riderName;
            orderRow[2] = order.orderType;
            orderRow[3] = order.orderState;
            orderRow[4] = order.deliveryTime;
            allOrdersTableModel.addRow(orderRow);
        }
        
        // get navigator
        navigator = Navigator.getInstance();
        
        // attach listener on allOrdersTable to update selectedOrderTable and
        // all associated buttons
        allOrdersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            
            @Override
            public void valueChanged(ListSelectionEvent event) {
                
                int selectedOrderIndex = allOrdersTable.getSelectedRow();
                if(!event.getValueIsAdjusting() && selectedOrderIndex >= 0){
                    NoEditableTableModel selectedOrderTableModel = (NoEditableTableModel) selectedOrderTable.getModel();
                    selectedOrderTableModel.setRowCount(0);
                    
                    // get selected Order
                    selectedOrder = orderList.get(selectedOrderIndex);
                    List<Dish> dishList = selectedOrder.getDishList();
                    
                    // put order's info in selectedOrderTable
                    for(Dish dish : dishList){
                        Object[] orderRow = new Object[2];
                        orderRow[0] = dish.getName();
                        orderRow[1] = dish.getQuantity();
                        selectedOrderTableModel.addRow(orderRow);
                    }
                    
                    // change current state based on selected order
                    String orderState = selectedOrder.getOrderState();
                    switch(orderState){
                        case "PENDING":
                            selectedOrderState = new SelectedOrderStatePending(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case "SEMI_ACCEPTED":
                            selectedOrderState = new SelectedOrderStateSemiAccepted(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case "ACCEPTED":
                            selectedOrderState = new SelectedOrderStateAccepted(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case "SHIPPED":
                            selectedOrderState = new SelectedOrderStateShipped(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case "COMPLETED":
                            selectedOrderState = new SelectedOrderStateCompleted(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        case "REFUSED":
                            selectedOrderState = new SelectedOrderStateRefused(homeView, selectedOrder, selectedOrderIndex);
                            break;
                        default:
                            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
                    }
                    selectedOrderState.updateButton();
                }
            }
        });
        
        // attach listener to accept button
        acceptBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            String orderType = selectedOrder.getOrderType();
            
            if(!orderType.equals("TAKE_AWAY")){
                // ask user if he wants to use riders
                int wantRiders = JOptionPane.showConfirmDialog(homeView,
                    "Do you want to use our riders?", 
                    "Question", 
                    JOptionPane.YES_NO_OPTION);
                
                // update order's type according to user response
                if(wantRiders == JOptionPane.YES_OPTION){
                    selectedOrder.setOrderType("DELIVERY_RIDERS");
                }
                else if(wantRiders == JOptionPane.NO_OPTION) {
                    selectedOrder.setOrderType("DELIVERY_NO_RIDER");
                }
                else {
                    return;
                }
            }
            selectedOrderState.accept();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
        
        // attach listener to ship button
        shipBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.ship();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
        
        // attach listener to complete button
        completeBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.complete();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
        
        // attach listener to refuse button
        refuseBtn.addActionListener((event) -> {
            // <- qua ci va il codice della richiesta al server
            // se va bene mi cambio lo stato
            // se va male non faccio niente
            selectedOrderState.refuse();
            selectedOrderState = new SelectedOrderStateNotSelected(homeView);
        });
        
        // attach listener to log out button
        logOutBtn.addActionListener((event) -> {
            navigator.fromHomeToLogIn(HomeController.this);
        });
        
        // attach listener to refresh button
        refreshBtn.addActionListener((event) -> {
            // <- qua ci devo mettere il codice per resettare alcune
            // robe, tipo tabelle
            
            // <- qua ci va il codice per ricaricare 
            // gli ordini dal backend
        });
    }
    
    public void disposeView(){
        homeView.dispose();
    }

    public void initOrderList(){
        // ORDER 1 - DISH 1
        LinkedList<String> ingredients11 = new LinkedList<>();
        ingredients11.add("in11");
        ingredients11.add("in11+");
        ingredients11.add("in11++");
        Dish dish11 = new Dish(11, "dish11", "dish11", ingredients11, 1);
        
        // ORDER 1 - DISH 2
        LinkedList<String> ingredients12 = new LinkedList<>();
        ingredients12.add("in12");
        ingredients12.add("in12+");
        ingredients12.add("in12++");
        Dish dish12 = new Dish(12, "dish12", "dish12", ingredients12, 2);
        
        // ORDER 1 - ADD DISHES
        LinkedList<Dish> dishList1 = new LinkedList<>();
        dishList1.add(dish11);
        dishList1.add(dish12);
        Order order1 = new Order("customer1", "", "TAKE_AWAY", "PENDING",
                "29-01-22-13:30", dishList1);
        orderList.add(order1);
        
        // ORDER 2 - DISH 1
        LinkedList<String> ingredients21 = new LinkedList<>();
        ingredients11.add("in21");
        ingredients11.add("in21+");
        ingredients11.add("in21++");
        Dish dish22 = new Dish(21, "dish21", "dish21", ingredients21, 1);
        
        // ORDER 2 - DISH 2
        LinkedList<String> ingredients22 = new LinkedList<>();
        ingredients12.add("in22");
        ingredients12.add("in22+");
        ingredients12.add("in22++");
        Dish dish21 = new Dish(22, "dish22", "dish22", ingredients12, 2);
        
        // ORDER 2 - ADD DISHES
        LinkedList<Dish> dishList2 = new LinkedList<>();
        dishList2.add(dish21);
        dishList2.add(dish22);
        Order order2 = new Order("customer2", "", "DELIVERY", "PENDING", 
                "29-01-22-13:30", dishList2);
        orderList.add(order2);
        
        // ORDER 3 - DISH 1
        LinkedList<String> ingredients31 = new LinkedList<>();
        ingredients31.add("in31");
        ingredients31.add("in31+");
        ingredients31.add("in31++");
        Dish dish31 = new Dish(31, "dish31", "dish31", ingredients31, 1);
        
        // ORDER 3 - DISH 2
        LinkedList<String> ingredients32 = new LinkedList<>();
        ingredients32.add("in32");
        ingredients32.add("in32+");
        ingredients32.add("in32++");
        Dish dish32 = new Dish(32, "dish32", "dish32", ingredients32, 2);
        
        // ORDER 3 - ADD DISHES
        LinkedList<Dish> dishList3 = new LinkedList<>();
        dishList3.add(dish31);
        dishList3.add(dish32);
        Order order3 = new Order("customer3", "", "DELIVERY", "PENDING",
                "29-01-22-13:30", dishList3);
        orderList.add(order3);
    }
    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HomeController c = new HomeController(1L);
            }
        });
    }

    /**
     * DISH
     */
    private class Dish{
        
        private int id;
        private String name;
        private String description;
        private List<String> ingredients;
        private int quantity;
        
        public Dish(int id, String name, String description, List<String> ingriedients, int quantity){
            this.id = id;
            this.name = name;
            this.description = description;
            this.ingredients = ingriedients;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getIngredients() {
            return ingredients;
        }
        
        public int getQuantity(){
            return quantity;
        }
    }
    
    /**
     * ORDER 
     */
    public class Order{
        
        private String customerName;
        private String riderName;
        private String orderType;
        private String orderState;
        private String deliveryTime;
        private List<Dish> dishList;
        
        public Order(String customerName, String riderName, String orderType,
                String orderState, String deliveryTime, List<Dish> dishList){
            this.customerName = customerName;
            this.riderName = riderName;
            this.orderType = orderType;
            this.orderState = orderState;
            this.deliveryTime = deliveryTime;
            this.dishList = dishList;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getRiderName() {
            return riderName;
        }

        public String getOrderType() {
            return orderType;
        }

        public String getOrderState() {
            return orderState;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public List<Dish> getDishList() {
            return dishList;
        }

        public void setRiderName(String riderName) {
            this.riderName = riderName;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public void setOrderState(String orderState) {
            this.orderState = orderState;
        }
        
    }
    
}
