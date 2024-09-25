package com.example.horseracing;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DashboardActivity extends AppCompatActivity {

    TextView tv_dashboard_result;
    TextView tv_dashboard_message;
    TextView tv_dashboard_amount;
    TextView tv_totalBet;
    TextView tv_totalWin;
    ImageView iv_dashboard_title;
    ImageView iv_dashboard_horse_icon;
    int imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // References
        tv_dashboard_result = (TextView) findViewById(R.id.tv_dashboard_result);
        tv_dashboard_message = (TextView) findViewById(R.id.tv_dashboard_message);
        tv_dashboard_amount = (TextView) findViewById(R.id.tv_dashboard_amount);
        tv_totalBet = (TextView) findViewById(R.id.tv_totalBet);
        tv_totalWin = (TextView) findViewById(R.id.tv_totalWin);
//        tv_totalLose = (TextView) findViewById(R.id.tv_totalLose);
        iv_dashboard_title = (ImageView) findViewById(R.id.iv_dashboard_title);
        iv_dashboard_horse_icon = (ImageView) findViewById(R.id.iv_dashboard_horse_icon);


        // Initiates dashboard properties
        boolean isWin = false;
        String message = "";
        double amount = 0d;

        // Get intent data
        if(getIntent() != null){
            int horseImageId = getIntent().getIntExtra("horseImageId", 0);
            int totalBet = getIntent().getIntExtra("totalBet", 0);
            int totalWin = getIntent().getIntExtra("totalWin", 0);
            int result = getIntent().getIntExtra("result", 0);
            double fluctuationAmount = getIntent().getDoubleExtra("amount", 0d);

            // Check whether user win or not
            isWin = result != 0;
            // Generate message rely on result status
            message = isWin ? "Congratulation!" : "Never give up!";
            // Assign amount
            amount = fluctuationAmount;
            // Assign horse image
            imageId = horseImageId;

            // Assign dashboard element
//            iv_dashboard_title.setImageResource(isWin
//                    ? R.drawable.ic_win : R.drawable.ic_lose);
            iv_dashboard_title.setImageResource(R.drawable.ic_win);
            tv_dashboard_result.setText(isWin ? "YOU WIN" : "YOU LOSE");
            tv_dashboard_result.setTextColor(ContextCompat.getColor(this,
                    isWin ? R.color.green : R.color.yellow));
            tv_dashboard_message.setText(message);
            tv_dashboard_amount.setText(isWin
                    ? "+ $" + amount
                    : "- $" + amount);
            tv_dashboard_amount.setTextColor(ContextCompat.getColor(this,
                    isWin ? R.color.yellow : R.color.red));
            tv_totalBet.setText("Total bet: " + totalBet);
            tv_totalWin.setText("Win: " + totalWin);
            iv_dashboard_horse_icon.setImageResource(imageId);
        }

        // Popup customization
        DisplayMetrics dm = new DisplayMetrics();
        // Get current metric display
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // Access layout pixels
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // Set layout
        getWindow().setLayout((int) (width* .8), (int) (height* .6));

        // Add transition and coordinates
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        // Set attributes
        getWindow().setAttributes(params);
    }
}