package com.project.timer.core;

import com.project.timer.TimerApp;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

class RepeatableTask extends TimerTask implements Timer {
    private AtomicLong atomicLong;
    private AtomicLong notifier;
    boolean cancelled;

    public RepeatableTask(int repeats) {
        this.atomicLong = new AtomicLong(repeats);
        this.notifier = new AtomicLong(3);
    }

    public RepeatableTask() {
        this(1);
    }

    public void startTimer() {
    }

    @Override
    public void run() {
        if(isRepetitionOver()) {
            stopTimer();
            return;
        }

        executeTask();
    }

    private void executeTask() {
        executeTask(new Date());
    }

    private void executeTask(Date date) {
        clrscr();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        TimerApp.timerLabel.setText(getText(atomicLong.get()));
//        System.out.println(dateFormat.format(date));
    }

    private void clrscr() {
        char c = '\n';
        int length = 25;
        char[] chars = new char[length];
        Arrays.fill(chars, c);
        //System.out.print(String.valueOf(chars));
    }

    public void stopTimer() {
        if(notifier.getAndDecrement() <= 0) {
            this.cancel();
            cancelled = true;
            return;
        }

//        System.out.println("Your pomodoro cycle has been completed successfully now ");
        try {
            makeSound();
        } catch (Exception e) {
            // do nothing...
        }
    }

    private boolean isRepetitionOver() {
        return (atomicLong.getAndDecrement() <= 0);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    private void makeSound() throws LineUnavailableException {

        byte[] buf = new byte[ 1 ];;
        AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
        SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
        sdl.open();
        sdl.start();
        for( int i = 0; i < 1000 * (float )44100 / 1000; i++ ) {
            double angle = i / ( (float )44100 / 440 ) * 2.0 * Math.PI;
            buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
            sdl.write( buf, 0, 1 );
        }
        sdl.drain();
        sdl.stop();
    }
}
