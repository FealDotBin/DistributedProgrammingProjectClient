/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.view;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.mycompany.common.components.JButtonEditor;
import com.mycompany.common.components.JButtonRenderer;
import com.mycompany.common.components.NoEditableTableModel;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author CATELLO
 */
public class ProviderSelection extends javax.swing.JFrame {

    /**
     * Creates new form ProviderSelection
     */
    public ProviderSelection() {
                try {
            //   UIManager.
            UIManager.setLookAndFeel( new FlatDarculaLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(com.mycompany.customerclient.view.MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        initComponents();
    }

    public JButton getLogoutBtn() {
        return logoutBtn;
    }

    public JTable getProviderTable() {
        return providerTable;
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        providerView = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        providerTable = new javax.swing.JTable(){
            private static final long serialVersionUID = 1L;

            /*@Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }*/
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                    return String.class;
                    case 1:
                    return String.class;
                    case 2:
                    return Boolean.class;
                    case 3:
                    return Boolean.class;
                    case 4:
                    return String.class;
                    case 5:
                    return String.class;
                    case 6:
                    return JButton.class;
                    default:
                    return String.class;
                }
            }
        };
        titleLabel = new javax.swing.JLabel();
        logoutBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Vector providerTableHeader = new Vector();
        providerTableHeader.addElement("Provider Name");
        providerTableHeader.addElement("Cuisine");
        providerTableHeader.addElement("Home Delivery");
        providerTableHeader.addElement("Take Away");
        providerTableHeader.addElement("Address");
        providerTableHeader.addElement("Telephone");
        providerTableHeader.addElement("");
        providerTable.setModel(new NoEditableTableModel(
            providerTableHeader,
            -6
        ));
        providerTable.setDefaultRenderer(JButton.class,new JButtonRenderer(6, "Menù"));
        providerTable.setDefaultEditor(JButton.class,new JButtonEditor());
        jScrollPane1.setViewportView(providerTable);

        titleLabel.setFont(new java.awt.Font("Perpetua Titling MT", 1, 36)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("CHOOSE WHERE YOU WANNA EAT");

        logoutBtn.setBackground(new java.awt.Color(44, 73, 129));
        logoutBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        logoutBtn.setText("Log out");
        logoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout providerViewLayout = new javax.swing.GroupLayout(providerView);
        providerView.setLayout(providerViewLayout);
        providerViewLayout.setHorizontalGroup(
            providerViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(providerViewLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(providerViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(providerViewLayout.createSequentialGroup()
                        .addComponent(titleLabel)
                        .addGap(18, 18, 18)
                        .addComponent(logoutBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        providerViewLayout.setVerticalGroup(
            providerViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, providerViewLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(providerViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(logoutBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(125, 125, 125))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 849, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(providerView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 420, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(providerView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_logoutBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JTable providerTable;
    private javax.swing.JPanel providerView;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
