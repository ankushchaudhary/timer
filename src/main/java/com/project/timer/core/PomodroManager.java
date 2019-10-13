package com.project.timer.core;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import static com.project.timer.constant.Duration.*;

public class PomodroManager {
    Timer timer = new Timer();
    RepeatableTask r1;
    RepeatableTask r2;

    public PomodroManager() {
       init();
    }

    private void init() {
        if(r1 == null || r1.isCancelled()) {
            r1 = new RepeatableTask(SHORT_SESSION_DURATION);
        }

        if(r2 == null || r2.isCancelled()) {
            r2 = new RepeatableTask(LONG_SESSION_DURATION);
        }
    }

    public void start5MinCycle() {
        init();
        startCycle(r1);
    }

    public void start25MinCycle() {
        init();
        startCycle(r2);
    }

    private void startCycle(TimerTask task) {
        timer.scheduleAtFixedRate(task, new Date(), 1000);
    }
}
