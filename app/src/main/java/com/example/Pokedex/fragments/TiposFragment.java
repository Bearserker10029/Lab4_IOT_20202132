package com.example.Pokedex.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.Pokedex.R;
import com.example.Pokedex.adapter.TipoAdapter;
import com.example.Pokedex.databinding.FragmentTiposBinding;
import com.example.Pokedex.dto.TipoListResponse;
import com.example.Pokedex.network.PokeAPI;
import com.example.Pokedex.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiposFragment extends Fragment {

    private FragmentTiposBinding binding;
    private final PokeAPI api = RetrofitClient.get().create(PokeAPI.class);
    private final List<TipoListResponse.Result> tipos = new ArrayList<>();
    private TipoAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTiposBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TipoAdapter(tipos, tipo -> {
            Bundle bundle = new Bundle();
            bundle.putString("tipo", tipo);
            Navigation.findNavController(view)
                    .navigate(R.id.action_tipos_to_pokemon, bundle);
        });
        binding.recyclerPokemons.setAdapter(adapter);
        cargarTipos();
    }

    private void cargarTipos() {
        api.getTypes().enqueue(new Callback<TipoListResponse>() {
            @Override
            public void onResponse(Call<TipoListResponse> call,
                                   Response<TipoListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tipos.clear();
                    tipos.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TipoListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}