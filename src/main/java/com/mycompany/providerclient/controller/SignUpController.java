/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller;

import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.providerclient.navigator.Navigator;
import com.mycompany.providerclient.view.SignUpView;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;

/**
 *
 * @author aferr
 */
public class SignUpController {
    
    private SignUpView signUpView;
    private JTextFieldPlaceholder usernameTextField;
    private JTextFieldPlaceholder passwordTextField;
    private JTextFieldPlaceholder nameTextField;
    private JTextFieldPlaceholder surnameTextField;
    private JDateChooser birtDateChooser;
    private JTextFieldPlaceholder ibanTextField;
    private JTextFieldPlaceholder addressTextField;
    private JTextFieldPlaceholder telephoneNumberTextField;
    private JTextFieldPlaceholder providerNameTextField;
    private JTextFieldPlaceholder cuisineTextField;
    private JCheckBox deliveringCheckBox;
    private JCheckBox takeAwayCheckBox;
    private JCheckBox ownRiderCheckBox;
    private JButton signUpBtn;
    private JButton logInBtn;
    private Navigator navigator;
    
    public SignUpController(){
        // initialize view
        signUpView = new SignUpView();
        signUpView.setVisible(true);
        
        // get components from view
        usernameTextField = signUpView.getUsernameTextField();
        passwordTextField = signUpView.getPasswordTextField();
        nameTextField = signUpView.getNameTextField();
        surnameTextField = signUpView.getSurnameTextField();
        birtDateChooser = signUpView.getBirthDateChooser();
        ibanTextField = signUpView.getIbanTextField();
        addressTextField = signUpView.getAddressTextField();
        telephoneNumberTextField = signUpView.getTelephoneTextField();
        providerNameTextField = signUpView.getProviderNameTextField();
        cuisineTextField = signUpView.getCuisineTextField();
        deliveringCheckBox = signUpView.getDeliveringCheckBox();
        takeAwayCheckBox = signUpView.getTakeAwayCheckBox();
        ownRiderCheckBox = signUpView.getOwnRiderCheckBox();
        signUpBtn = signUpView.getSignUpBtn();
        logInBtn = signUpView.getLogInBtn();
        
        // get navigator
        navigator = Navigator.getInstance();
        
        // attach listener to signUpBtn
        signUpBtn.addActionListener((event) -> {
            
        });
        
        // attach listener to logInBtn
        logInBtn.addActionListener((event) -> {
            navigator.fromSignUpToLogIn(SignUpController.this);
        });
        
    }
    
    public void disposeView(){
        signUpView.dispose();
    }
    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SignUpController c = new SignUpController();
            }
        });
    }
}
