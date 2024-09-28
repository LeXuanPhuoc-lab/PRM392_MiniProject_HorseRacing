package com.example.horseracing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.horseracing.Components.CharacterSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RaceTrackActivity extends AppCompatActivity {
    private MediaPlayer idleSound;
    private MediaPlayer startRaceSound;
    private MediaPlayer racingSound;
    private CharacterSeekBar red;
    private CharacterSeekBar blue;
    private CharacterSeekBar green;
    private CharacterSeekBar yellow;
    private CharacterSeekBar pink;
    private EditText redBet;
    private EditText blueBet;
    private EditText greenBet;
    private EditText yellowBet;
    private EditText pinkBet;
    private TextView balance;
    private Button btnStart;
    private ImageView imgRule;
    private final Handler handler = new Handler();
    private boolean isRunning = false;
    private final List<String> winners = new ArrayList<>();
    private final List<String> playerBets = new ArrayList<>();
    private int currentBalance = 20;
    private int netChange;
    private HashMap<String, Integer> colorToCharImageId = new HashMap<>();
    private final List<String> runningChars = new ArrayList<>();

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_race_track);

        runningChars.add("Red");
        runningChars.add("Yellow");
        runningChars.add("Green");
        runningChars.add("Pink");
        runningChars.add("Blue");

        idleSound = MediaPlayer.create(this, R.raw.idle_sound);
        startRaceSound = MediaPlayer.create(this, R.raw.start_race_sound);
        racingSound = MediaPlayer.create(this, R.raw.racing_sound);
        idleSound.setLooping(true);
        racingSound.setLooping(true);

        idleSound.start();

        // character
        red = findViewById(R.id.redSeekBar);
        blue = findViewById(R.id.blueSeekBar);
        green = findViewById(R.id.greenSeekBar);
        yellow = findViewById(R.id.yellowSeekBar);
        pink = findViewById(R.id.pinkSeekBar);

        red.setEnabled(false);
        blue.setEnabled(false);
        green.setEnabled(false);
        yellow.setEnabled(false);
        pink.setEnabled(false);

        // bet
        redBet = findViewById(R.id.redBet);
        blueBet = findViewById(R.id.blueBet);
        greenBet = findViewById(R.id.greenBet);
        yellowBet = findViewById(R.id.yellowBet);
        pinkBet = findViewById(R.id.pinkBet);

        balance = findViewById(R.id.balance);
        btnStart = findViewById(R.id.startButton);

        // Set initial balance
        balance.setText(currentBalance + "");

        // Handle Start button click
        btnStart.setOnClickListener(v -> {
            if (!isRunning && validateBets()) {  // Prevent starting again while already running
                resetRace();
                startRace();
            }
        });
        if (getIntent() != null) {
            int redCharImageId = getIntent().getIntExtra("redCharImageId", 0);
            int blueCharImageId = getIntent().getIntExtra("blueCharImageId", 0);
            int greenCharImageId = getIntent().getIntExtra("greenCharImageId", 0);
            int yellowCharImageId = getIntent().getIntExtra("yellowCharImageId", 0);
            int pinkCharImageId = getIntent().getIntExtra("pinkCharImageId", 0);

            red.setCharacterDrawable(getDrawable(redCharImageId));
            blue.setCharacterDrawable(getDrawable(blueCharImageId));
            yellow.setCharacterDrawable(getDrawable(yellowCharImageId));
            pink.setCharacterDrawable(getDrawable(pinkCharImageId));
            green.setCharacterDrawable(getDrawable(greenCharImageId));

            colorToCharImageId.put("Red", redCharImageId);
            colorToCharImageId.put("Blue", blueCharImageId);
            colorToCharImageId.put("Green", greenCharImageId);
            colorToCharImageId.put("Yellow", yellowCharImageId);
            colorToCharImageId.put("Pink", pinkCharImageId);
        }

//        colorToCharImageId.put("Red", R.drawable.mario);
//        colorToCharImageId.put("Yellow", R.drawable.pikachu);
//        colorToCharImageId.put("Blue", R.drawable.turbo);
//        colorToCharImageId.put("Pink", R.drawable.nyancat);
//        colorToCharImageId.put("Green", R.drawable.steve);
    }


    // Validate the bets before starting the race
    @SuppressLint("SetTextI18n")
    private boolean validateBets() {
        // Parse each bet and add them to total if they're not empty
        try {
            int redAmount = getBetAmount("Red");
            int blueAmount = getBetAmount("Blue");
            int greenAmount = getBetAmount("Green");
            int yellowAmount = getBetAmount("Yellow");
            int pinkAmount = getBetAmount("Pink");

            int totalBets = getTotalBetAmount();
            int numberOfBets = countBets(redAmount, blueAmount, greenAmount, yellowAmount, pinkAmount);

            // Check if more than 3 bets are placed
            if (numberOfBets > 3) {
                Toast.makeText(this, "You can only bet on up to 3 races!", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (totalBets == 0) {
                Toast.makeText(this, "You need choose at least once bet!", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Check if total bets are more than the balance
            if (totalBets > currentBalance) {
                Toast.makeText(this, "You cannot bet more than your balance!", Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid bet amount!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Store player bets
        playerBets.clear(); // Clear previous bets
        if (getBetAmount("Red") > 0) playerBets.add("Red");
        if (getBetAmount("Blue") > 0) playerBets.add("Blue");
        if (getBetAmount("Green") > 0) playerBets.add("Green");
        if (getBetAmount("Yellow") > 0) playerBets.add("Yellow");
        if (getBetAmount("Pink") > 0) playerBets.add("Pink");

        balance.setText(currentBalance - getTotalBetAmount() + "");
        currentBalance = currentBalance - getTotalBetAmount();
        return true;
    }

    // Helper method to get bet amounts (if empty, return 0)
    private int getBetAmount(String color) {
        String bet = "";

        switch (color) {
            case "Red":
                bet = redBet.getText().toString();
                break;
            case "Yellow":
                bet = yellowBet.getText().toString();
                break;
            case "Blue":
                bet = blueBet.getText().toString();
                break;
            case "Pink":
                bet = pinkBet.getText().toString();
                break;
            case "Green":
                bet = greenBet.getText().toString();
                break;
        }

        return bet.isEmpty() ? 0 : Integer.parseInt(bet);
    }

    // Helper method to count how many bets have been placed
    private int countBets(int... bets) {
        int totalBet = 0;
        for (int bet : bets) {
            if (bet > 0) totalBet++;
        }
        return totalBet;
    }

    // Reset progress for all SeekBars and winners
    private void resetRace() {
        isRunning = true;
        winners.clear();
        red.setProgress(0);
        blue.setProgress(0);
        green.setProgress(0);
        yellow.setProgress(0);
        pink.setProgress(0);
    }

    // Start race and run all SeekBars concurrently
    @SuppressLint("SetTextI18n")
    private void startRace() {
        btnStart.setText("Racing...");
        btnStart.setEnabled(false);
        redBet.setEnabled(false);
        yellowBet.setEnabled(false);
        blueBet.setEnabled(false);
        pinkBet.setEnabled(false);
        greenBet.setEnabled(false);
        idleSound.stop();
        idleSound.reset();          // Reset MediaPlayer
        idleSound = MediaPlayer.create(this, R.raw.idle_sound);
        idleSound.setLooping(true);

        new Thread(() -> {
            startRaceSound.start();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            racingSound.start();

            while (isRunning && winners.size() < 3) {
                // Randomly increase progress for each SeekBar
                runOnUiThread(() -> {
                    moveSeekBar(red, "Red");
                    moveSeekBar(blue, "Blue");
                    moveSeekBar(green, "Green");
                    moveSeekBar(yellow, "Yellow");
                    moveSeekBar(pink, "Pink");
                });

                try {
                    // Sleep for 500 milliseconds to simulate gradual progress
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // When 3 winners are found, stop the race and display results
            isRunning = false;
            handler.post(this::stopRace);
        }).start();
    }

    // Stop the race and show results
    @SuppressLint("SetTextI18n")
    private void stopRace() {
        if (winners.size() != 3) return;

        btnStart.setText("Start the race");
        btnStart.setEnabled(true);
        redBet.setEnabled(true);
        yellowBet.setEnabled(true);
        blueBet.setEnabled(true);
        pinkBet.setEnabled(true);
        greenBet.setEnabled(true);
        racingSound.stop();
        racingSound.reset();          // Reset MediaPlayer
        racingSound = MediaPlayer.create(this, R.raw.racing_sound);
        racingSound.setLooping(true);
        idleSound.start();
        updateBalance();
        announceWinners(); // toast 3 winners
        clearBetInputs(); // Clear bet inputs after race
        for (int i = 0; i < winners.size(); i++) {
            System.out.println((i + 1) + ". " + winners.get(i));
        }
        // You can add code here to show the results to the user via UI

    }

    // Announce 1st, 2nd, and 3rd place winners
    private void announceWinners() {
        String firstPlace = winners.get(0);
        String secondPlace = winners.get(1);
        String thirdPlace = winners.get(2);

        // Create an Intent to navigate to DashboardActivity
        Intent it = new Intent(RaceTrackActivity.this, DashboardActivity.class);

        // Pass horse image, total bet, and winnings to the DashboardActivity
        it.putExtra("firstPlaceImg", colorToCharImageId.get(firstPlace)); // Image ID for the 1st place horse
        it.putExtra("secondPlaceImg", colorToCharImageId.get(secondPlace)); // Image ID for the 1st place horse
        it.putExtra("thirdPlaceImg", colorToCharImageId.get(thirdPlace)); // Image ID for the 1st place horse
        it.putExtra("firstPlace", firstPlace);
        it.putExtra("secondPlace", secondPlace);
        it.putExtra("thirdPlace", thirdPlace);
        it.putExtra("fourPlace", runningChars.get(0));
        it.putExtra("fifthPlace", runningChars.get(1));
        it.putExtra("firstPlaceBet", getBetAmount(firstPlace));
        it.putExtra("secondPlaceBet", getBetAmount(secondPlace));
        it.putExtra("thirdPlaceBet", getBetAmount(thirdPlace));
        it.putExtra("fourPlaceBet", getBetAmount(runningChars.get(0)));
        it.putExtra("fifthPlaceBet", getBetAmount(runningChars.get(1)));
        it.putExtra("totalBetAmount", getTotalBetAmount());
        it.putExtra("result", checkWinningBets() ? 1 : 0); // 1 for win, 0 for lose
        it.putExtra("amount", Math.abs(netChange)); // Amount won or lost

        startActivity(it);
    }

    // Update balance based on race results
    private void updateBalance() {
        netChange = -getTotalBetAmount();

        for (int i = 0; i < winners.size(); i++) {
            String winner = winners.get(i);
            int multiplier = 0;

            // 1st place: 3x, 2nd place: 2x, 3rd place: 1x
            switch (i) {
                case 0:
                    multiplier = 3;
                    break; // 1st place
                case 1:
                    multiplier = 2;
                    break; // 2nd place
                case 2:
                    multiplier = 1;
                    break; // 3rd place
            }

            // Update balance based on winning position
            int winAmount = getBetAmount(winner) * multiplier;
            currentBalance += winAmount;
            netChange += winAmount;

        }

        // Update balance UI and show toast
        balance.setText(String.valueOf(currentBalance));
    }

    // Calculate total amount bet by the player
    private int getTotalBetAmount() {
        return getBetAmount("Red") + getBetAmount("Yellow") + getBetAmount("Blue") +
                getBetAmount("Pink") + getBetAmount("Green");
    }

    // Check if any of the player's bets match the winners
    private boolean checkWinningBets() {
        for (String bet : playerBets) {
            if (winners.contains(bet) && netChange > 0) {
                return true; // At least one match found
            }
        }
        return false; // No matches
    }

    // Clear bet inputs
    private void clearBetInputs() {
        redBet.setText("0");
        blueBet.setText("0");
        greenBet.setText("0");
        yellowBet.setText("0");
        pinkBet.setText("0");
    }


    // Move each SeekBar and check if it reaches the end
    private void moveSeekBar(CharacterSeekBar seekBar, String name) {
        if (seekBar.getProgress() < seekBar.getMax() && winners.size() < 3) {
            // Randomize the increment of progress (can be adjusted)
            int randomProgress = (int) (Math.random() * 8 + 1); // Adjusted to ensure minimum progress
            seekBar.incrementProgressBy(randomProgress);

            // Check if the SeekBar has reached the max
            if (seekBar.getProgress() >= seekBar.getMax() && !winners.contains(name)) {
                winners.add(name);
                runningChars.remove(name);
                if (winners.size() == 3) {
                    isRunning = false; // Stop the race
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (idleSound != null) {
            idleSound.stop();
            idleSound.release();
            idleSound = null;
        }
        if (startRaceSound != null) {
            startRaceSound.stop();
            startRaceSound.release();
            startRaceSound = null;
        }
        if (racingSound != null) {
            racingSound.stop();
            racingSound.release();
            racingSound = null;
        }
    }
}