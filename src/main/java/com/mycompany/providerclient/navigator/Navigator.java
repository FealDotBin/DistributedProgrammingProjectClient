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
 *
 * @author aferr
 */
public class Navigator {
    
    private static Navigator instance;
        
        private Navigator(){
            
        }
        
        public synchronized static Navigator getInstance() {
            if (instance == null) {
                instance = new Navigator();
            }
            return instance;
        }
        
        public void fromLogInToHome(LogInController c){
            c.disposeView();
            Long providerId = c.getProviderId();
            new HomeController(providerId);
        }
        
        public void fromLogInToSignUp(LogInController c){
            c.disposeView();
            new SignUpController();
        }
        
        public void fromSignUpToLogIn(SignUpController c){
            c.disposeView();
            new LogInController();
        }
        
        public void fromHomeToMenuManager(HomeController c){
            c.disposeView();
            Long providerId = c.getProviderId();
            new MenuManagerController(providerId);
        }
        
        public void fromHomeToUpdateAccount(HomeController c){
            c.disposeView();
            Long providerId = c.getProviderId();
            new UpdateController(providerId);
        }
        
        public void fromHomeToLogIn(HomeController c){
            c.disposeView();
            new LogInController();
        }
        
        public void fromMenuManagerToHome(MenuManagerController c){
            c.disposeView();
            Long providerId = c.getProviderId();
            new HomeController(providerId);
        }
        
        public void fromMenuManagerToUpdateAccount(MenuManagerController c){
            c.disposeView();
            Long providerId = c.getProviderId();
            new UpdateController(providerId);
        }
        
        public void fromMenuManagerToLogIn(MenuManagerController c){
            c.disposeView();
            new LogInController();
        }
        
        public void fromUpdateToMenuManager(UpdateController c){
            c.disposeView();
            Long providerId = c.getProviderId();
            new MenuManagerController(providerId);
        }
        
        public void fromUpdateToHome(UpdateController c){
            c.disposeView();
            Long providerId = c.getProviderId();
            new HomeController(providerId);
        }
        
        public void fromUpdateToLogIn(UpdateController c){
            c.disposeView();
            new LogInController();
        }
        
}
