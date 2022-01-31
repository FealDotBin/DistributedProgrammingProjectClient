/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.navigator;
    
import com.mycompany.navigator.*;
import com.mycompany.customerclient.controller.CustomerLogInController;
import com.mycompany.customerclient.controller.CustomerProviderSelectionController;
import com.mycompany.customerclient.controller.CustomerSignUpController;
import com.mycompany.customerclient.controller.OrderCreationController;

/**
 *
 * @author CATELLO
 */
public class Navigator {
        
        private static Navigator instance;
        private Long customerId;
        private Navigator(){
            
        }
        
        public synchronized static Navigator getInstance() {
            if (instance == null) {
                instance = new Navigator();
            }
            return instance;
        }
        
        public void fromLogInToOrderCreation(CustomerLogInController c){
            c.disposeView();
            new OrderCreationController();
        }
        
        public void fromLogInToProviderSelection(CustomerLogInController c){
            c.disposeView();
            customerId=c.getCustomerId();
            new CustomerProviderSelectionController(customerId);
        }
        
        public void fromProviderSelectionOrderCreation(CustomerProviderSelectionController c){
            c.disposeView();
            new OrderCreationController();
        }
        
        public void fromSignUpToLogIn(CustomerSignUpController c){
            c.disposeView();
            new CustomerLogInController();
        }
        
        public void fromLogInToSingUp(CustomerLogInController c){
            c.disposeView();
            new CustomerSignUpController();
        }
        
}
