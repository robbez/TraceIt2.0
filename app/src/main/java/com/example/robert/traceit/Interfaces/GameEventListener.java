package com.example.robert.traceit.Interfaces;

/**
 * Created by Robert on 3/7/2017.
 */

public interface GameEventListener {
    void OnGameReady();
    void OnShapeCompleted(int addedTime, int addedScore);
    void OnGameOver();
}
