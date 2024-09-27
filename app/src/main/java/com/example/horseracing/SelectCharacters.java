package com.example.horseracing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectCharacters extends AppCompatActivity {
    private MediaPlayer idleSound;
    private ImageAdapter imageAdapter;
    private String selectedAnimal;

    private final List<String> animals = Arrays.asList("Lion", "Dog", "Turtle", "Dragon", "Horse");

    private final Map<String, List<Integer>> animalImagesMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_characters);

        idleSound = MediaPlayer.create(this, R.raw.idle_sound);
        idleSound.setLooping(true);
        idleSound.start();
        selectedAnimal = animals.get(0);

        // Khởi tạo Spinner và RecyclerView
        Spinner animalSpinner = findViewById(R.id.animalSpinner);
        RecyclerView imageRecyclerView = findViewById(R.id.imageRecyclerView);
        Button goToRaceButton = findViewById(R.id.goToRaceButton);

        goToRaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(SelectCharacters.this, RaceTrackActivity.class);

                it.putExtra("redCharImageId", animalImagesMap.get(selectedAnimal).get(0));
                it.putExtra("blueCharImageId", animalImagesMap.get(selectedAnimal).get(1));
                it.putExtra("greenCharImageId", animalImagesMap.get(selectedAnimal).get(2));
                it.putExtra("pinkCharImageId", animalImagesMap.get(selectedAnimal).get(3));
                it.putExtra("yellowCharImageId", animalImagesMap.get(selectedAnimal).get(4));

                if (idleSound != null) {
                    idleSound.stop();
                    idleSound.release();
                    idleSound = null;
                }

                startActivity(it);
            }
        });

        // Khởi tạo dữ liệu hình ảnh cho từng loại động vật
        animalImagesMap.put("Lion", Arrays.asList(
                R.drawable.red_lion, R.drawable.blue_lion, R.drawable.green_lion, R.drawable.pink_lion, R.drawable.yellow_lion
        ));
        animalImagesMap.put("Dog", Arrays.asList(
                R.drawable.red_dog, R.drawable.blue_dog, R.drawable.green_dog, R.drawable.pink_dog, R.drawable.yellow_dog
        ));
        animalImagesMap.put("Turtle", Arrays.asList(
                R.drawable.red_turtle, R.drawable.blue_turtle, R.drawable.green_turtle, R.drawable.pink_turtle, R.drawable.yellow_turtle
        ));
        animalImagesMap.put("Dragon", Arrays.asList(
                R.drawable.red_dragon, R.drawable.blue_dragon, R.drawable.green_dragon, R.drawable.pink_dragon, R.drawable.yellow_dragon
        ));
        animalImagesMap.put("Horse", Arrays.asList(
                R.drawable.red_horse, R.drawable.blue_horse, R.drawable.green_horse, R.drawable.pink_horse, R.drawable.yellow_horse
        ));

        // Thiết lập Adapter cho Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, animals);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animalSpinner.setAdapter(spinnerAdapter);

        // Thiết lập RecyclerView
        imageRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        imageAdapter = new ImageAdapter(animalImagesMap.get(animals.get(0)));
        imageRecyclerView.setAdapter(imageAdapter);

        // Xử lý sự kiện chọn Spinner
        animalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAnimal = animals.get(position);
                List<Integer> images = animalImagesMap.get(selectedAnimal);
                if (images != null) {
                    imageAdapter.updateImages(images);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nothing to do
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        if (idleSound != null) {
            idleSound.stop();
            idleSound.release();
            idleSound = null;
        }
    }
}