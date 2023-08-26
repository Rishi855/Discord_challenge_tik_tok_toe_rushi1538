package com.tiktoktoe;


import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiktoktoe.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private boolean player1Turn = true;
    private boolean isTwoPlayers = false; // Indicates if it's a 2-player game
    private int roundCount = 0;
    private boolean gameOver = false;
    private int botDifficulty = 2; // 0: Easy, 1: Medium, 2: Hard
    public int player1 = 0,player2=0,bot=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show a dialog to choose the number of players
        showPlayerNumberDialog();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button settingButton = findViewById(R.id.settingsButton);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerNumberDialog();
                resetGame();
                RelativeLayout relativeLayout = findViewById(R.id.mainBg);
                Drawable backgroundDrawable = getResources().getDrawable(R.drawable.player1_bg);
                relativeLayout.setBackground(backgroundDrawable);
                player1=0;
                player2=0;
                bot=0;
                TextView textView = findViewById(R.id.scoreBoard);
                textView.setText("0:0");
            }
        });
        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
                RelativeLayout relativeLayout = findViewById(R.id.mainBg);
                Drawable backgroundDrawable = getResources().getDrawable(R.drawable.player1_bg);
                relativeLayout.setBackground(backgroundDrawable);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (gameOver) {
            return;
        }

        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("X");
//            ((Button) v).setBackgroundColor(getResources().getColor(R.color.X_player));
            ((Button) v).setBackgroundResource(R.drawable.player1_button);
            RelativeLayout relativeLayout = findViewById(R.id.mainBg);
            Drawable backgroundDrawable = getResources().getDrawable(R.drawable.player2_bg);
            relativeLayout.setBackground(backgroundDrawable);
        } else {
            ((Button) v).setText("O");
//            ((Button) v).setBackgroundColor(getResources().getColor(R.color.Y_player));
            ((Button) v).setBackgroundResource(R.drawable.player2_button);
            RelativeLayout relativeLayout = findViewById(R.id.mainBg);
            Drawable backgroundDrawable = getResources().getDrawable(R.drawable.player1_bg);
            relativeLayout.setBackground(backgroundDrawable);
        }

        roundCount++;

        TextView scoreBoard = findViewById(R.id.scoreBoard);
        if (checkForWin()) {
            if (player1Turn) {
                playerWins("Player 1");
                player1++;
                scoreBoard.setText(player1+":"+player2);
            } else {
                playerWins(isTwoPlayers ? "Player 2" : "Player 2 (Bot)");
                player2++;
                bot++;
                if(isTwoPlayers)
                scoreBoard.setText(player1+":"+player2);
                else
                    scoreBoard.setText(player1+":"+bot);
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;

            if (!isTwoPlayers && !player1Turn) {
                botMove();
            }
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].isEmpty()) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].isEmpty()) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].isEmpty()) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].isEmpty()) {
            return true;
        }

        return false;
    }

    private void playerWins(String playerName) {
        Toast.makeText(this, playerName + " wins!", Toast.LENGTH_SHORT).show();
        gameOver = true;

    }

    private void draw() {
        Toast.makeText(this, "It's a draw!", Toast.LENGTH_SHORT).show();
        gameOver = true;
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
//                buttons[i][j].setBackgroundColor(getResources().getColor(R.color.Reset));
                buttons[i][j].setBackgroundResource(R.drawable.rounded_radius);
            }
        }
        roundCount = 0;
        player1Turn = true;
        gameOver = false;
    }

    private void botMove() {
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (!buttons[row][col].getText().toString().isEmpty());

        RelativeLayout relativeLayout = findViewById(R.id.mainBg);
        Drawable backgroundDrawable = getResources().getDrawable(R.drawable.player1_bg);
        relativeLayout.setBackground(backgroundDrawable);
        buttons[row][col].setText("O");
//        buttons[row][col].setBackgroundColor(getResources().getColor(R.color.Y_player));
        buttons[row][col].setBackgroundResource(R.drawable.player2_button);
        roundCount++;

        if (checkForWin()) {
            playerWins("Player 2 (Bot)");
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = true;
        }
    }

    private void showPlayerNumberDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Choose Number of Players");

        // Create a RadioGroup with two options: 1 Player and 2 Players
        final String[] playerOptions = {"1 Player (vs Bot)", "2 Players"};
        final boolean[] checkedItems = {true, false}; // Initially, the first option is checked

        dialogBuilder.setSingleChoiceItems(playerOptions, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isTwoPlayers = (which == 1);
                dialog.dismiss();
            }
        });

        AlertDialog playerNumberDialog = dialogBuilder.create();
        playerNumberDialog.setCancelable(false); // Prevent dismissing the dialog without choosing a player number
        playerNumberDialog.show();
    }
}
