package com.example.lab4_iot_20202132.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>{

    public interface OnItemClickListener {
        void onClick(PokemonsResponse.Pokemon categoria);
    }

    private final List<PokemonsResponse.Pokemon> lista;
    private final OnItemClickListener listener;

    public TipoAdapter(List<PokemonsResponse.Pokemon> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPokemonBinding binding = ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PokemonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonAdapter.PokemonViewHolder holder, int position) {
        
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        PokemonsResponse.Pokemon categoria = lista.get(position);
        holder.binding.textName.setText(categoria.getStrPokemon());
        holder.binding.textDescription.setText(categoria.getStrPokemonDescription());
        Glide.with(holder.binding.imagePokemon.getContext())
                .load(categoria.getStrPokemonThumb())
                .into(holder.binding.imagePokemon);
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(categoria));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        final ItemPokemonBinding binding;

        PokemonViewHolder(ItemPokemonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
