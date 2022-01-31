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
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
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
    private RetrofitBuilder retroBuild;
    private ServiceApi serviceApi;
    private Navigator navigator;
    private MenuEntity menu;
    private DishEntity selectedDish;
    
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
        
        // initialize retrofitBuilder and serviceApi
        retroBuild = new RetrofitBuilder();
        serviceApi = retroBuild.getRetrofit().create(ServiceApi.class);
        
        // get navigator
        navigator = Navigator.getInstance();
        
        // fetch menu from server and show it on table
        fetchMenuFromServer();
        
        // attach listener on addBtn
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
                ingredientsList.addAll(Arrays.asList(ingredientsArray));
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
                        showDishOnTable(addedDish);
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
        });
    }
    
    private void fetchMenuFromServer(){
        Call<MenuEntity> getMenuCall = serviceApi.getMenu(providerId);
        getMenuCall.enqueue(new Callback<MenuEntity>(){
            @Override
            public void onResponse(Call<MenuEntity> call, Response<MenuEntity> response) {

                if(response.isSuccessful()){ // status code tra 200-299
                    menu = (MenuEntity) response.body();
                    showMenuOnTable();
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
    
    private void showMenuOnTable(){
        List<DishEntity> dishList = menu.getDishEntities();
        if(!dishList.isEmpty()){
            for(DishEntity dish : dishList){
                showDishOnTable(dish);
            }
        }
    }
    
    private void showDishOnTable(DishEntity dish){
        NoEditableTableModel allOrdersTableModel = (NoEditableTableModel) menuTable.getModel();
        Object[] dishRow = new Object[4];
        dishRow[0] = dish.getName();
        dishRow[1] = dish.getDescription();
        dishRow[2] = dish.getIngredients();
        dishRow[3] = dish.getPrice();
        allOrdersTableModel.addRow(dishRow);
    }
    
    //Shows a pop up to inform the user that the informations that is typing are not correct
    private void fieldErrorPane(String errorMessage){
        JOptionPane.showMessageDialog(menuManagerView, errorMessage, "Field error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void clearAllFields(){
        dishNameTextField.clear();
        priceTextField.clear();
        descriptionTextArea.setText("");
        ingredientsTextArea.setText("");
    }
    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuManagerController c = new MenuManagerController(1L);
            }
        });
    }
}
