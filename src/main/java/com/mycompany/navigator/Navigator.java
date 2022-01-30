/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.navigator;
    
import com.mycompany.customerclient.controller.CustomerLogInController;
import com.mycompany.customerclient.controller.OrderCreationController;

/**
 *
 * @author CATELLO
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
        
        public void fromLogInToOrderCreation(CustomerLogInController c){
            c.disposeView();
            new OrderCreationController();
        }
}
