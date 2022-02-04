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
 * Represents a navigator between the views. It allows the view-switch, disposing
 * the previous view and creating a new controller instance of the next view.
 * @author Amos
 */
public class Navigator {
    
    private static Navigator instance;
    
    private static RiderUpdateController riderUpdate;
        
        private Navigator(){
            
        }
        
        /**
         * Navigator singleton
         * @return the current Navigator instance
         */
        public synchronized static Navigator getInstance() {
            if (instance == null) {
                instance = new Navigator();
            }
            return instance;
        }
        
        /**
         * Switch the view from RiderLoginView to RiderSignUpView
         * @param c 
         */
        public void fromLogInToSignUp(RiderLoginController c){
            c.disposeView();
            new SignUpController();
        }
        
         /**
         * Switch the view from RiderLoginView to RiderHomeView
         * @param c 
         */
        public void fromLogInToHome(RiderLoginController c){
            c.disposeView();
            new RiderHomeController(c.getRiderId());
        }
        
        /**
         * Switch the view from RiderHomeView to RiderLoginView
         * @param c 
         */
         public void fromHomeToLogin(RiderHomeController c){
            c.disposeView();
            new RiderLoginController();
        }
        
         /**
         * Switch the view from RiderHomeView to DeliveryOrderView
         * @param c 
         */
        public void fromHomeToOrderView(RiderHomeController c, Long orderId){
            c.disposeView();
            new RiderOrderController(c.getRiderId(), orderId);
        }
        
         /**
         * Switch the view from RiderSignUp to RiderLoginView
         * @param c 
         */
        public void fromSignUpToLogIn(SignUpController c){
            c.disposeView();
            new RiderLoginController();
        }
        
         /**
         * Switch the view from DeliveryOrderView to RiderHomeView
         * @param c 
         */
        public void fromOrderToHome(RiderOrderController c){
            c.disposeView();
            new RiderHomeController(c.getRiderId());
        }
        
        /**
         * Show the RiderUpdateView; if the view was created and then closed,
         * set the visibility on true, avoiding multiple istances.
         * @param riderId 
         */
        public void showUpdateView(Long riderId){
            //  RiderUpdateController.getInstance(riderId);
            if(this.riderUpdate == null){
                this.riderUpdate = new RiderUpdateController(riderId);
                return;
            }
            riderUpdate.getUpdateView().setVisible(true);
            
        }

        
}
