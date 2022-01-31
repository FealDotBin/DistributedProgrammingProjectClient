/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.customerclient.api;


import com.mycompany.common.model.Credentials;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.customerclient.model.CustomerEntity;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

/**
 *
 * @author Amos
 */
public interface ServiceApi {
    
    @GET("customer/{customer-id}/myinfo")
    Call<CustomerEntity> getCustomer(@Path("customer-id") Long customerId);
    
    
    @POST("customer/login")
    Call<Long> login(@Body Credentials credentials);
    
    
    @POST("customer/postCustomer")
    Call<CustomerEntity> createCustomer(@Body CustomerEntity customer);
    
    @PUT("customer/putCustomer")
    Call<CustomerEntity> updateCustomer(@Body CustomerEntity customer);
    
    @GET("customer/avail-providers")
    Call<List<ProviderDto>> getAvailableProviderDTO();
    
    @GET("customer/{customer-id}/current-order")
    Call<OrderDto> getCurrentOrderDTO(@Path("customer-id") Long customerId);
    
}
