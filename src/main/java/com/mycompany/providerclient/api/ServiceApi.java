/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.providerclient.api;

import com.mycompany.common.model.Credentials;
import com.mycompany.providerclient.model.ProviderEntity;
import retrofit2.Call;
import retrofit2.http.*;

/**
 *
 * @author aferr
 */
public interface ServiceApi {
    
    @POST("provider/login")
    Call<Long> login(@Body Credentials credentials);
        
    @POST("provider/postProvider")
    Call<ProviderEntity> createNewProvider(@Body ProviderEntity provider);
}
