package com.example.Pokedex.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.Pokedex.adapter.PokemonAdapter;
import com.example.Pokedex.databinding.FragmentPokemonsBinding;
import com.example.Pokedex.dto.PokemonDetailResponse;
import com.example.Pokedex.dto.TipoDetailResponse;
import com.example.Pokedex.network.PokeAPI;
import com.example.Pokedex.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonFragment extends Fragment implements SensorEventListener {

    private FragmentPokemonsBinding binding;
    private final PokeAPI api = RetrofitClient.get().create(PokeAPI.class);

    private final List<PokemonDetailResponse> pokemons = new ArrayList<>();
    private PokemonAdapter adapter;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long ultimaAgitacion = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPokemonsBinding.inflate(inflater, container, false);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new PokemonAdapter(pokemons);
        binding.recyclerPokemons.setAdapter(adapter);

        String tipo = getArguments() != null ? getArguments().getString("tipo") : null;
        if (tipo != null) {
            binding.textTitulo.setText("Pokémon tipo: " + tipo.toUpperCase());
            cargarPokemons(tipo);
        }
    }

    private void cargarPokemons(String tipo) {
        api.getPokemonsByType(tipo).enqueue(new Callback<TipoDetailResponse>() {
            @Override
            public void onResponse(Call<TipoDetailResponse> call,
                                   Response<TipoDetailResponse> response) {
                if (!response.isSuccessful() || response.body() == null
                        || response.body().getPokemon() == null) {
                    Toast.makeText(getContext(), "No se encontraron Pokémon",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                pokemons.clear();
                for (TipoDetailResponse.Entry entry : response.body().getPokemon()) {
                    cargarDetalle(entry.getPokemon().getName());
                }
            }

            @Override
            public void onFailure(Call<TipoDetailResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Segunda consulta: por cada Pokémon se usa la URL/nombre para traer su detalle
    private void cargarDetalle(String nombre) {
        api.getPokemon(nombre).enqueue(new Callback<PokemonDetailResponse>() {
            @Override
            public void onResponse(Call<PokemonDetailResponse> call,
                                   Response<PokemonDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemons.add(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PokemonDetailResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double magnitud = Math.sqrt(x * x + y * y + z * z);
        double aceleracion = Math.abs(magnitud - SensorManager.GRAVITY_EARTH);

        if (aceleracion > 4) { // umbral a tu criterio
            long ahora = System.currentTimeMillis();
            if (ahora - ultimaAgitacion > 3000) { // evita disparos repetidos
                ultimaAgitacion = ahora;
                Navigation.findNavController(requireView()).navigateUp(); // vuelve al Fragment A
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}