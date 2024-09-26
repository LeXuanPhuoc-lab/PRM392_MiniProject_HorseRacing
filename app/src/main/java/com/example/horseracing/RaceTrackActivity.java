package com.example.horseracing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import com.example.horseracing.Components.CharacterSeekBar;

import java.util.ArrayList;
import java.util.List;

public class RaceTrackActivity extends AppCompatActivity {
    private MediaPlayer idleSound;
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
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private List<String> winners = new ArrayList<>();
    private List<String> playerBets  = new ArrayList<>();
    private double currentBalance = 100;
    private double initBalance = currentBalance;
    private double netChange;
    private int totalBet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_race_track);

        idleSound = MediaPlayer.create(this, R.raw.idle_sound);
        idleSound.setLooping(true);
        idleSound.start();

        // character
        red = findViewById(R.id.redSeekBar);
        blue = findViewById(R.id.blueSeekBar);
        green = findViewById(R.id.greenSeekBar);
        yellow = findViewById(R.id.yellowSeekBar);
        pink = findViewById(R.id.pinkSeekBar);
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
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning && validateBets()) {  // Prevent starting again while already running
                    resetRace();
                    startRace();
                }
            }
        });
    }

    // Validate the bets before starting the race
    private boolean validateBets() {
        int totalBets = 0;
        int numberOfBets = 0;

        // Parse each bet and add them to total if they're not empty
        try {
            int redAmount = getBetAmount(redBet);
            int blueAmount = getBetAmount(blueBet);
            int greenAmount = getBetAmount(greenBet);
            int yellowAmount = getBetAmount(yellowBet);
            int pinkAmount = getBetAmount(pinkBet);

            totalBets = redAmount + blueAmount + greenAmount + yellowAmount + pinkAmount;
            numberOfBets = countBets(redAmount, blueAmount, greenAmount, yellowAmount, pinkAmount);

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
        if (getBetAmount(redBet) > 0) playerBets.add("Red");
        if (getBetAmount(blueBet) > 0) playerBets.add("Blue");
        if (getBetAmount(greenBet) > 0) playerBets.add("Green");
        if (getBetAmount(yellowBet) > 0) playerBets.add("Yellow");
        if (getBetAmount(pinkBet) > 0) playerBets.add("Pink");

        balance.setText(currentBalance - getTotalBetAmount() + "");
        currentBalance =  currentBalance - getTotalBetAmount();
        return true;
    }

    // Helper method to get bet amounts (if empty, return 0)
    private int getBetAmount(EditText betInput) {
        String bet = betInput.getText().toString();
        return bet.isEmpty() ? 0 : Integer.parseInt(bet);
    }

    // Helper method to count how many bets have been placed
    private int countBets(int... bets) {
        totalBet = 0;
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
    private void startRace() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning && winners.size() < 3) {
                    // Randomly increase progress for each SeekBar
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            moveSeekBar(red, "Red");
                            moveSeekBar(blue, "Blue");
                            moveSeekBar(green, "Green");
                            moveSeekBar(yellow, "Yellow");
                            moveSeekBar(pink, "Pink");
                        }
                    });

                    try {
                        // Sleep for 100 milliseconds to simulate gradual progress
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // When 3 winners are found, stop the race and display results
                isRunning = false;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        stopRace();
                    }
                });
            }
        }).start();
    }

    // Stop the race and show results
    private void stopRace() {
        if (winners.size() == 3) {
            updateBalance();
            announceWinners(); // toast 3 winners
            for (int i = 0; i < winners.size(); i++) {
                System.out.println((i + 1) + ". " + winners.get(i));
            }
            // You can add code here to show the results to the user via UI
        }
    }

    private int getHorseImageId(String horseName) {
        switch (horseName) {
            case "Red":
                return R.drawable.mario;
            case "Blue":
                return R.drawable.nyancat;
            case "Green":
                return R.drawable.turbo;
            case "Yellow":
                return R.drawable.pikachu;
            case "Pink":
                return R.drawable.steve;
            default:
                return R.drawable.ic_horse; // Fallback icon
        }
    }

    // Announce 1st, 2nd, and 3rd place winners
    private void announceWinners() {
        String firstPlace = winners.get(0);
        String secondPlace = winners.get(1);
        String thirdPlace = winners.get(2);

        Toast.makeText(this, "1st Place: " + firstPlace, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "2nd Place: " + secondPlace, Toast.LENGTH_LONG).show();
        Toast.makeText(this, "3rd Place: " + thirdPlace, Toast.LENGTH_LONG).show();

        // Create an Intent to navigate to DashboardActivity
        Intent it = new Intent(RaceTrackActivity.this, DashboardActivity.class);

        // Pass horse image, total bet, and winnings to the DashboardActivity
        it.putExtra("firstPlaceImg", getHorseImageId(firstPlace)); // Image ID for the 1st place horse
        it.putExtra("secondPlaceImg", getHorseImageId(secondPlace)); // Image ID for the 1st place horse
        it.putExtra("thirdPlaceImg", getHorseImageId(thirdPlace)); // Image ID for the 1st place horse
        it.putExtra("result", checkWinningBets() ? 1 : 0); // 1 for win, 0 for lose
        it.putExtra("amount", Math.abs(netChange)); // Amount won or lost

        startActivity(it);
    }
    // Update balance based on race results
    private void updateBalance() {
        double initialBalance = initBalance;

        // Check if any winning horse is in the player's bets
        boolean hasWinningBet = false;

        for (int i = 0; i < winners.size(); i++) {
            String winner = winners.get(i);
            double multiplier = 0;

            // 1st place: 3x, 2nd place: 2x, 3rd place: 1x
            switch (i) {
                case 0: multiplier = 2; break; // 1st place
                case 1: multiplier = 0.5d; break; // 2nd place
                case 2: multiplier = 0; break; // 3rd place
            }

            // Update balance based on winning position
            if (winner.equals("Red")) {
                currentBalance += getBetAmount(redBet) + getBetAmount(redBet) * multiplier;
                if (playerBets.contains("Red")) hasWinningBet = true;
            } else if (winner.equals("Blue")) {
                currentBalance += getBetAmount(blueBet) + getBetAmount(blueBet) * multiplier;
                if (playerBets.contains("Blue")) hasWinningBet = true;
            } else if (winner.equals("Green")) {
                currentBalance += getBetAmount(greenBet) + getBetAmount(greenBet) * multiplier;
                if (playerBets.contains("Green")) hasWinningBet = true;
            } else if (winner.equals("Yellow")) {
                currentBalance += getBetAmount(yellowBet) + getBetAmount(yellowBet) * multiplier;
                if (playerBets.contains("Yellow")) hasWinningBet = true;
            } else if (winner.equals("Pink")) {
                currentBalance += getBetAmount(pinkBet) + getBetAmount(pinkBet) * multiplier;
                if (playerBets.contains("Pink")) hasWinningBet = true;
            }
        }

        // Deduct the total bet amount from the balance if they lose
        if (!hasWinningBet) {
            currentBalance -= getTotalBetAmount();
        }

        // Update balance UI and show toast
        balance.setText(String.valueOf(currentBalance));
        clearBetInputs(); // Clear bet inputs after race

        // Calculate profit or loss
        netChange = currentBalance - initialBalance;

        // Display Toast message indicating profit or loss
        if (netChange > 0) {
            Toast.makeText(this, "You won $" + netChange + "!", Toast.LENGTH_LONG).show();
        } else if (netChange < 0) {
            Toast.makeText(this, "You lost $" + Math.abs(netChange) + "!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No profit or loss.", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Race finished! Balance updated: $" + currentBalance, Toast.LENGTH_LONG).show();
    }

    // Calculate total amount bet by the player
    private double getTotalBetAmount() {
        return getBetAmount(redBet) + getBetAmount(blueBet) + getBetAmount(greenBet) +
                getBetAmount(yellowBet) + getBetAmount(pinkBet);
    }

    // Check if any of the player's bets match the winners
    private boolean checkWinningBets() {
        for (String bet : playerBets) {
            if (winners.contains(bet) && currentBalance - initBalance > 0) {
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
            int randomProgress = (int) (Math.random() * 2 + 0.5); // Adjusted to ensure minimum progress
            seekBar.incrementProgressBy(randomProgress);

            // Check if the SeekBar has reached the max
            if (seekBar.getProgress() >= seekBar.getMax() && !winners.contains(name)) {
                winners.add(name);
                if (winners.size() == 3) {
                    isRunning = false; // Stop the race
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên MediaPlayer
        if (idleSound != null) {
            idleSound.stop();
            idleSound.release();
            idleSound = null;
        }
    }
}