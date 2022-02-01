/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.navigator;
    
import com.mycompany.customerclient.controller.CustomerBalanceController;
import com.mycompany.navigator.*;
import com.mycompany.customerclient.controller.CustomerLogInController;
import com.mycompany.customerclient.controller.CustomerProviderSelectionController;
import com.mycompany.customerclient.controller.CustomerSignUpController;
import com.mycompany.customerclient.controller.CustomerUpdateController;
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
        
        
        public void fromLogInToProviderSelection(CustomerLogInController c){
            c.disposeView();
            customerId=c.getCustomerId();
            new CustomerProviderSelectionController(customerId);
        }
        
        public void fromProviderSelectionToOrderCreation(CustomerProviderSelectionController c){
            c.disposeView();
            
            new OrderCreationController(customerId, c.getSelectedProvider());
        }
        
        public void fromProviderSelectionToLogIn(CustomerProviderSelectionController c){
            c.disposeView();
            new CustomerLogInController();
        }
        
        public void fromSignUpToLogIn(CustomerSignUpController c){
            c.disposeView();
            customerId=null;
            new CustomerLogInController();
        }
        
        public void fromLogInToSingUp(CustomerLogInController c){
            c.disposeView();
            new CustomerSignUpController();
        }
        
        public void fromUpdatetoProviderSelection(CustomerUpdateController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
        
        public void fromUpdatetoLogOut(CustomerUpdateController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        public void fromBalancetoLogOut(CustomerBalanceController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        public void fromBalancetoUpdate(CustomerBalanceController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
        public void fromBalancetoProviderSelection(CustomerBalanceController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
        
        public void fromOrderCreationToLogOut(OrderCreationController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        public void fromOrderCreationToUpdate(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
        public void fromOrderCreationToBalance(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        public void fromOrderCreationToProviderSelection(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
}
