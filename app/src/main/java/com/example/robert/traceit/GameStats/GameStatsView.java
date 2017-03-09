package com.example.robert.traceit.GameStats;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.robert.traceit.Interfaces.GameEventListener;
import com.example.robert.traceit.R;

/**
 * Created by Robert on 3/7/2017.
 */

public class GameStatsView extends RelativeLayout {

    private static final int INITIAL_TIME = 10000;

    private Context context;
    public int width;
    public int height;

    private TextView timerText, scoreText;
    private ImageButton pauseBtn;

    private CountDownTimer timer;
    private int secondsLeft;

    private int score;

    private GameEventListener gameEventListener;

    private final Object lock = new Object();

    public GameStatsView(Context c) {
        super(c);
        init(c);
    }

    public GameStatsView(Context c, AttributeSet attrs) {
        super(c, attrs);
        init(c);
    }

    public GameStatsView(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        init(c);
    }

    private void init(Context c) {
        this.context = c;
        inflate(c, R.layout.view_game_stats, this);

        this.secondsLeft = INITIAL_TIME / 1000;
        this.score = 0;

        this.timerText = (TextView) findViewById(R.id.timer_text);
        this.timerText.setText(getTimerText(this.secondsLeft));
        this.scoreText = (TextView) findViewById(R.id.score_text);
        this.scoreText.setText(getScoreText(this.score));
        this.pauseBtn = (ImageButton) findViewById(R.id.pause_btn);

        this.timer = getNewCountDownTimer(INITIAL_TIME);

    }

    public void addTIme(int seconds) {
        synchronized (lock) {
            secondsLeft += seconds;
            this.timer.cancel();
            this.timer = getNewCountDownTimer(secondsLeft * 1000);
            this.timer.start();
        }
    }

    public void addScore(int scoreIncrement) {
        score += scoreIncrement;
        this.scoreText.setText(getScoreText(score));
    }

    public int getScore() {
        return this.score;
    }

    public void startCountDown() {
        if(this.timer != null) {
            this.timer.start();
        }
    }

    public void stopCountDown() {
        if(this.timer != null) {
            this.timer.cancel();
        }
    }

    public void reset() {
        stopCountDown();
        this.score = 0;
        this.secondsLeft = 0;
        this.scoreText.setText(getScoreText(score));
        this.timerText.setText(getTimerText(this.secondsLeft));
    }

    private String getTimerText(int secondsLeft) {
        return "TIME LEFT: " + secondsLeft + "s";
    }

    private String getScoreText(int score) {
        return "SCORE: " + score;
    }

    private CountDownTimer getNewCountDownTimer(int length) {
        return new CountDownTimer(length + 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                synchronized (lock) {

                    if (secondsLeft > 0) {
                        timerText.setText(getTimerText(--secondsLeft));
                    }
                }
            }

            @Override
            public void onFinish() {
                if( secondsLeft == 0 && gameEventListener != null) {
                    gameEventListener.OnGameOver();
                }
            }
        };
    }

    public void setGameReadyListener(GameEventListener grl) {
        this.gameEventListener = grl;
    }
}
