package com.example.Pokedex.network;

import com.example.Pokedex.dto.PokemonDetailResponse;
import com.example.Pokedex.dto.TipoDetailResponse;
import com.example.Pokedex.dto.TipoListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PokeAPI {

    @GET("api/v2/type")
    Call<TipoListResponse> getTypes();

    @GET("api/v2/type/{tipo}")
    Call<TipoDetailResponse> getPokemonsByType(@Path("tipo") String tipo);

    @GET("api/v2/pokemon/{nombre}")
    Call<PokemonDetailResponse> getPokemon(@Path("nombre") String nombre);
}