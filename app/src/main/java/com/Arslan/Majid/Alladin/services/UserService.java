package com.Arslan.Majid.Alladin.services;

import java.util.List;

import com.Arslan.Majid.Alladin.entities.Users;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface  UserService {

    @GET("findall")
    Call<List<Users>> findAll();

    @GET("find/{id}")
    Call<Users> find(@Query("Id") int id);

    @Headers("Content-Type: application/json")
    @GET("findbylogin/{email}/{password}")
    Call<String> findbylogin(@Path("email") String Email, @Path("password") String Password);

    @POST("create")
    Call<Void> create(@Body Users user);

    @PUT("update")
    Call<Void> update(@Body Users user);

    @DELETE("delete/{id}")
    Call<Void> delete(@Query("Id") int id);
}
