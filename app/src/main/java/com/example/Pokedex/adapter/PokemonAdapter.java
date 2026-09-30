package com.example.Pokedex.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Pokedex.databinding.ItemPokemonBinding;
import com.example.Pokedex.dto.PokemonDetailResponse;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private final List<PokemonDetailResponse> lista;

    public PokemonAdapter(List<PokemonDetailResponse> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPokemonBinding binding = ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PokemonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        PokemonDetailResponse p = lista.get(position);
        holder.binding.textId.setText("ID: " + p.getId());
        holder.binding.textName.setText(p.getName().toUpperCase());
        holder.binding.textExp.setText("Base experience: " + p.getBaseExperience());
        holder.binding.textHeight.setText("Altura: " + p.getHeight());
        holder.binding.textWeight.setText("Peso: " + p.getWeight());
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        final ItemPokemonBinding binding;
        PokemonViewHolder(ItemPokemonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}