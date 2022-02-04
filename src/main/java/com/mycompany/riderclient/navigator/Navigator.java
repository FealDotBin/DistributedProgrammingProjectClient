/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.riderclient.navigator;


import com.mycompany.riderclient.controller.RiderHomeController;
import com.mycompany.riderclient.controller.RiderLoginController;
import com.mycompany.riderclient.controller.RiderOrderController;
import com.mycompany.riderclient.controller.RiderUpdateController;
import com.mycompany.riderclient.controller.SignUpController;


/**
 *
 * @author aferr
 */
public class Navigator {
    
    private static Navigator instance;
    
    private static RiderUpdateController riderUpdate;
        
        private Navigator(){
            
        }
        
        public synchronized static Navigator getInstance() {
            if (instance == null) {
                instance = new Navigator();
            }
            return instance;
        }
        
        
        public void fromLogInToSignUp(RiderLoginController c){
            c.disposeView();
            new SignUpController();
        }
        
        public void fromLogInToHome(RiderLoginController c){
            c.disposeView();
            new RiderHomeController(c.getRiderId());
        }
        
         public void fromHomeToLogin(RiderHomeController c){
            c.disposeView();
            new RiderLoginController();
        }
        
        public void fromHomeToOrderView(RiderHomeController c, Long orderId){
            c.disposeView();
            new RiderOrderController(c.getRiderId(), orderId);
        }
        
        
        public void fromSignUpToLogIn(SignUpController c){
            c.disposeView();
            new RiderLoginController();
        }
        
        public void fromOrderToHome(RiderOrderController c){
            c.disposeView();
            new RiderHomeController(c.getRiderId());
        }
        
        public void showUpdateView(Long riderId){
            //  RiderUpdateController.getInstance(riderId);
            if(this.riderUpdate == null){
                this.riderUpdate = new RiderUpdateController(riderId);
                return;
            }
            riderUpdate.getUpdateView().setVisible(true);
            
        }

        
}
