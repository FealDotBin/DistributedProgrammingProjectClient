/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.riderclient.api;

import com.mycompany.common.model.Credentials;
import com.mycompany.common.model.dto.order.OrderDto;
import com.mycompany.riderclient.model.RiderEntity;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * This interface define all the REST APIs that a rider can call 
 * @author Amos
 */
public interface ServiceApi {
    
    
     /**
     * This call is a GET that allows the rider to get their own informations
     * @param riderId is the rider ID
     * @return the RiderEntity
     */
    @GET("rider/{rider-id}/myinfo")
    Call<RiderEntity> getMyInfo(@Path("rider-id") Long riderId);
    
     /**
     * This call is a GET that allows the rider to get all the orders that 
     * have been accepted by providers, but do not yet have a rider
     * @return a list of OrderDto
     */
    @GET("rider/getSemiAcceptedOrders")
    Call<List<OrderDto>> getSemiAcceptedOrders();
    
    /**
     * This call is a GET that allows the rider to get all the orders that 
     * he have accepted. 
     * @return a list of OrderDto
     * @param riderId is the rider ID
     * @return a list of OrderDto
     */
    @GET("rider/{rider-id}/getAtLeastAcceptedOrders")            
    Call<List<OrderDto>> getAtLeastAccepted(@Path("rider-id") Long riderId);
    
    /**
     * This call is a GET that allows the rider to get a specific order
     * @param orderId is the order ID
     * @return an OrderDto
     */
    @GET("rider/order/{order-id}")
    Call<OrderDto> getOrderById(@Path("order-id") Long orderId);
     
    /**
     * This call is a POST that allows the rider to login
     * @param credentials consisting of username and password, which are encapsulated in the body
     * @return a Long that represent the rider ID
     */
    @POST("rider/login")
    Call<Long> login(@Body Credentials credentials);
    
     /**
     * This call is a POST that allows the rider to signup to the system
     * @param rider is the whole RiderEntity, which is passed into the body of the package
     * @return the RiderEntity just inserted in the server
     */
    @POST("rider/postRider")
    Call<RiderEntity> createRider(@Body RiderEntity rider);
    
    /**
     * This call is a PUT that allows the rider to update their own informations 
     * @param rider is the whole RiderEntity, which is passed into the body of the package
     * @return the RiderEntity just updated on the server
     */
    @PUT("rider/updateRider")
    Call<RiderEntity> updateRider(@Body RiderEntity rider); 
    
    /**
     * This call is a PUT that allows the rider to accept an order. 
     * It does not return anything, it is sufficient that the response code is 200 from the server
     * @param riderId
     * @param orderId
     */
    @PUT("rider/{rider-id}/acceptOrder")
    Call<Void> acceptOrder(@Path("rider-id") Long riderId,@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the rider to change the state of the order in "shipped".
     * The rider will call this function when ready to leave the restaurant with the order.
     * It does not return any object, it is sufficient that the response code is 200 from the server.
     * @param orderId
     */
    @PUT("rider/shipOrder")
    Call<Void> putOrderOnShipped(@Query("id") Long orderId);
    
    /**
     * This call is a PUT that allows the rider to change the state of the order in "completed".
     * The rider will call this function when the order has been concluded, that is, when he delivers the order to the customer
     * It does not return any object, it is sufficient that the response code is 200 from the server.
     * @param orderId
     * @param orderId
     * @return 
     */
    @PUT("rider/deliveredOrder")
    Call<Void> completeOrder(@Query("id") Long orderId);
       
}
