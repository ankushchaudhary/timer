package com.project.timer;


import com.project.timer.core.PomodroManager;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.project.timer.constant.Duration.*;

public class TimerApp {
    static Scanner scanner = new Scanner(System.in);
    static AtomicLong shortSessionCount = new AtomicLong();
    static AtomicLong longSessionCount = new AtomicLong();
    public static JLabel timerLabel;

    public static void main(String[] args) {
        createGUI();
    }

    static void createGUI() {
        PomodroManager pomodroManager = new PomodroManager();
        JFrame frame = new JFrame(String .format("Start Pomodoro %s", new Date()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        JButton session5Min = new JButton("5 Min");
        session5Min.setBounds(100, 100, 100, 100);
        JButton session25Min = new JButton("25 Min");
        session25Min.setBounds(300, 100, 100, 100);
        frame.setLayout(null);
        Color defaultColor = session5Min.getBackground();
        frame.getContentPane().add(BorderLayout.EAST,session5Min);
        frame.getContentPane().add(BorderLayout.WEST, session25Min);
        String totalShortFormatter = "total short session - %s";
        String totalLongFormatter = "total long session - %s";
        JLabel shortLabel = new JLabel(String.format(totalShortFormatter, 0));
        JLabel longLabel = new JLabel(String.format(totalLongFormatter, 0));
        timerLabel = new JLabel("         ");
        timerLabel.setBounds(250, 20, 100, 100);
        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.red);
        errorLabel.setBounds(30, 30, 280, 30);
        frame.getContentPane().add(errorLabel);
        Timer timer = new Timer();
        session5Min.addActionListener((actionEvent) -> {
            if(needLongSeesion()) {
                errorLabel.setText("I want you to take a long break");
                return;
            }
            session5Min.setBackground(Color.GREEN);
            pomodroManager.start5MinCycle();
            timer.schedule(new TimerTask() {
                               @Override
                               public void run() {
                                   session5Min.setBackground(defaultColor);
                                   //timerLabel.setText("Completed...");
                                   shortLabel.setText(String.format(totalShortFormatter, shortSessionCount.incrementAndGet()));
                               }
                           }, TimeUnit.SECONDS.toMillis(SHORT_SESSION_DURATION));
        });

        shortLabel.setBounds(200, 200, 200, 200);
        longLabel.setBounds(200, 300, 200, 200);
        frame.getContentPane().add(shortLabel);
        frame.getContentPane().add(longLabel);
        frame.getContentPane().add(timerLabel);

        session25Min.addActionListener((actionEvent) -> {
            errorLabel.setText("");
            session25Min.setBackground(Color.GREEN);
            pomodroManager.start25MinCycle();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    session25Min.setBackground(defaultColor);
                    longLabel.setText(String.format(totalLongFormatter, longSessionCount.incrementAndGet()));
                }
            }, TimeUnit.SECONDS.toMillis(LONG_SESSION_DURATION));
        });
        frame.setVisible(true);
    }

    public static boolean needLongSeesion() {
        return ((shortSessionCount.get() / 3) > longSessionCount.get());
    }
}
