/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.controller;

import com.mycompany.customerclient.navigator.Navigator;
import com.mycompany.customerclient.view.customerLogIn;
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
    private JButton signUpButton;
    private Navigator nav;

    public CustomerLogInController(){
        view = new customerLogIn();
        nav = Navigator.getInstance();
        signUpButton = view.getSignUpButton();
        signUpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                nav.fromLogInToSingUp(CustomerLogInController.this);
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
