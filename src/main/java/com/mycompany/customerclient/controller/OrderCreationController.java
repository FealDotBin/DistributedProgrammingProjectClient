/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JButtonDeleteItemEditor;
import com.mycompany.common.components.JButtonEditor;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.orderCreation;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

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
    List<Order> orderList;
    List<Order> cartList;
    
    private class Order{
        private int id;
        private String description;
        private List<String> ingredients;
        private RetrofitBuilder retroBuild;
        private ServiceApi serviceApi;
        private Navigator nav;
        
        public Order(int id, String description, List<String> ingriedients){
            this.id = id;
            this.description = description;
            this.ingredients = ingriedients;
        }

        public int getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public List<String> getIngredients() {
            return ingredients;
        }
        
    }
    
    public OrderCreationController(){
        
        LinkedList<String> ingredient = new LinkedList<>(){
            @Override
            public String toString(){
                String acc = "";
                for(String elem : this)
                    acc+=elem+",\n";
                return acc;
            }
        };
        orderList = new LinkedList<>();
        cartList = new LinkedList<>();
        ingredient.add("Pummarola");
        ingredient.add("CalceStruzzo");
        orderList.add(new Order(0, "Bella fra", ingredient));
        ingredient.add("Zizzona di battipaglia");
        orderList.add(new Order(1, "Fratm pizzaiolo", ingredient));
        orderCreationView = new orderCreation();
        menuTable = orderCreationView.getMenuTable();
        cartTable = orderCreationView.getCartTable();
        dishDescription = orderCreationView.getDishDescriptionTextArea();
        dishIngredient = orderCreationView.getDishIngredientTextArea();
        totalField = orderCreationView.getTotalField();
        total = 0.0;
        JButtonEditor menuButtonEditor = new JButtonEditor("Add to your order", new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                JButton button = (JButton)ae.getSource();
                button.setBackground(new java.awt.Color(44, 73, 129));
                button.setFont(new java.awt.Font("Segoe UI", 1, 14));
                button.setForeground(new Color(200,200,200));
                int selectedRow = menuTable.getSelectedRow();
                String dishName = (String)menuTable.getValueAt(selectedRow, 0);
                String dishPrice = (String)menuTable.getValueAt(selectedRow, 1);
                DefaultTableModel model = (DefaultTableModel)cartTable.getModel();
                model.addRow(new Object[]{dishName, dishPrice, new JButton("Remove from your order")});
                Order selectedOrder = orderList.get(selectedRow);
                cartList.add(selectedOrder);
                total+= Double.parseDouble(cartTable.getValueAt(selectedRow, 1).toString());
                totalField.setText(total.toString());
                System.out.println("Aggiunto\n"+cartList);
            }
        });
        menuTable.setDefaultEditor(JButton.class,menuButtonEditor);
        
        JButtonDeleteItemEditor cartButtonEditor = new JButtonDeleteItemEditor("Remove from your order", new ActionListener(){
            
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = cartTable.getSelectedRow();
            cartList.remove(selectedRow);
            JButtonDeleteItemEditor editor = (JButtonDeleteItemEditor) cartTable.getDefaultEditor(JButton.class);
            total-= Double.parseDouble(cartTable.getValueAt(selectedRow, 1).toString());
            totalField.setText(total.toString());
            editor.setIsButtonPressed();
            editor.stopCellEditing();
            
        }
    
        });
        cartTable.setDefaultEditor(JButton.class,cartButtonEditor);
                
                
                /*new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                JButton button = (JButton)ae.getSource();
                button.setBackground(new java.awt.Color(44, 73, 129));
                button.setFont(new java.awt.Font("Segoe UI", 1, 14));
                button.setForeground(new Color(200,200,200));
                System.out.println("Sono ancora vivo");
                //super.actionPerformed(ae);
                System.out.println("Sono ancora vivo2");
                int selectedRow = cartTable.getSelectedRow();
                selectedRow=cartTable.convertRowIndexToModel(selectedRow);
                DefaultTableModel model = (DefaultTableModel)cartTable.getModel();
                System.out.println("Ho eliminato la riga: "+selectedRow);
                System.out.println("Indici prima: "+cartTable.getRowCount()+" "+(selectedRow));
                model.removeRow(selectedRow);
                System.out.println("Indici dopo: "+cartTable.getRowCount()+" "+(selectedRow));
                if(cartTable.getRowCount()>0 && cartTable.getRowCount()!=selectedRow){
                    System.out.println("Sono nell'if giusto");
                    cartTable.setRowSelectionInterval(selectedRow, selectedRow);
                    cartList.remove(selectedRow);
                   // System.out.println("Rimosso"+cartList);
                }
                if(cartTable.getRowCount()>0 && cartTable.getRowCount()==selectedRow){
                    System.out.println("Sono nel'if storto");
                    //cartTable.setRowSelectionInterval(selectedRow -1, selectedRow - 1);
                    cartTable.clearSelection();
                    cartList.remove(selectedRow);
                    
                    System.out.println("Nuova row selezionata"+cartTable.getSelectedRow());
                }
            }
        });
        //cartTable.setDefaultEditor(JButton.class,cartButtonEditor);
        /*menuTable.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = menuTable.getSelectedRow();
                Order selectOrder = orderList.get(selectedRow);
                dishDescription.setText(selectOrder.getDescription());
                dishIngredient.setText(selectOrder.getIngredients().toString());
                
            }
            
        });*/
        menuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent event) {
            // do some actions here, for example
            // print first column value from selected row
            if(!event.getValueIsAdjusting()){
                int selectedRow = menuTable.getSelectedRow();
                System.out.println("Riga menu"+menuTable.getSelectedRow());
                Order selectOrder = orderList.get(selectedRow);
                dishDescription.setText(selectOrder.getDescription());
                dishIngredient.setText(selectOrder.getIngredients().toString());
                menuLastSelectionRow = selectedRow;
            }
        }
    });
        
        
        cartTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        
        public void valueChanged(ListSelectionEvent event) {
            // do some actions here, for example
            // print first column value from selected row

            
            
            if(!event.getValueIsAdjusting()){
                int selectedRow = cartTable.getSelectedRow();
                if(selectedRow >= 0){
                    Order selectOrder = cartList.get(selectedRow);
                    dishDescription.setText(selectOrder.getDescription());
                    dishIngredient.setText(selectOrder.getIngredients().toString());
            
                }
                else{
                    dishDescription.setText("");
                    dishIngredient.setText("");
                }
            
            }
        }
        });
        
        orderCreationView.setVisible(true);
    }
    
    public void disposeView(){
        this.orderCreationView.dispose();
    }

    
    public static void main(String args[]){
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OrderCreationController c = null;
                c = new OrderCreationController();
            }
        });
        
    }
}
