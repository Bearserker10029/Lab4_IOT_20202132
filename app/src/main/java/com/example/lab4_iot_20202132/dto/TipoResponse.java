package com.example.lab4_iot_20202132.dto;

import java.util.ArrayList;

public class TipoResponse {
    private ArrayList<PokemonDetail> pokemon;

    public ArrayList<PokemonDetail> getPokemons() {
        return pokemon;
    }

    public static class PokemonDetail{
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
    }

}
