/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JButtonEditor;
import com.mycompany.common.components.JButtonRenderer;
import com.mycompany.common.components.NoEditableTableModel;
import com.mycompany.common.components.NoEditableTableModelWithDelete;
import com.mycompany.customerclient.api.ServiceApi;
import com.mycompany.customerclient.navigator.Navigator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author CATELLO
 */
public class orderCreation extends javax.swing.JFrame {

    /**
     * Creates new form orderCreation
     */
    public orderCreation() {
        try {
            //   UIManager.
            UIManager.setLookAndFeel( new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(com.mycompany.customerclient.view.MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    public JTable getCartTable() {
        return cartTable;
    }

    public JTextArea getDishDescriptionTextArea() {
        return dishDescriptionTextArea;
    }

    public JTextArea getDishIngredientTextArea() {
        return dishIngredientTextArea;
    }

    public JTable getMenuTable() {
        return menuTable;
    }

    public JTextField getTotalField() {
        return totalField;
    }


    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        newViewMenu = new javax.swing.JPanel();
        goBackBtn = new javax.swing.JButton();
        confirmOrderBtn = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        cartTable = new javax.swing.JTable(){

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                    return String.class;
                    case 1:
                    return String.class;
                    case 2:
                    return JButton.class;
                    default:
                    return String.class;
                }
            }
        };
        jScrollPane12 = new javax.swing.JScrollPane();
        menuTable = new javax.swing.JTable(){

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                    return String.class;
                    case 1:
                    return String.class;
                    case 2:
                    return JButton.class;
                    default:
                    return String.class;
                }
            }
        };
        jScrollPane10 = new javax.swing.JScrollPane();
        dishDescriptionTextArea = new javax.swing.JTextArea();
        jScrollPane11 = new javax.swing.JScrollPane();
        dishIngredientTextArea = new javax.swing.JTextArea();
        homeDeliveryRadioButton = new javax.swing.JRadioButton();
        takeAwayRadioButton = new javax.swing.JRadioButton();
        cartLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        dishLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        totalField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        goBackBtn.setBackground(new java.awt.Color(44, 73, 129));
        goBackBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        goBackBtn.setText("Return to provider choise");
        goBackBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goBackBtnActionPerformed(evt);
            }
        });

        confirmOrderBtn.setBackground(new java.awt.Color(44, 73, 129));
        confirmOrderBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        confirmOrderBtn.setText("Confirm your order");
        confirmOrderBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmOrderBtnActionPerformed(evt);
            }
        });

        Vector header = new Vector();
        header.addElement("Dish name");
        header.addElement("Dish price");
        header.addElement("");
        cartTable.setModel(new NoEditableTableModelWithDelete(
            header,
            2
        ));
        cartTable.setDefaultRenderer(JButton.class,new JButtonRenderer(2,"Remove from your order"));
        jScrollPane9.setViewportView(cartTable);

        menuTable.setModel(new NoEditableTableModel(
            new Object [][] {
                {"A pizza ca pummarola ngoppa", "50", new javax.swing.JButton()},
                {null, null, new javax.swing.JButton()},
            },
            new String [] {
                "<html><div style = 'text-align: center'>Dish Name</div></html>\"",
                "<html><div style = 'text-align: center'>Dish Price</div></html>\"",
                ""
            },
            2
        ));
        menuTable.setDefaultRenderer(JButton.class,new JButtonRenderer(2,"Add to your order"));
        jScrollPane12.setViewportView(menuTable);

        dishDescriptionTextArea.setColumns(20);
        dishDescriptionTextArea.setRows(5);
        dishDescriptionTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dish Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jScrollPane10.setViewportView(dishDescriptionTextArea);

        dishIngredientTextArea.setColumns(20);
        dishIngredientTextArea.setRows(5);
        dishIngredientTextArea.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dish Ingredient", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jScrollPane11.setViewportView(dishIngredientTextArea);

        buttonGroup1.add(homeDeliveryRadioButton);
        homeDeliveryRadioButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        homeDeliveryRadioButton.setText("Home delivery");
        homeDeliveryRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeDeliveryRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(takeAwayRadioButton);
        takeAwayRadioButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        takeAwayRadioButton.setText("Take Away");

        cartLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cartLabel.setText("Your order");

        titleLabel.setFont(new java.awt.Font("Perpetua Titling MT", 1, 36)); // NOI18N
        titleLabel.setText("CHOOSE YOUR DISHES");

        dishLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dishLabel.setText("Menù of  Nome Provider");

        totalLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        totalLabel.setText("Total");

        totalField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout newViewMenuLayout = new javax.swing.GroupLayout(newViewMenu);
        newViewMenu.setLayout(newViewMenuLayout);
        newViewMenuLayout.setHorizontalGroup(
            newViewMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newViewMenuLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(newViewMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9)
                    .addComponent(cartLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dishLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(newViewMenuLayout.createSequentialGroup()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(newViewMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(confirmOrderBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(newViewMenuLayout.createSequentialGroup()
                                .addComponent(homeDeliveryRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(takeAwayRadioButton, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(goBackBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                            .addGroup(newViewMenuLayout.createSequentialGroup()
                                .addComponent(totalLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalField))))
                    .addComponent(titleLabel)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 913, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        newViewMenuLayout.setVerticalGroup(
            newViewMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(newViewMenuLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(titleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dishLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cartLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(newViewMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(newViewMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane11)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(newViewMenuLayout.createSequentialGroup()
                        .addGroup(newViewMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(homeDeliveryRadioButton)
                            .addComponent(takeAwayRadioButton))
                        .addGap(18, 18, 18)
                        .addGroup(newViewMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalLabel)
                            .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(confirmOrderBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(goBackBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newViewMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newViewMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void goBackBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_goBackBtnActionPerformed

    private void confirmOrderBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmOrderBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmOrderBtnActionPerformed

    private void totalFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalFieldActionPerformed

    private void homeDeliveryRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeDeliveryRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_homeDeliveryRadioButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel cartLabel;
    private javax.swing.JTable cartTable;
    private javax.swing.JButton confirmOrderBtn;
    private javax.swing.JTextArea dishDescriptionTextArea;
    private javax.swing.JTextArea dishIngredientTextArea;
    private javax.swing.JLabel dishLabel;
    private javax.swing.JButton goBackBtn;
    private javax.swing.JRadioButton homeDeliveryRadioButton;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable menuTable;
    private javax.swing.JPanel newViewMenu;
    private javax.swing.JRadioButton takeAwayRadioButton;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField totalField;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables
}
