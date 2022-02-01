/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.riderclient.test;

import com.mycompany.common.api.RetrofitBuilder;
import com.mycompany.common.model.Credentials;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.riderclient.api.ServiceApi;
import com.mycompany.riderclient.model.RiderEntity;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author Amos
 */
public class mainTest {
    
    public static void main(String args[]){
        
        RetrofitBuilder retroBuild = new RetrofitBuilder();
        
        ServiceApi apiService = retroBuild.getRetrofit().create(ServiceApi.class);
        
        RiderEntity r1 = new RiderEntity("riderinooX"
                + ""
                + "", "cane", "Pippo", "Peppe", 
                "2000-01-01", "fdfdk", "329-3792075", "pathok","bici") ;
        
         // Call<RiderEntity> call2 = apiService.createRider(r1); //CREATE
         
         //UPDATE
        // r1.setId(6L);
         Call<RiderEntity> call2 = apiService.updateRider(r1);
         
         //GET MY INFO
        call2 = apiService.getMyInfo(6L);
        
        
        //GET SEMI-ACCEPTED
        Call<List<OrderDto>> call4 = apiService.getSemiAcceptedOrders();
        
        call4.enqueue(new Callback<List<OrderDto>>() {         
            
            @Override
            public void onResponse(Call<List<OrderDto>> call, Response<List<OrderDto>> response) {
               
              
              if(response.isSuccessful()){ // status code tra 200-299
               List<OrderDto> c3= response.body();
               System.out.println(c3.toString());
              }
              
              
              else{ // in caso di errori
                  try {
                      JSONObject jObjError = new JSONObject(response.errorBody().string());
                      System.out.println(jObjError.get("message"));
                  } catch (IOException ex) {
                      Logger.getLogger(mainTest.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }
            }

            @Override
            public void onFailure(Call<List<OrderDto>> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
                
            }

        });
         
          
        call2.enqueue(new Callback<RiderEntity>() {         
            
            @Override
            public void onResponse(Call<RiderEntity> call, Response<RiderEntity> response) {
               
              
              if(response.isSuccessful()){ // status code tra 200-299
               RiderEntity c3= response.body();
               System.out.println(c3.toString());
              }
              
              
              else{ // in caso di errori
                  try {
                      JSONObject jObjError = new JSONObject(response.errorBody().string());
                      System.out.println(jObjError.get("message"));
                  } catch (IOException ex) {
                      Logger.getLogger(mainTest.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }
            }

            @Override
            public void onFailure(Call<RiderEntity> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
                
            }

        });
       
           
        Credentials credentials = new Credentials("riderinooX", "cane");
        Call<Long> call3 = apiService.login(credentials);
           
        
        call3.enqueue(new Callback<Long>() {         
            
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
               
              
              if(response.isSuccessful()){ // status code tra 200-299
               System.out.println(response.body());
              }
              
              
              else{ // in caso di errori
                  try {
                      JSONObject jObjError = new JSONObject(response.errorBody().string());
                      System.out.println(jObjError.get("message"));
                  } catch (IOException ex) {
                      Logger.getLogger(mainTest.class.getName()).log(Level.SEVERE, null, ex);
                  }
              }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                // Log error here since request failed
                  System.out.println("failed");
                
            }

        });
        
        
        //Accept Order
        Call<Void> call5 = apiService.completeOrder(7L);
        call5.enqueue(new Callback<Void>() {         
            
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
               
              
              if(response.isSuccessful()){ // status code tra 200-299
               System.out.println("200OK");
              }
              
              
              else{ // in caso di errori
                  try {
                      JSONObject jObjError = new JSONObject(response.errorBody().string());
                      System.out.println(jObjError.get("message"));
                  } catch (IOException ex) {
                      Logger.getLogger(mainTest.class.getName()).log(Level.SEVERE, null, ex);
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
}
