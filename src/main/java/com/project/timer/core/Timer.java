package com.project.timer.core;

interface Timer {
    default String getText(long seconds) {
        return String.format("%02d : %02d ", seconds/60, seconds % 60);
    }
    public void startTimer();

    public void stopTimer();
}