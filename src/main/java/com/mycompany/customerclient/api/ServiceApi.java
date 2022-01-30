/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.customerclient.api;


import com.mycompany.customerclient.model.CustomerEntity;
import retrofit2.Call;
import retrofit2.http.*;

/**
 *
 * @author Amos
 */
public interface ServiceApi {
    
    @GET("customer/{customer-id}/myinfo")
    Call<CustomerEntity> getCustomer(@Path("customer-id") int customerId);
    
    @POST("customer/postCustomer")
    Call<CustomerEntity> createCustomer(@Body CustomerEntity customer);
    
    @PUT("customer/putCustomer")
    Call<CustomerEntity> updateCustomer(@Body CustomerEntity customer);
    
}
