/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.mycompany.common.components.NoEditableTableModel;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author aferr
 */
public class HomeView extends javax.swing.JFrame {

    /**
     * Creates new form AllOrderViewer
     */
    public HomeView() {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(HomeView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        initComponents();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JButton getAcceptBtn() {
        return acceptBtn;
    }

    public JTable getAllOrdersTable() {
        return allOrdersTable;
    }

    public JScrollPane getAllOrdersTableScroll() {
        return allOrdersTableScroll;
    }

    public JButton getCompleteBtn() {
        return completeBtn;
    }

    public JLabel getHomeLabel() {
        return homeLabel;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public JScrollPane getjScrollPane2() {
        return jScrollPane2;
    }

    public JButton getLogOutBtn() {
        return logOutBtn;
    }

    public JButton getRefreshBtn() {
        return refreshBtn;
    }

    public JButton getRefuseBtn() {
        return refuseBtn;
    }

    public JLabel getSelectedOrderLabel() {
        return selectedOrderLabel;
    }

    public JTable getSelectedOrderTable() {
        return selectedOrderTable;
    }

    public JButton getShipBtn() {
        return shipBtn;
    }

    public JButton getAvailableBtn() {
        return availableBtn;
    }

    public JPanel getjPanel2() {
        return jPanel2;
    }

    public JButton getManageMenuBtn() {
        return manageMenuBtn;
    }

    public JPanel getNavBar() {
        return navBar;
    }

    public JButton getUpdateAccountBtn() {
        return updateAccountBtn;
    }

    public JRadioButton getShippedRadioButton() {
        return ShippedRadioButton;
    }

    public JRadioButton getAcceptedRadioButton() {
        return acceptedRadioButton;
    }

    public ButtonGroup getAllOrdersTableButtonGroup() {
        return allOrdersTableButtonGroup;
    }

    public JRadioButton getCompletedRadioButton() {
        return completedRadioButton;
    }

    public JRadioButton getPendingRadioButton() {
        return pendingRadioButton;
    }

    public JRadioButton getRefusedRadioButton() {
        return refusedRadioButton;
    }

    public JRadioButton getSemiAcceptedRadioButton() {
        return semiAcceptedRadioButton;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        allOrdersTableButtonGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        allOrdersTableScroll = new javax.swing.JScrollPane();
        allOrdersTable = new javax.swing.JTable(){
            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                    return String.class;
                    case 1:
                    return String.class;
                    case 2:
                    return String.class;
                    case 3:
                    return String.class;
                    case 4:
                    return String.class;
                    default:
                    return String.class;
                }
            }
        };
        selectedOrderLabel = new javax.swing.JLabel();
        acceptBtn = new javax.swing.JButton();
        shipBtn = new javax.swing.JButton();
        refuseBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        completeBtn = new javax.swing.JButton();
        homeLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        selectedOrderTable = new javax.swing.JTable(){
            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                    return String.class;
                    case 1:
                    return Integer.class;
                    default:
                    return String.class;
                }
            }
        };
        availableBtn = new javax.swing.JButton();
        pendingRadioButton = new javax.swing.JRadioButton();
        semiAcceptedRadioButton = new javax.swing.JRadioButton();
        acceptedRadioButton = new javax.swing.JRadioButton();
        ShippedRadioButton = new javax.swing.JRadioButton();
        completedRadioButton = new javax.swing.JRadioButton();
        refusedRadioButton = new javax.swing.JRadioButton();
        navBar = new javax.swing.JPanel();
        manageMenuBtn = new javax.swing.JButton();
        updateAccountBtn = new javax.swing.JButton();
        logOutBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(802, 802));

        Vector allOrdersTableHeader = new Vector();
        allOrdersTableHeader.addElement("Customer Name");
        allOrdersTableHeader.addElement("Rider Name");
        allOrdersTableHeader.addElement("Order Type");
        allOrdersTableHeader.addElement("Order State");
        allOrdersTableHeader.addElement("Delivery Time");
        allOrdersTable.setModel(new NoEditableTableModel(
            allOrdersTableHeader,
            -1
        ));
        allOrdersTableScroll.setViewportView(allOrdersTable);

        selectedOrderLabel.setFont(new java.awt.Font("Perpetua Titling MT", 1, 36)); // NOI18N
        selectedOrderLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedOrderLabel.setText("selected order");

        acceptBtn.setBackground(new java.awt.Color(44, 73, 129));
        acceptBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        acceptBtn.setText("ACCEPT");
        acceptBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptBtnActionPerformed(evt);
            }
        });

        shipBtn.setBackground(new java.awt.Color(44, 73, 129));
        shipBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        shipBtn.setText("SHIP");
        shipBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shipBtnActionPerformed(evt);
            }
        });

        refuseBtn.setBackground(new java.awt.Color(146, 43, 32));
        refuseBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        refuseBtn.setText("REFUSE");
        refuseBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refuseBtnActionPerformed(evt);
            }
        });

        refreshBtn.setBackground(new java.awt.Color(44, 73, 129));
        refreshBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        refreshBtn.setText("REFRESH");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        completeBtn.setBackground(new java.awt.Color(44, 73, 129));
        completeBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        completeBtn.setText("COMPLETE");
        completeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completeBtnActionPerformed(evt);
            }
        });

        homeLabel.setFont(new java.awt.Font("Perpetua Titling MT", 1, 36)); // NOI18N
        homeLabel.setText("home");

        Vector selectedOrderTableHeader = new Vector();
        selectedOrderTableHeader.addElement("Dish");
        selectedOrderTableHeader.addElement("Quantity");
        selectedOrderTable.setModel(new NoEditableTableModel(
            selectedOrderTableHeader,
            -1
        ));
        jScrollPane2.setViewportView(selectedOrderTable);

        availableBtn.setBackground(new java.awt.Color(146, 43, 32));
        availableBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        availableBtn.setText("NOT AVAILABLE");
        availableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availableBtnActionPerformed(evt);
            }
        });

        allOrdersTableButtonGroup.add(pendingRadioButton);
        pendingRadioButton.setSelected(true);
        pendingRadioButton.setText("Pending");

        allOrdersTableButtonGroup.add(semiAcceptedRadioButton);
        semiAcceptedRadioButton.setText("Semi Accepted");

        allOrdersTableButtonGroup.add(acceptedRadioButton);
        acceptedRadioButton.setText("Accepted");

        allOrdersTableButtonGroup.add(ShippedRadioButton);
        ShippedRadioButton.setText("Shipped");

        allOrdersTableButtonGroup.add(completedRadioButton);
        completedRadioButton.setText("Completed");

        allOrdersTableButtonGroup.add(refusedRadioButton);
        refusedRadioButton.setText("Refused");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(324, 324, 324)
                        .addComponent(homeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(availableBtn))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(selectedOrderLabel)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(refuseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(shipBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(acceptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(completeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(95, 95, 95))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(allOrdersTableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pendingRadioButton)
                            .addComponent(semiAcceptedRadioButton)
                            .addComponent(acceptedRadioButton)
                            .addComponent(ShippedRadioButton)
                            .addComponent(completedRadioButton)
                            .addComponent(refusedRadioButton)
                            .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 14, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(homeLabel)
                    .addComponent(availableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(pendingRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(semiAcceptedRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(acceptedRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(ShippedRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(completedRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(refusedRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(allOrdersTableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(selectedOrderLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addGap(21, 21, 21))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(refuseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(acceptBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(shipBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(completeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(138, Short.MAX_VALUE))))
        );

        navBar.setBackground(new java.awt.Color(146, 43, 32));

        manageMenuBtn.setBackground(new java.awt.Color(255, 255, 255));
        manageMenuBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        manageMenuBtn.setForeground(new java.awt.Color(5, 5, 5));
        manageMenuBtn.setText("Manage Menu");
        manageMenuBtn.setBorderPainted(false);
        manageMenuBtn.setMaximumSize(new java.awt.Dimension(145, 29));
        manageMenuBtn.setMinimumSize(new java.awt.Dimension(145, 29));
        manageMenuBtn.setPreferredSize(new java.awt.Dimension(145, 29));
        manageMenuBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageMenuBtnActionPerformed(evt);
            }
        });

        updateAccountBtn.setBackground(new java.awt.Color(255, 255, 255));
        updateAccountBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updateAccountBtn.setForeground(new java.awt.Color(5, 5, 5));
        updateAccountBtn.setText("Update Account");
        updateAccountBtn.setBorderPainted(false);
        updateAccountBtn.setPreferredSize(new java.awt.Dimension(145, 29));
        updateAccountBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateAccountBtnActionPerformed(evt);
            }
        });

        logOutBtn.setBackground(new java.awt.Color(255, 255, 255));
        logOutBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logOutBtn.setForeground(new java.awt.Color(5, 5, 5));
        logOutBtn.setText("Log out");
        logOutBtn.setBorderPainted(false);
        logOutBtn.setPreferredSize(new java.awt.Dimension(145, 29));
        logOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout navBarLayout = new javax.swing.GroupLayout(navBar);
        navBar.setLayout(navBarLayout);
        navBarLayout.setHorizontalGroup(
            navBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navBarLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(manageMenuBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(145, 145, 145)
                .addComponent(updateAccountBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logOutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        navBarLayout.setVerticalGroup(
            navBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navBarLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(navBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manageMenuBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateAccountBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logOutBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(navBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(navBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 835, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void shipBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shipBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_shipBtnActionPerformed

    private void acceptBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_acceptBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void completeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completeBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_completeBtnActionPerformed

    private void refuseBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refuseBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refuseBtnActionPerformed

    private void availableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availableBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_availableBtnActionPerformed

    private void manageMenuBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageMenuBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_manageMenuBtnActionPerformed

    private void updateAccountBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateAccountBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updateAccountBtnActionPerformed

    private void logOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_logOutBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton ShippedRadioButton;
    private javax.swing.JButton acceptBtn;
    private javax.swing.JRadioButton acceptedRadioButton;
    private javax.swing.JTable allOrdersTable;
    private javax.swing.ButtonGroup allOrdersTableButtonGroup;
    private javax.swing.JScrollPane allOrdersTableScroll;
    private javax.swing.JButton availableBtn;
    private javax.swing.JButton completeBtn;
    private javax.swing.JRadioButton completedRadioButton;
    private javax.swing.JLabel homeLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton logOutBtn;
    private javax.swing.JButton manageMenuBtn;
    private javax.swing.JPanel navBar;
    private javax.swing.JRadioButton pendingRadioButton;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton refuseBtn;
    private javax.swing.JRadioButton refusedRadioButton;
    private javax.swing.JLabel selectedOrderLabel;
    private javax.swing.JTable selectedOrderTable;
    private javax.swing.JRadioButton semiAcceptedRadioButton;
    private javax.swing.JButton shipBtn;
    private javax.swing.JButton updateAccountBtn;
    // End of variables declaration//GEN-END:variables
}