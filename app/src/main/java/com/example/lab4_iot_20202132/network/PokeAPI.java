package com.example.lab4_iot_20202132.network;

import com.example.lab4_iot_20202132.dto.TipoResponse;

public interface PokeAPI {
    @Get("api/v2/type")
    Call<TipoResponse> getCategories();

    @Get("api/v2/type")
    Call<TipoResponse> lookup(@Query("tipo") String tipoPokemon);

    @GET("api/v2/pokemon")
    Call<DetailResponse> lookup(@Query("id") String idPokemon);
}
