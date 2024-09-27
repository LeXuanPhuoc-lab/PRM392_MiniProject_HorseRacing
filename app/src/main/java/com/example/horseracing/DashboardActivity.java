package com.example.horseracing;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    TextView tv_dashboard_result;
    TextView tv_dashboard_message;
    TextView tv_dashboard_amount;
    ImageView iv_dashboard_title;
    ImageView iv_firstPlace;
    ImageView iv_secondPlace;
    ImageView iv_thirdPlace;
    ImageView firstPlaceTicket, secondPlaceTicket, thirdPlaceTicket;
    TextView firstPlaceExpression, secondPlaceExpression, thirdPlaceExpression, totalBet, totalWin, firstPlaceBet, secondPlaceBet, thirdPlaceBet;
    Button backToRaceButton;

    private HashMap<String, Integer> colorToTicketImageId = new HashMap<>();


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

        colorToTicketImageId.put("Red", R.drawable.red_ticket);
        colorToTicketImageId.put("Yellow", R.drawable.yellow_ticket);
        colorToTicketImageId.put("Blue", R.drawable.blue_ticket);
        colorToTicketImageId.put("Pink", R.drawable.pink_ticket);
        colorToTicketImageId.put("Green", R.drawable.green_ticket);

        // References
        tv_dashboard_result = (TextView) findViewById(R.id.tv_dashboard_result);
        tv_dashboard_message = (TextView) findViewById(R.id.tv_dashboard_message);
        tv_dashboard_amount = (TextView) findViewById(R.id.tv_dashboard_amount);
        iv_dashboard_title = (ImageView) findViewById(R.id.iv_dashboard_title);
        iv_firstPlace = (ImageView) findViewById(R.id.iv_firstPlace);
        iv_secondPlace = (ImageView) findViewById(R.id.iv_secondPlace);
        iv_thirdPlace = (ImageView) findViewById(R.id.iv_thirdPlace);
        backToRaceButton = findViewById(R.id.backToRaceButton);

        firstPlaceTicket = findViewById(R.id.firstPlaceTicket);
        secondPlaceTicket = findViewById(R.id.secondPlaceTicket);
        thirdPlaceTicket = findViewById(R.id.thirdPlaceTicket);
        firstPlaceExpression = findViewById(R.id.firstPlaceExpression);
        secondPlaceExpression = findViewById(R.id.secondPlaceExpression);
        thirdPlaceExpression = findViewById(R.id.thirdPlaceExpression);
        firstPlaceBet = findViewById(R.id.firstPlaceBet);
        secondPlaceBet = findViewById(R.id.secondPlaceBet);
        thirdPlaceBet = findViewById(R.id.thirdPlaceBet);
        totalBet = findViewById(R.id.totalBet);
        totalWin = findViewById(R.id.totalWin);

        // Initiates dashboard properties
        boolean isWin = false;
        String message = "";
        int amount = 0;

        // Get intent data
        if (getIntent() != null) {
            int firstPlaceImg = getIntent().getIntExtra("firstPlaceImg", 0);
            int secondPlaceImg = getIntent().getIntExtra("secondPlaceImg", 0);
            int thirdPlaceImg = getIntent().getIntExtra("thirdPlaceImg", 0);
            String firstPlace = getIntent().getStringExtra("firstPlace");
            String secondPlace = getIntent().getStringExtra("secondPlace");
            String thirdPlace = getIntent().getStringExtra("thirdPlace");
            int firstPlaceBetValue = getIntent().getIntExtra("firstPlaceBet", 0);
            int secondPlaceBetValue = getIntent().getIntExtra("secondPlaceBet", 0);
            int thirdPlaceBetValue = getIntent().getIntExtra("thirdPlaceBet", 0);
            int totalBetAmount = getIntent().getIntExtra("totalBetAmount", 0);
            int result = getIntent().getIntExtra("result", 0);
            int fluctuationAmount = getIntent().getIntExtra("amount", 0);

            firstPlaceTicket.setImageResource(colorToTicketImageId.get(firstPlace));
            secondPlaceTicket.setImageResource(colorToTicketImageId.get(secondPlace));
            thirdPlaceTicket.setImageResource(colorToTicketImageId.get(thirdPlace));

            firstPlaceBet.setText(firstPlaceBetValue + "");
            secondPlaceBet.setText(secondPlaceBetValue + "");
            thirdPlaceBet.setText(thirdPlaceBetValue + "");

            firstPlaceExpression.setText("x3 = " + firstPlaceBetValue * 3);
            secondPlaceExpression.setText("x2 = " + secondPlaceBetValue * 2);
            thirdPlaceExpression.setText("x1 = " + thirdPlaceBetValue * 1);

            totalBet.setText("= -" + totalBetAmount);


            // Check whether user win or not
            isWin = result != 0;
            // Generate message rely on result status
            message = isWin ? "Congratulation!" : "Never give up!";
            // Assign amount
            amount = fluctuationAmount;

            totalWin.setText("= " + (isWin ? "" : "-") + fluctuationAmount);

            // Assign dashboard element
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
            iv_firstPlace.setImageResource(firstPlaceImg);
            iv_secondPlace.setImageResource(secondPlaceImg);
            iv_thirdPlace.setImageResource(thirdPlaceImg);
        }

//        // Popup customization
//        DisplayMetrics dm = new DisplayMetrics();
//        // Get current metric display
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        // Access layout pixels
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;

        // Set layout
        getWindow().setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // Add transition and coordinates
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        // Set attributes
        getWindow().setAttributes(params);

        backToRaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}