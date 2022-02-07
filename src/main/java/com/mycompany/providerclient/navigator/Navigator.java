/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.navigator;

import com.mycompany.providerclient.controller.HomeController;
import com.mycompany.providerclient.controller.LogInController;
import com.mycompany.providerclient.controller.MenuManagerController;
import com.mycompany.providerclient.controller.SignUpController;
import com.mycompany.providerclient.controller.UpdateController;

/**
 * Represents a navigator between the views. It allows the view-switch, disposing
 * the previous view and creating a new controller instance of the next view.
 * @author aferr
 */
public class Navigator {
    
    private static Navigator instance;
        
    private Navigator(){}
    
    /**
     * Factory method to return the only instance of navigator
     * @return the instance of navigator
     */
    public synchronized static Navigator getInstance() {
        if (instance == null) {
            instance = new Navigator();
        }
        return instance;
    }

    /**
     * Switch the view from LogInView to HomeView
     * @param c the LogInController instance
     */
    public void fromLogInToHome(LogInController c){
        c.disposeView();
        Long providerId = c.getProviderId();
        new HomeController(providerId);
    }

    /**
     * Switch the view from LogInView to SignUpView
     * @param c the LogInController instance
     */
    public void fromLogInToSignUp(LogInController c){
        c.disposeView();
        new SignUpController();
    }

    /**
     * Switch the view from SignUpView to LogInView
     * @param c the SignUpController instance
     */
    public void fromSignUpToLogIn(SignUpController c){
        c.disposeView();
        new LogInController();
    }

    /**
     * Switch the view from HomeView to MenuManagerView
     * @param c the HomeController instance
     */
    public void fromHomeToMenuManager(HomeController c){
        c.disposeView();
        Long providerId = c.getProviderId();
        new MenuManagerController(providerId);
    }

    /**
     * Switch the view from HomeView to UpdateView
     * @param c the HomeController instance
     */
    public void fromHomeToUpdateAccount(HomeController c){
        c.disposeView();
        Long providerId = c.getProviderId();
        new UpdateController(providerId);
    }

    /**
     * Switch the view from HomeView to LogInView
     * @param c the HomeController instance
     */
    public void fromHomeToLogIn(HomeController c){
        c.disposeView();
        new LogInController();
    }

    /**
     * Switch the view from MenuManagerView to HomeView
     * @param c the MenuManagerController instance
     */
    public void fromMenuManagerToHome(MenuManagerController c){
        c.disposeView();
        Long providerId = c.getProviderId();
        new HomeController(providerId);
    }

    /**
     * Switch the view from MenuManagerView to UpdateView
     * @param c the MenuManagerController instance
     */
    public void fromMenuManagerToUpdateAccount(MenuManagerController c){
        c.disposeView();
        Long providerId = c.getProviderId();
        new UpdateController(providerId);
    }

    /**
     * Switch the view from MenuManagerView to LogInView
     * @param c the MenuManagerController instance
     */
    public void fromMenuManagerToLogIn(MenuManagerController c){
        c.disposeView();
        new LogInController();
    }

    /**
     * Switch the view from UpdateView to MenuManagerView
     * @param c the UpdateController instance
     */
    public void fromUpdateToMenuManager(UpdateController c){
        c.disposeView();
        Long providerId = c.getProviderId();
        new MenuManagerController(providerId);
    }

    /**
     * Switch the view from UpdateView to HomeView
     * @param c the UpdateController instance
     */
    public void fromUpdateToHome(UpdateController c){
        c.disposeView();
        Long providerId = c.getProviderId();
        new HomeController(providerId);
    }

    /**
     * Switch the view from UpdateView to LogInView
     * @param c the UpdateController instance
     */
    public void fromUpdateToLogIn(UpdateController c){
        c.disposeView();
        new LogInController();
    }
        
}
