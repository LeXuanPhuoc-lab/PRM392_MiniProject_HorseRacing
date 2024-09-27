package com.example.horseracing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RuleActivity extends AppCompatActivity {
    Button btnBack;
    Button btnContinue;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rule);
        btnBack =(Button) findViewById(R.id.btnBack);
        btnContinue =(Button) findViewById(R.id.btnContinue);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent
            }
        });

    }
}
