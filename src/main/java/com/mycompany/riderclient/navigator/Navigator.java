/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.riderclient.navigator;


import com.mycompany.riderclient.controller.RiderLoginController;
import com.mycompany.riderclient.controller.SignUpController;


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
        
        
        public void fromLogInToSignUp(RiderLoginController c){
            c.disposeView();
            new SignUpController();
        }
        
        
        
        public void fromSignUpToLogIn(SignUpController c){
            c.disposeView();
            new RiderLoginController();
        }
        

        
}
