/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.api;

import com.mycompany.common.model.Credentials;
import com.mycompany.common.model.dao.order.DishEntity;
import com.mycompany.common.model.dao.order.MenuEntity;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.providerclient.model.ProviderEntity;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * This interface define all the REST APIs that a provider can call 
 * @author aferr
 */
public interface ServiceApi {
    
    /**
     * This call is a POST that allows the provider to login
     * @param credentials consisting of username and password, which are encapsulated in the body
     * @return a Long that represent the provider ID
     */
    @POST("provider/login")
    Call<Long> login(@Body Credentials credentials);
        
    /**
     * This call is a POST that allows the provider to signup to the system
     * @param provider is the whole ProviderEntity, which is passed into the body of the package
     * @return the ProviderEntity just inserted in the server
     */
    @POST("provider/postProvider")
    Call<ProviderEntity> createNewProvider(@Body ProviderEntity provider);
    
    /**
     * This call is a PUT that allows the provider to update their own informations 
     * @param provider is the whole ProviderEntity, which is passed into the body of the package
     * @return the ProviderEntity just updated on the server
     */
    @PUT("provider/putProvider")
    Call<ProviderEntity> putProvider(@Body ProviderEntity provider);
    
    /**
     * This call is a GET that allows the provider to get their own informations
     * @param providerId is the provider ID
     * @return the ProviderEntity
     */
    @GET("provider/{provider-id}/myinfo")
    Call<ProviderEntity> getMyInfo(@Path("provider-id") Long providerId);
    
    /**
     * This call is a PUT that allows the provider to update their availability
     * @param providerId is the provider ID
     * @param isAvailable is the provider's availability
     */
    @PUT("provider/{provider-id}/")
    Call<Void> setAvail(@Path("provider-id") Long providerId, @Query("availability") Boolean isAvailable);
    
    /**
     * This call is a GET that allows the provider to get all the pending orders
     * @param providerId is the provider ID
     * @return a list containing all the pending orders
     */
    @GET("provider/{provider-id}/getPendingOrders")
    Call<List<OrderDto>> getPendingOrders(@Path("provider-id") Long providerId);
    
    /**
     * This call is a GET that allows the provider to get all the accepted orders
     * @param providerId is the provider ID
     * @return a list containing all the accepted orders
     */
    @GET("provider/{provider-id}/getAcceptedOrders")
    Call<List<OrderDto>> getAcceptedOrders(@Path("provider-id") Long providerId);
    
    /**
     * This call is a GET that allows the provider to get all the semi-accepted 
     * orders, therefore that has been accepted by the provider but not yet 
     * by the raider
     * @param providerId is the provider ID
     * @return a list containing all the semi-accepted orders
     */
    @GET("provider/{provider-id}/getSemiAcceptedOrders")
    Call<List<OrderDto>> getSemiAcceptedOrders(@Path("provider-id") Long providerId);
    
    /**
     * This call is a GET that allows the provider to get all the shipped orders
     * @param providerId is the provider ID
     * @return a list containing all the shipped orders
     */
    @GET("provider/{provider-id}/getShippedOrders")
    Call<List<OrderDto>> getShippedOrders(@Path("provider-id") Long providerId);
    
    /**
     * This call is a GET that allows the provider to get all the completed orders
     * @param providerId is the provider ID
     * @return a list containing all the completed orders
     */
    @GET("provider/{provider-id}/getCompletedOrders")
    Call<List<OrderDto>> getCompletedOrders(@Path("provider-id") Long providerId);
    
    /**
     * This call is a GET that allows the provider to get all the refused orders
     * @param providerId is the provider ID
     * @return a list containing all the refused orders
     */
    @GET("provider/{provider-id}/getRefusedOrders")
    Call<List<OrderDto>> getRefusedOrders(@Path("provider-id") Long providerId);
    
    /**
     * This call is a GET that allows the provider to get their menu
     * @param providerId is the provider ID
     * @return the provider's menu
     */
    @GET("provider/{provider-id}/getMenu")
    Call<MenuEntity> getMenu(@Path("provider-id") Long providerId);
    
    /**
     * This call is a POST that allows the provider to add a new dish
     * to their menu
     * @param providerId is the provider ID
     * @param dish is the dish to be added
     * @return the dish that has been added
     */
    @POST("provider/{provider-id}/addDish")
    Call<DishEntity> addDish(@Path("provider-id") Long providerId, @Body DishEntity dish);
    
    /**
     * This call is a DELETE that allows the provider to remove an existing
     * dish from their menu
     * @param providerId is the provider ID
     * @param dishId is the dish ID
     */
    @DELETE("provider/{provider-id}/removeDish")
    Call<Void> deleteDish(@Path("provider-id") Long providerId, @Query("dish_id") Long dishId);
    
    /**
     * This call is a PUT that allows the provider to update an existing dish
     * in their menu
     * @param providerId is the provider ID
     * @param dish is the dish to be updated
     * @return the dish that has been updated
     */
    @PUT("provider/{provider-id}/updateDish")
    Call<DishEntity> updateDish(@Path("provider-id") Long providerId, @Body DishEntity dish);
    
    /**
     * This call is a PUT that allows the provider to accept a take-away order
     * @param orderId is the order ID
     */
    @PUT("provider/putTakeAwayOrder")
    Call<Void> putTakeAwayOrder(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the provider to accept an order which has
     * to be delivered by their delivery boy
     * @param orderId is the order ID
     */
    @PUT("provider/putNoRiderDeliveringOrder")
    Call<Void> putNoRiderDeliveringOrder(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the provider to accept an order which has 
     * to be delivered by the riders
     * @param orderId is the order ID
     */
    @PUT("provider/putRiderOrder")
    Call<Void> putRiderOrder(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the provider to refuse a take-away order
     * @param orderId is the order ID
     */
    @PUT("provider/refuseTakeAway")
    Call<Void> refuseTakeAway(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the provider to refuse an order which has 
     * to be delivered by their delivery boy
     * @param orderId is the order ID
     */
    @PUT("provider/refuseNoRider")
    Call<Void> refuseNoRider(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the provider to refuse an order which has
     * to be delivered by the riders
     * @param orderId is the order ID
     */
    @PUT("provider/refuseRider")
    Call<Void> refuseRider(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the provider to notify that the order
     * is in shipping
     * @param orderId is the order ID
     */
    @PUT("provider/putShipOrder")
    Call<Void> putShipOrder(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the provider to notify that the customer
     * has taken away the order
     * @param orderId is the order ID
     */
    @PUT("provider/putCompletedHandOrder")
    Call<Void> putCompletedHandOrder(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the provider to notify that the order
     * has been delivered to the customer
     * @param orderId is the order ID
     */
    @PUT("provider/putCompletedOrder")
    Call<Void> putCompletedOrder(@Query("id") Long orderId);
}
