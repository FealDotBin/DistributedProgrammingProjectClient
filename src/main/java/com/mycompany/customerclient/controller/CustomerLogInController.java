/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.customerclient.view.customerLogIn;
import com.mycompany.navigator.Navigator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

/**
 *
 * @author CATELLO
 */
public class CustomerLogInController {
    private customerLogIn view;
    private JButton loginButton;
    private Navigator nav;

    public CustomerLogInController(){
        view = new customerLogIn();
        nav = Navigator.getInstance();
        loginButton = view.getLogInButton();
        loginButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                nav.fromLogInToOrderCreation(CustomerLogInController.this);
            }
            
        });
        view.setVisible(true);
    }
    
    public void disposeView(){
        view.dispose();
    }
    
    public static void main(String args[]){
         SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CustomerLogInController c = null;
                c = new CustomerLogInController();
            }
        });
        
    }
    
    
    
    
    
}
