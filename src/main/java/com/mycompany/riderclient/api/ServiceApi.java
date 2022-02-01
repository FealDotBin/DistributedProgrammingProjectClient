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
 *
 * @author Amos
 */
public interface ServiceApi {
    
     
    @POST("rider/login")
    Call<Long> login(@Body Credentials credentials);
    
    @PUT("rider/updateRider")
    Call<RiderEntity> updateRider(@Body RiderEntity rider);
    
    @GET("rider/{rider-id}/myinfo")
    Call<RiderEntity> getMyInfo(@Path("rider-id") Long riderId);
    
    
    @POST("rider/postRider")
    Call<RiderEntity> createRider(@Body RiderEntity rider);
    
    
    @GET("rider/getSemiAcceptedOrders")
    Call<List<OrderDto>> getSemiAcceptedOrders();
    
    @PUT("rider/{rider-id}/acceptOrder")
    Call<Void> acceptOrder(@Path("rider-id") Long riderId,@Query("id") Long orderId);
    
    @PUT("rider/shipOrder")
    Call<Void> putOrderOnShipped(@Query("id") Long orderId);
    
    @PUT("rider/deliveredOrder")
    Call<Void> completeOrder(@Query("id") Long orderId);
    
    
    
    
}
