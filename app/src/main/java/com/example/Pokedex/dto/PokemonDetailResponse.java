package com.example.Pokedex.dto;

public class PokemonDetailResponse {
    private int id;
    private String name;
    private int base_experience;
    private int height;
    private int weight;

    public int getId() { return id; }
    public String getName() { return name; }
    public int getBaseExperience() { return base_experience; }
    public int getHeight() { return height; }
    public int getWeight() { return weight; }
}