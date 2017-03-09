package com.example.robert.traceit;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.robert.traceit.GameStats.GameStatsView;
import com.example.robert.traceit.Interfaces.GameEventListener;
import com.example.robert.traceit.drawing.DrawArea;

public class GameActivity extends AppCompatActivity implements GameEventListener, PauseFragment.OnPauseFragmentInteractionListener{

    private DrawArea drawArea;
    private GameStatsView gameStatsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.drawArea = (DrawArea) findViewById(R.id.draw_area);
        this.gameStatsView = (GameStatsView) findViewById(R.id.game_stats);

        this.drawArea.setGameReadyListener(this);
        this.gameStatsView.setGameReadyListener(this);
    }

    @Override
    public void onBackPressed() {
        goBackToMainMenu();
    }

    private void goBackToMainMenu() {
        Intent backToMainActivityIntent = new Intent(this, MainMenuActivity.class);
        backToMainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backToMainActivityIntent);
    }

    @Override
    public void OnGameReady() {
        drawArea.startGame();
        gameStatsView.startCountDown();
    }

    @Override
    public void OnShapeCompleted(int addedTime, int addedScore) {
        gameStatsView.addTIme(addedTime);
        gameStatsView.addScore(addedScore);
    }

    @Override
    public void OnGameOver() {
        drawArea.endGame();
        Fragment pauseFragment = PauseFragment.newInstance(PauseFragment.PauseFragmentType.Geme_Over);
        getFragmentManager().beginTransaction().add(R.id.activity_game, pauseFragment).commit();
    }

    @Override
    public void OnRestartPressed() {

    }

    @Override
    public void OnMenuPressed() {

    }

    @Override
    public void OnResumedPressed() {

    }
}
