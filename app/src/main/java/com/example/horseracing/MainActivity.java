package com.example.horseracing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btn_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         btn_dashboard = (Button) findViewById(R.id.btn_dashboard);
         btn_dashboard.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent it = new Intent(MainActivity.this, DashboardActivity.class);
                 it.putExtra("firstPlaceImg", R.drawable.pikachu);
                 it.putExtra("secondPlaceImg", R.drawable.steve);
                 it.putExtra("thirdPlaceImg", R.drawable.mario);
                 it.putExtra("result", 1); // WIN
//                 it.putExtra("result", 0); // LOSE
                 it.putExtra("amount", 100d);

                 startActivity(it);
             }
         });
    }
}