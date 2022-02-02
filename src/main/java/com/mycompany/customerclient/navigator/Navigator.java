/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.customerclient.navigator;
    
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.customerclient.controller.CustomerBalanceController;
import com.mycompany.customerclient.controller.CustomerCurrentOrderController;
import com.mycompany.navigator.*;
import com.mycompany.customerclient.controller.CustomerLogInController;
import com.mycompany.customerclient.controller.CustomerOrderHistoryController;
import com.mycompany.customerclient.controller.CustomerProviderSelectionController;
import com.mycompany.customerclient.controller.CustomerSignUpController;
import com.mycompany.customerclient.controller.CustomerUpdateController;
import com.mycompany.customerclient.controller.OrderCreationController;
import com.mycompany.customerclient.controller.OrderMoreInformationController;

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
        
        public void fromProviderSelectiontoHistory(CustomerProviderSelectionController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        public void fromProviderSelectiontoUpdate(CustomerProviderSelectionController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
        public void fromProviderSelectiontoBalance(CustomerProviderSelectionController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
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
        
        public void fromUpdatetoHistory(CustomerUpdateController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        public void fromUpdatetoBalance(CustomerUpdateController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        public void fromUpdatetoOrderViewer(CustomerUpdateController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerCurrentOrderController(customerId);
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
        
        public void fromBalancetoHistory(CustomerBalanceController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        public void fromBalancetoOrderViewer(CustomerBalanceController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerCurrentOrderController(customerId);
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
        
        public void fromOrderCreationtoHistory(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        public void fromOrderCreationToProviderSelection(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
        
        public void fromOrderViewertoLogIn(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        public void fromOrderViewertoUpdate(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
        public void fromOrderViewertoToBalance(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        public void fromOrderViewertoHistory(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        public void fromOrderViewerToProviderSelection(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
        
        public void fromLogInToOrderViewer(CustomerLogInController c){
           customerId=c.getCustomerId();
           
           c.disposeView();
           
           new CustomerCurrentOrderController(customerId);
        }
        
        public void fromHistory1ToBalance(CustomerOrderHistoryController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        public void fromHistory1ToUpdate(CustomerOrderHistoryController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
         public void fromHistory1ToLogIn(CustomerOrderHistoryController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        public void fromHistory1ToOrderViewer(CustomerOrderHistoryController c){
           customerId=c.getCustomerId();          
           c.disposeView();        
           new CustomerCurrentOrderController(customerId);
        }
        
        public void fromHistory1ToProviderSelection(CustomerOrderHistoryController c){
           c.disposeView();
            customerId=c.getCustomerId();
            new CustomerProviderSelectionController(customerId);
        }
        
        public void fromHistory1ToMoreInfo(CustomerOrderHistoryController c){
           c.disposeView();
            customerId=c.getCustomerId();
            OrderDto selectedOrder = c.getSelectedOrder();
            new OrderMoreInformationController(customerId, selectedOrder);
        }
        
        public void fromMoreInfoToBalance(OrderMoreInformationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        public void fromMoreInfoToUpdate(OrderMoreInformationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
         public void fromMoreInfoToLogIn(OrderMoreInformationController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        public void fromMoreInfoToOrderViewer(OrderMoreInformationController c){
           customerId=c.getCustomerId();
           
           c.disposeView();
           
           new CustomerCurrentOrderController(customerId);
        }
        
        public void fromMoreInfoToProviderSelection(OrderMoreInformationController c){
           c.disposeView();
            customerId=c.getCustomerId();
            new CustomerProviderSelectionController(customerId);
        }
        
        public void fromMoreInfoToHistory(OrderMoreInformationController c){
           c.disposeView();
            customerId=c.getCustomerId();
            new CustomerOrderHistoryController(customerId);
        }
        
        
}
