package com.example.franklindominguez.gruahelp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GruaService {
    @GET("api/values/getClients")
    Call<List<Client>> getCliets();

    @GET("api/values/ExistsImei")
    Call<Client> getClietByImei(@Query("imei") String imei);

    @GET("api/values/GetVehicles")
    Call<List<Vehicle>> getVehicle(@Query("imei") String imei);

    @POST("api/values/AddClient")
    Call<Client> addClient(@Body Client client);

    @POST("api/values/AddVehicle")
    Call<Vehicle> addVehicle(@Body Vehicle vehicle);

}
