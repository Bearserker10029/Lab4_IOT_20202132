package com.example.lab4_iot_20202132.fragments;

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

import com.example.lab4_iot_20202132.databinding.FragmentPokemonsBinding;
import com.example.lab4_iot_20202132.dto.PokemonResponse;
import com.example.lab4_iot_20202132.network.PokeAPI;
import com.example.lab4_iot_20202132.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonFragment extends Fragment implements SensorEventListener {

    private FragmentPokemonsBinding binding;
    private final PokeAPI api = RetrofitClient.get().create(PokeAPI.class);

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

    }

    private void buscarReceta(String id) {
        api.lookup(id).enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call,
                                   Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null
                        && response.body().getPokemons() != null
                        && !response.body().getPokemons().isEmpty()) {
                    mostrarReceta(response.body().getPokemons().get(0));
                } else {
                    Toast.makeText(getContext(), "No se encontró la receta",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarReceta(PokemonResponse.PokemonDetail meal) {
        binding.textPokemonName.setText(meal.getStrPokemon());
        binding.textPokemonTipo.setText("Categoría: " + meal.getStrPokemon());

        StringBuilder ingredientes = new StringBuilder();
        String[] nombres = meal.getabilities();
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i] != null && !nombres[i].trim().isEmpty()) {
                ingredientes.append("• ").append(nombres[i].trim());
                ingredientes.append("\n");
            }
        }
        binding.textAbilities.setText(ingredientes.toString());

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double magnitud = Math.sqrt(x * x + y * y + z * z);
        double aceleracion = Math.abs(magnitud - SensorManager.GRAVITY_EARTH);

        if (aceleracion > 4) { // umbral de 4 m/s²
            long ahora = System.currentTimeMillis();
            if (ahora - ultimaAgitacion > 3000) { // evita disparos repetidos
                ultimaAgitacion = ahora;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

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
