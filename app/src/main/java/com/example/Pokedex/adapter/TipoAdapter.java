package com.example.Pokedex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Pokedex.databinding.ItemTipoBinding;
import com.example.Pokedex.dto.TipoListResponse;

import java.util.List;

public class TipoAdapter extends RecyclerView.Adapter<TipoAdapter.TipoViewHolder> {

    public interface OnItemClickListener {
        void onClick(String tipo);
    }

    private final List<TipoListResponse.Result> lista;
    private final OnItemClickListener listener;

    public TipoAdapter(List<TipoListResponse.Result> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTipoBinding binding = ItemTipoBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new TipoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TipoViewHolder holder, int position) {
        TipoListResponse.Result tipo = lista.get(position);
        holder.binding.textName.setText(tipo.getName().toUpperCase());
        holder.binding.textDescription.setVisibility(View.GONE); // el item_tipo.xml ya lo tenías
        holder.binding.getRoot().setOnClickListener(v -> listener.onClick(tipo.getName()));
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class TipoViewHolder extends RecyclerView.ViewHolder {
        final ItemTipoBinding binding;
        TipoViewHolder(ItemTipoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}