package com.example.Pokedex.dto;

import java.util.List;

public class TipoDetailResponse {
    private List<Entry> pokemon;

    public List<Entry> getPokemon() { return pokemon; }

    public static class Entry {
        private TipoListResponse.Result pokemon; // {name, url}
        private int slot;

        public TipoListResponse.Result getPokemon() { return pokemon; }
    }
}