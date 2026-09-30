package com.example.lab4_iot_20202132.dto;

import java.util.ArrayList;

public class PokemonResponse {
    private ArrayList<PokemonDetail> pokemons;

    public ArrayList<PokemonDetail> getPokemons() {
        return pokemons;
    }

    public static class PokemonDetail{

        private String idPokemon;

        private String strPokemon;

        private String base_experience;

        private String height;

        private String is_default;
        private String order;
        private String weight;

        private String strAbilitie1;
        private String strAbilitie2;
        private String strAbilitie3;
        private String strAbilitie4;

        public String getIdPokemon() {
            return idPokemon;
        }

        public String getStrPokemon() {
            return strPokemon;
        }

        public String getBase_experience() {
            return base_experience;
        }

        public String getHeight() {
            return height;
        }

        public String getIs_default() {
            return is_default;
        }

        public String getOrder() {
            return order;
        }

        public String getWeight() {
            return weight;
        }

        public String[] getabilities() {
            return new String[]{strAbilitie1, strAbilitie2, strAbilitie3, strAbilitie4};
        }
    }

}
