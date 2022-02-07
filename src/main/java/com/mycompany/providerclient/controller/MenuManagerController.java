/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.controller;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.components.JTextFieldPlaceholder;
import com.mycompany.common.components.NoEditableTableModel;
import com.mycompany.common.model.dao.order.DishEntity;
import com.mycompany.common.model.dao.order.MenuEntity;
import com.mycompany.providerclient.api.ServiceApi;
import com.mycompany.providerclient.navigator.Navigator;
import com.mycompany.providerclient.view.MenuManagerView;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Represents the controller associated to MenuManagerView class.
 * It is responsible of taking the user's input to add, delete or modify
 * the menu's dishes.
 * It is also responsible of showing the dishes contained into the menu.
 * Whenever it's necessary to send or retrieve datas to/from server, the
 * API endpoint (ServiceApi class) is being used.
 * @author aferr
 */
public class MenuManagerController {
    
    private Long providerId;
    private MenuManagerView menuManagerView;
    private JTable menuTable;
    private JTextFieldPlaceholder dishNameTextField;
    private JTextFieldPlaceholder priceTextField;
    private JTextArea descriptionTextArea;
    private JTextArea ingredientsTextArea;
    private JButton deleteBtn;
    private JButton updateBtn;
    private JButton addBtn;
    private JButton homeBtn;
    private JButton updateAccountBtn;
    private JButton logOutBtn;
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private Navigator navigator;
    private MenuEntity menu;
    private DishEntity selectedDish;
    private int selectedDishIndex;
    
    /**
     * Initialize both the view and the controller by disabling the "update" and
     * "delete" buttons, showing all menu's content on the table and attaching
     * all necessary listeners to the buttons.
     * @param providerId the provider's id
     */
    public MenuManagerController(Long providerId){
        this.providerId = providerId;
        
        // initialize view
        menuManagerView = new MenuManagerView();
        menuManagerView.setVisible(true);
        
        // get components from view
        menuTable = menuManagerView.getMenuTable();
        dishNameTextField = menuManagerView.getDishNameTextField();
        priceTextField = menuManagerView.getPriceTextField();
        descriptionTextArea = menuManagerView.getDescriptionTextArea();
        ingredientsTextArea = menuManagerView.getIngredientsTextArea();
        deleteBtn = menuManagerView.getDeleteBtn();
        updateBtn = menuManagerView.getUpdateBtn();
        addBtn = menuManagerView.getAddBtn();
        homeBtn = menuManagerView.getHomeBtn();
        updateAccountBtn = menuManagerView.getUpdateAccountBtn();
        logOutBtn = menuManagerView.getLogOutBtn();
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // get navigator
        navigator = Navigator.getInstance();
        
        // disable update and delete buttons
        disableButtons();
        
        // fetch menu from server and show it on table
        getMenuCall();
        
        // attach listeners
        attachListenerOnAddBtn();
        attachListenerOnMenuTable();
        attachListenerOnDeleteBtn();
        attachListenerOnUpdateBtn();
        attachListenerOnHomeBtn();
        attachListenerOnUpdateAccountBtn();
        attachListenerOnLogOutBtn();
    }
    
    /**
     * Destroy the homeView instance and all its components.
     */
    public void disposeView(){
        menuManagerView.dispose();
    }
    
    /**
     * Getter for providerId
     * @return the provider's id
     */
    public Long getProviderId(){
        return providerId;
    }
    
    /**
     * Shows a pop up to inform the user that the informations that is typing are not correct
     */ 
    private void fieldErrorPane(String errorMessage){
        JOptionPane.showMessageDialog(menuManagerView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Clear dish's name text field, dish's price text field, dish's description
     * text area and dish's ingredients text area
     */
    private void clearAllFields(){
        dishNameTextField.clear();
        priceTextField.clear();
        descriptionTextArea.setText("");
        ingredientsTextArea.setText("");
    }
    
    /**
     * Disable delete and update buttons
     */
    private void disableButtons(){
        deleteBtn.setEnabled(false);
        updateBtn.setEnabled(false);
    }
    
    /**
     * Enable delete and update buttons
     */
    private void enableButtons(){
        deleteBtn.setEnabled(true);
        updateBtn.setEnabled(true);
    }
    
    /**
     * Show all the dishes contained into the menu on the menuTable.
     * For each dish, its name, price, description and ingredients are displayed.
     */
    private void addMenuOnTable(){
        List<DishEntity> dishList = menu.getDishEntities();
        if(dishList != null){
            for(DishEntity dish : dishList){
                addDishOnTable(dish);
            }
        }
    }
    
    /**
     * Show a dish's name, price, description and ingredients on the menuTable.
     */
    private void addDishOnTable(DishEntity dish){
        NoEditableTableModel menuTableModel = (NoEditableTableModel) menuTable.getModel();
        Object[] dishRow = new Object[4];
        dishRow[0] = dish.getName();
        dishRow[1] = dish.getDescription();
        dishRow[2] = dish.getIngredients();
        dishRow[3] = dish.getPrice();
        menuTableModel.addRow(dishRow);
    }
    
    /**
     * Update the dish at the given row with the given dish's infos.
     * @param rowIndex the menuTable's row number associated to the 
     * dish that has to be updated
     * @param dish the updated dish whose infos have to be displayed on the table
     */
    private void updateDishOnTable(int rowIndex, DishEntity dish){
        NoEditableTableModel menuTableModel = (NoEditableTableModel) menuTable.getModel();
        menuTableModel.removeRow(selectedDishIndex);
        Object[] dishRow = new Object[4];
        dishRow[0] = dish.getName();
        dishRow[1] = dish.getDescription();
        dishRow[2] = dish.getIngredients();
        dishRow[3] = dish.getPrice();
        menuTableModel.insertRow(rowIndex, dishRow);
    }
    
    /**
     * Send a GET request to the server to get the provider's menu.
     * If the response is successful, the provider's menu is shown on
     * the menuTable;
     * otherwise, an error message is shown to the user.
     */
    private void getMenuCall(){
        Call<MenuEntity> getMenuCall = serviceApi.getMenu(providerId);
        getMenuCall.enqueue(new Callback<MenuEntity>(){
            @Override
            public void onResponse(Call<MenuEntity> call, Response<MenuEntity> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    menu = (MenuEntity) response.body();
                    addMenuOnTable();
                }
                else{ // in caso di errori
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(
                                menuManagerView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<MenuEntity> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
            }
        });   
    }
    
    /**
     * Send a POST request to the server to create a new dish.
     * If the response is successful, the dish has been created successfully
     * and it's displayed on the menuTable;
     * otherwise, an error message is shown to the user.
     */
    private void addDishCall(DishEntity dish){
        Call<DishEntity> addDishCall = serviceApi.addDish(providerId, dish);
        addDishCall.enqueue(new Callback<DishEntity>(){
            @Override
            public void onResponse(Call<DishEntity> call, Response<DishEntity> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    DishEntity addedDish = (DishEntity) response.body();
                    menu.getDishEntities().add(addedDish);
                    JOptionPane.showMessageDialog(
                                menuManagerView,
                                "Dish added successfully",
                                "ERROR",
                                JOptionPane.INFORMATION_MESSAGE);
                    // update view
                    clearAllFields();
                    addDishOnTable(addedDish);
                    disableButtons();

                    // clear selectedDish infos
                    selectedDishIndex = -1;
                    selectedDish = null;
                }
                else{ // in caso di errori
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(
                                menuManagerView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<DishEntity> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
            }
        });
    }
    
    /**
     * Send a DELETE request to the server to delete a dish.
     * If the response is successful, the dish has been deleted successfully
     * and it's removed from the menuTable;
     * otherwise, an error message is shown to the user.
     */
    private void deleteDishCall(){
        Call<Void> deleteDishCall = serviceApi.deleteDish(providerId, selectedDish.getId());
        deleteDishCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.isSuccessful()){ // status code tra 200-299

                    menu.getDishEntities().remove(selectedDishIndex);
                    JOptionPane.showMessageDialog(
                                menuManagerView,
                                "Dish removed successfully",
                                "ERROR",
                                JOptionPane.INFORMATION_MESSAGE);
                    // update view
                    clearAllFields();
                    NoEditableTableModel menuTableModel = (NoEditableTableModel) menuTable.getModel();
                    menuTableModel.removeRow(selectedDishIndex);
                    disableButtons();

                    // clear selected dish infos
                    selectedDishIndex = -1;
                    selectedDish = null;
                }
                else{ // in caso di errori
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(
                                menuManagerView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
            }
        });
    }
    
    /**
     * Send a PUT request to the server to updated a dish.
     * If the response is successful, the dish has been updated successfully
     * and it's shown up to date on the menuTable;
     * otherwise, an error message is shown to the user.
     */
    private void updateDishCall(DishEntity updatedDish){
        Call<DishEntity> updateDishCall = serviceApi.updateDish(providerId, updatedDish);
        updateDishCall.enqueue(new Callback<DishEntity>(){
            @Override
            public void onResponse(Call<DishEntity> call, Response<DishEntity> response) {

                if(response.isSuccessful()){ // status code tra 200-299

                    selectedDish.setName(updatedDish.getName());
                    selectedDish.setDescription(updatedDish.getDescription());
                    selectedDish.setIngredients(updatedDish.getIngredients());
                    selectedDish.setPrice(updatedDish.getPrice());
                    JOptionPane.showMessageDialog(
                                menuManagerView,
                                "Dish updated successfully",
                                "ERROR",
                                JOptionPane.INFORMATION_MESSAGE);
                    // update view
                    clearAllFields();
                    updateDishOnTable(selectedDishIndex, selectedDish);
                    disableButtons();

                    // clear selectedDish infos
                    selectedDishIndex = -1;
                    selectedDish = null;
                }
                else{ // in caso di errori
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        JOptionPane.showMessageDialog(
                                menuManagerView,
                                jObjError.get("message"),
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(LogInController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onFailure(Call<DishEntity> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
            }
        });
    }
    
    /**
     * Attach a ListSelectionListener to menuTable in order to 
     * load the dish's name text field, dish's price text field,
     * dish's description text area and dish's ingredients text area
     * with the selected dish's infos, whenever a dish is selected.
     */
    private void attachListenerOnMenuTable(){
        menuTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            
            @Override
            public void valueChanged(ListSelectionEvent event) {
                
                selectedDishIndex = menuTable.getSelectedRow();
                if(!event.getValueIsAdjusting() && selectedDishIndex >= 0){
                    
                    // get selectedDish's fields
                    selectedDish = menu.getDishEntities().get(selectedDishIndex);
                    String dishName = selectedDish.getName();
                    String description = selectedDish.getDescription();
                    String ingredientsWithBrackets = selectedDish.getIngredients().toString();
                    String ingredientsWithoutBrackets = ingredientsWithBrackets.substring(1, ingredientsWithBrackets.length() - 1);
                    Double price = selectedDish.getPrice();
                    
                    // show fields on bottom components
                    dishNameTextField.setText(dishName);
                    descriptionTextArea.setText(description);
                    ingredientsTextArea.setText(ingredientsWithoutBrackets);
                    priceTextField.setText(price.toString());
                    
                    // enable buttons
                    enableButtons();
                }
            }
        });
    }
    
    /**
     * Attach an ActionListener to addButton in order to 
     * add a new dish containing the infos provided by the user
     * through the text fields and text areas.
     */
    private void attachListenerOnAddBtn(){
        addBtn.addActionListener(event -> {
            String dishName = dishNameTextField.getText(true).trim();
            if (dishName.isBlank()) {
                    fieldErrorPane("Dish name cannot be blank");
                    return;
            }
            
            String description = descriptionTextArea.getText().trim();
            if (description.isBlank()) {
                    fieldErrorPane("Description cannot be blank");
                    return;
            }
            
            String ingredientsString = ingredientsTextArea.getText().trim();
            LinkedList<String> ingredientsList = new LinkedList<>();
            if (ingredientsString.isBlank()) {
                    fieldErrorPane("Ingredients cannot be blank");
                    return;
            }
            else{
                String ingredientsArray[] = ingredientsString.split(",");
                for (String ingredient : ingredientsArray){
                    ingredientsList.add(ingredient.trim());
                }
            }
            
            String priceString = priceTextField.getText(true).trim();
            Double priceDouble = null;
            if (priceString.isBlank()) {
                    fieldErrorPane("Username cannot be blank");
                    return;
            }
            else{
                priceDouble = Double.parseDouble(priceString);
            }
            
            // send dish to server
            DishEntity dish = new DishEntity(dishName, description, ingredientsList, priceDouble);
            addDishCall(dish);
        });
    }
    
    /**
     * Attach an ActionListener to deleteBtn in order to 
     * delete a dish selected by the user.
     */
    private void attachListenerOnDeleteBtn(){
        deleteBtn.addActionListener(event -> {
            deleteDishCall();
        });
    }
    
    /**
     * Attach an ActionListener to updateBtn in order to 
     * update the selected dish containing the infos provided by the user
     * through the text fields and text areas.
     */
    private void attachListenerOnUpdateBtn(){
        updateBtn.addActionListener(event -> {
            
            // get dish's fields from input
            String dishName = dishNameTextField.getText(true).trim();
            if (dishName.isBlank()) {
                    fieldErrorPane("Dish name cannot be blank");
                    return;
            }
            
            String description = descriptionTextArea.getText().trim();
            if (description.isBlank()) {
                    fieldErrorPane("Description cannot be blank");
                    return;
            }
            
            String ingredientsString = ingredientsTextArea.getText().trim();
            LinkedList<String> ingredientsList = new LinkedList<>();
            if (ingredientsString.isBlank()) {
                    fieldErrorPane("Ingredients cannot be blank");
                    return;
            }
            else{
                String ingredientsArray[] = ingredientsString.split(",");
                for (String ingredient : ingredientsArray){
                    ingredientsList.add(ingredient.trim());
                }
            }
            
            String priceString = priceTextField.getText(true).trim();
            Double priceDouble = null;
            if (priceString.isBlank()) {
                    fieldErrorPane("Username cannot be blank");
                    return;
            }
            else{
                priceDouble = Double.parseDouble(priceString);
            }
            
            // send update request to server
            DishEntity updatedDish = new DishEntity(selectedDish.getId(), 
                    dishName, description, ingredientsList, priceDouble);
            updateDishCall(updatedDish);
        });
    }
    
    /**
     * Attach an ActionListener to homeButton in order to switch to
     * the "home" window, when the button is pressed.
     */
    private void attachListenerOnHomeBtn(){
        homeBtn.addActionListener(event -> {
            navigator.fromMenuManagerToHome(MenuManagerController.this);
        });
    }
    
    /**
     * Attach an ActionListener to updateAccountButton in order to switch to
     * the "update account" window, when the button is pressed.
     */
    private void attachListenerOnUpdateAccountBtn(){
        updateAccountBtn.addActionListener(event -> {
            navigator.fromMenuManagerToUpdateAccount(MenuManagerController.this);
        });
    }
    
    /**
     * Attach an ActionListener to logOutButton in order to switch to
     * the "log in" window, when the button is pressed.
     */
    private void attachListenerOnLogOutBtn(){
        logOutBtn.addActionListener(event -> {
            navigator.fromMenuManagerToLogIn(MenuManagerController.this);
        });
    }
}
