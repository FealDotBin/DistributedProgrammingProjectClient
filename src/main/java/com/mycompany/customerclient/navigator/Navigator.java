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
 * Represents a navigator between the customer views. It allows the view-switch, disposing
 * the previous view and creating a new controller instance of the next view.
 * @author CATELLO
 */
public class Navigator {
        
        private static Navigator instance;
        private Long customerId;
        private Navigator(){
            
        }
        /**
         * The navigator implements the singleton pattern.
         * So there is only one instance of it for all customer view controllers.
         * When one of them tries to instantiate it if the navigator has not yet been instantiated then it is created and then returned.
         * If it has been instantiated at least once, it is not created again when a controller needs it.
         * @return navigator istance that allows the view-switch
         */
        public synchronized static Navigator getInstance() {
            if (instance == null) {
                instance = new Navigator();
            }
            return instance;
        }
        
        /**
         * This method removes CustomerLogIn view from the visualization and adds ProviderSelection view
         * @param c CustomerLogIn view controller
         */
        public void fromLogInToProviderSelection(CustomerLogInController c){
            c.disposeView();
            customerId=c.getCustomerId();
            new CustomerProviderSelectionController(customerId);
        }
        /**
         * This method removes ProviderSelection view from the visualization and adds OrderCreation view
         * @param c ProviderSelection view controller
         */
        public void fromProviderSelectionToOrderCreation(CustomerProviderSelectionController c){
            c.disposeView();
            new OrderCreationController(customerId, c.getSelectedProvider());
        }
        /**
         * This method removes ProviderSelection view from the visualization and adds CustomerLogIn view
         * @param c ProviderSelection view controller
         */
        public void fromProviderSelectionToLogIn(CustomerProviderSelectionController c){
            c.disposeView();
            new CustomerLogInController();
        }
        
        /**
         * This method removes ProviderSelection view from the visualization and adds CustomerFrameHistory view
         * @param c ProviderSelection view controller
         */
        public void fromProviderSelectiontoHistory(CustomerProviderSelectionController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        /**
         * This method removes ProviderSelection view from the visualization and adds customerUpdate view
         * @param c ProviderSelection view controller
         */
        public void fromProviderSelectiontoUpdate(CustomerProviderSelectionController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
        /**
         * This method removes ProviderSelection view from the visualization and adds UpdateBalance view
         * @param c ProviderSelection view controller
         */
        public void fromProviderSelectiontoBalance(CustomerProviderSelectionController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        /**
         * This method removes customerSignUp view from the visualization and adds CustomerLogIn view
         * @param c CustomerSignUp view controller
         */
        public void fromSignUpToLogIn(CustomerSignUpController c){
            c.disposeView();
            customerId=null;
            new CustomerLogInController();
        }
        
        /**
         * This method removes CustomerLogIn view from the visualization and adds CustomerSignUp view
         * @param c CustomerLogIn view controller
         */
        public void fromLogInToSingUp(CustomerLogInController c){
            c.disposeView();
            new CustomerSignUpController();
        }
        
        /**
         * This method removes CustomerLogIn view from the visualization and adds CurrentOrderView view
         * @param c CustomerLogIn view controller
         */
        public void fromLogInToOrderViewer(CustomerLogInController c){
           customerId=c.getCustomerId();
           
           c.disposeView();
           
           new CustomerCurrentOrderController(customerId);
        }
        
        /**
         * This method removes CustomerUpdate view from the visualization and adds ProviderSelection view
         * @param c CustomerUpdate view controller
         */
        public void fromUpdatetoProviderSelection(CustomerUpdateController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
        
        /**
         * This method removes CustomerUpdate view from the visualization and adds CustomerLogIn view
         * @param c CustomerUpdate view controller
         */
        public void fromUpdatetoLogOut(CustomerUpdateController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        /**
         * This method removes CustomerUpdate view from the visualization and adds CustomerFrameHistory view
         * @param c CustomerUpdate view controller
         */
        public void fromUpdatetoHistory(CustomerUpdateController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        /**
         * This method removes CustomerUpdate view from the visualization and adds UpdateBalance view
         * @param c CustomerUpdate view controller
         */
        public void fromUpdatetoBalance(CustomerUpdateController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        /**
         * This method removes CustomerUpdate view from the visualization and adds CurrentOrderView view
         * @param c CustomerUpdate view controller
         */
        public void fromUpdatetoOrderViewer(CustomerUpdateController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerCurrentOrderController(customerId);
        }
        
        /**
         * This method removes UpdateBalance view from the visualization and adds CustomerLogIn view
         * @param c UpdateBalance view controller
         */
        public void fromBalancetoLogOut(CustomerBalanceController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        /**
         * This method removes UpdateBalance view from the visualization and adds CustomerUpdate view
         * @param c UpdateBalance view controller
         */
        public void fromBalancetoUpdate(CustomerBalanceController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        /**
         * This method removes UpdateBalance view from the visualization and adds ProviderSelection view
         * @param c UpdateBalance view controller
         */
        public void fromBalancetoProviderSelection(CustomerBalanceController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
        /**
         * This method removes UpdateBalance view from the visualization and adds CustomerFremaeHistory1 view
         * @param c UpdateBalance view controller
         */
        public void fromBalancetoHistory(CustomerBalanceController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        /**
         * This method removes UpdateBalance view from the visualization and adds CurrentOrderView view
         * @param c UpdateBalance view controller
         */
        public void fromBalancetoOrderViewer(CustomerBalanceController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerCurrentOrderController(customerId);
        }
        
        /**
         * This method removes OrderCreation view from the visualization and adds CustomerLogIn view
         * @param c OrderCreation view controller
         */
        public void fromOrderCreationToLogOut(OrderCreationController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        /**
         * This method removes OrderCreation view from the visualization and adds CustomerLogIn view
         * @param c OrderCreation view controller
         */
        public void fromOrderCreationToUpdate(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
        /**
         * This method removes OrderCreation view from the visualization and adds UpdateBalance view
         * @param c OrderCreation view controller
         */
        public void fromOrderCreationToBalance(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        /**
         * This method removes OrderCreation view from the visualization and adds CustomerFrameHistory1 view
         * @param c OrderCreation view controller
         */
        public void fromOrderCreationtoHistory(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        /**
         * This method removes OrderCreation view from the visualization and adds ProviderSelection view
         * @param c OrderCreation view controller
         */
        public void fromOrderCreationToProviderSelection(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
        
        /**
         * This method removes OrderCreation view from the visualization and adds CurrentOrderView view
         * @param c OrderCreation view controller
         */
        public void fromOrderCreationToOrderViewer(OrderCreationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerCurrentOrderController(customerId);
        }
        
        /**
         * This method removes CurrentOrderView view from the visualization and adds CustomerLogIn view
         * @param c CurrentOrderView view controller
         */
        public void fromOrderViewertoLogIn(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
        /**
         * This method removes CurrentOrderView view from the visualization and adds CustomerUpdate view
         * @param c CurrentOrderView view controller
         */
        public void fromOrderViewertoUpdate(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
        /**
         * This method removes CurrentOrderView view from the visualization and adds UpdateBalance view
         * @param c CurrentOrderView view controller
         */
        public void fromOrderViewertoToBalance(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        /**
         * This method removes CurrentOrderView view from the visualization and adds CustomerFrameHistory1 view
         * @param c CurrentOrderView view controller
         */
        public void fromOrderViewertoHistory(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerOrderHistoryController(customerId);
        }
        
        /**
         * This method removes CurrentOrderView view from the visualization and adds ProviderSelection view
         * @param c CurrentOrderView view controller
         */
        public void fromOrderViewerToProviderSelection(CustomerCurrentOrderController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerProviderSelectionController(customerId);
        }
        
        /**
         * This method removes CustomerFrameHistory1 view from the visualization and adds UpdateBalance view
         * @param c CustomerFramwHistory1 view controller
         */
        public void fromHistory1ToBalance(CustomerOrderHistoryController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        /**
         * This method removes CustomerFrameHistory1 view from the visualization and adds CustomerUpdate view
         * @param c CustomerFramwHistory1 view controller
         */
        public void fromHistory1ToUpdate(CustomerOrderHistoryController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        
        /**
         * This method removes CustomerFrameHistory1 view from the visualization and adds CustomerLogIn view
         * @param c CustomerFramwHistory1 view controller
         */
         public void fromHistory1ToLogIn(CustomerOrderHistoryController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
         /**
         * This method removes CustomerFrameHistory1 view from the visualization and adds CurrentOrderView view
         * @param c CustomerFramwHistory1 view controller
         */
        public void fromHistory1ToOrderViewer(CustomerOrderHistoryController c){
           customerId=c.getCustomerId();          
           c.disposeView();        
           new CustomerCurrentOrderController(customerId);
        }
        
        /**
         * This method removes CustomerFrameHistory1 view from the visualization and adds ProviderSelection view
         * @param c CustomerFramwHistory1 view controller
         */
        public void fromHistory1ToProviderSelection(CustomerOrderHistoryController c){
           c.disposeView();
            customerId=c.getCustomerId();
            new CustomerProviderSelectionController(customerId);
        }
        
        /**
         * This method removes CustomerFrameHistory1 view from the visualization and adds MoreOrderInformation view
         * @param c CustomerFramwHistory1 view controller
         */
        public void fromHistory1ToMoreInfo(CustomerOrderHistoryController c){
           c.disposeView();
            customerId=c.getCustomerId();
            OrderDto selectedOrder = c.getSelectedOrder();
            new OrderMoreInformationController(customerId, selectedOrder);
        }
        
        /**
         * This method MoreOrderInformation view from the visualization and adds UpdateBalance view
         * @param c MoreOrderInformation view controller
         */
        public void fromMoreInfoToBalance(OrderMoreInformationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerBalanceController(customerId);
        }
        
        /**
         * This method MoreOrderInformation view from the visualization and adds CustomerUpdate view
         * @param c MoreOrderInformation view controller
         */
        public void fromMoreInfoToUpdate(OrderMoreInformationController c){
           c.disposeView();
           customerId=c.getCustomerId();
           new CustomerUpdateController(customerId);
        }
        /**
         * This method MoreOrderInformation view from the visualization and adds CustomerLogIn view
         * @param c MoreOrderInformation view controller
         */
         public void fromMoreInfoToLogIn(OrderMoreInformationController c){
           c.disposeView();
           customerId=null;
           new CustomerLogInController();
        }
        
         /**
         * This method MoreOrderInformation view from the visualization and adds CurrentOrderView view
         * @param c MoreOrderInformation view controller
         */
        public void fromMoreInfoToOrderViewer(OrderMoreInformationController c){
           customerId=c.getCustomerId();
           
           c.disposeView();
           
           new CustomerCurrentOrderController(customerId);
        }
        
        /**
         * This method MoreOrderInformation view from the visualization and adds ProviderSelection view
         * @param c MoreOrderInformation view controller
         */
        public void fromMoreInfoToProviderSelection(OrderMoreInformationController c){
           c.disposeView();
            customerId=c.getCustomerId();
            new CustomerProviderSelectionController(customerId);
        }
        
        /**
         * This method MoreOrderInformation view from the visualization and adds CustomerFrameHistory1 view
         * @param c MoreOrderInformation view controller
         */
        public void fromMoreInfoToHistory(OrderMoreInformationController c){
           c.disposeView();
            customerId=c.getCustomerId();
            new CustomerOrderHistoryController(customerId);
        }
        
        
}
