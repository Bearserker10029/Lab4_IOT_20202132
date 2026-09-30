package com.example.Pokedex.dto;

import java.util.List;

public class TipoListResponse {
    private int count;
    private List<Result> results;

    public List<Result> getResults() { return results; }

    public static class Result {
        private String name;
        private String url;

        public String getName() { return name; }
        public String getUrl() { return url; }
    }
}