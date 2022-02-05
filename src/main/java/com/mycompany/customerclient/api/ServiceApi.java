/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.customerclient.api;



import com.mycompany.common.model.Credentials;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.common.model.dto.user.ProviderDto;
import com.mycompany.customerclient.model.CustomerEntity;
import com.mycompany.customerclient.model.OrderEntity;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * This interface define all the REST APIs that a costumer can call 
 * @author Catello
 */
public interface ServiceApi {
    /**
     * This call is a GET that allows the customer to get his own informations
     * @param customerId is the customer ID
     * @return the CustomerEntity
     */
    @GET("customer/{customer-id}/myinfo")
    Call<CustomerEntity> getCustomer(@Path("customer-id") Long customerId);
    
    /**
     * This call is a GET that allows the customer to get all available providers
     * @return list of all available providers
     */
    @GET("customer/avail-providers")
    Call<List<ProviderDto>> getAvailableProviderDTO();
    
    /**
     * This call is a GET that allows the customer to get his current order
     * @param customerId is the costumer ID
     * @return customer's current order
     */
    @GET("customer/{customer-id}/current-order")
    Call<OrderDto> getCurrentOrderDTO(@Path("customer-id") Long customerId);
    
    /**
     * This call is a GET that allows the customer to get his order history
     * @param customerId is the costumer ID
     * @return list of past customer's orders
     */
    @GET("customer/{customer-id}/myorders")
    Call<List<OrderDto>> getCustomerHistory(@Path("customer-id") Long customerId);
    
    /**
     * This call is a GET that allows the customer to get a specific order
     * @param orderId is the order ID that you want to obtain from server
     * @return order with identifier equals to orderId
     */
    @GET("customer/order/{order-id}")
    Call<OrderDto> getOrderDTO(@Path("order-id") Long orderId);
    
    /**
     * This call is a POST that allows the customer to log in
     * @param credentials object that contains username and hashed password
     * @return customer's ID
     */
    @POST("customer/login")
    Call<Long> login(@Body Credentials credentials);
    
    /**
     * This call is a POST that allows the customer to register in the application
     * @param customer object that contains all necessary information abount a customer
     * @return customer created on server 
     */
    @POST("customer/postCustomer")
    Call<CustomerEntity> createCustomer(@Body CustomerEntity customer);
    
    /**
     * This call is a POST that allows the customer to place an order
     * @param order object that contains all necessary information abount the order
     * @return order create on server
     */
    @POST("customer/postOrder")
    Call<OrderDto> createOrder(@Body OrderEntity order);
    
    /**
     * This call is a PUT that allows the customer to modify his account information
     * @param customer object that contains all updated information abount a customer
     * @return customer modified on server
     */
    @PUT("customer/putCustomer")
    Call<CustomerEntity> updateCustomer(@Body CustomerEntity customer);
    
    /**
     * This call is a PUT that allows the customer to increase his balance
     * @param customerId is the costumer ID
     * @param value amount of money that the customer wants to add to his current balance
     * @return 
     */
    @PUT("customer/{customer-id}/balance")
    Call<Void> increaseBalance(@Path("customer-id") Long customerId, @Query ("value") double value);
    

    
    
    
}
