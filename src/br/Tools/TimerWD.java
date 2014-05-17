package br.Tools;

import com.badlogic.gdx.utils.TimeUtils;

//WD = WannabeDevs 
public class TimerWD {

    private final long NANO = 1000000000; // Number of nanoseconds in a second

    private long start; // Tracks when the timer was started
    private long end;  // Tracks when the timer ends

    private long seconds; // Number of seconds to wait

    private boolean neverUsed;

    public TimerWD(int seconds) {

        this.seconds = seconds * NANO;
        start = end = -1; // I use negative one to see if the timer is active
        neverUsed = true;
    }

    public TimerWD(float seconds) {

        this.seconds = (long) (seconds * NANO);
        start = end = -1; // I use negative one to see if the timer is active
        neverUsed = true;
    }

    // Activate the timer
    public void start() {

        start = TimeUtils.nanoTime();
        end = start + seconds;
        neverUsed = false;
    }

    // Check if the timer is done
    // If so, deactivate it and return true
    public boolean isDone() {

        if ((TimeUtils.nanoTime()) >= end) {
            start = end = -1;
            return true;
        }

        return false;
    }

    public boolean neverUsed() {
        return neverUsed;
    }

    public void reset() {
        start = end = -1; // I use negative one to see if the timer is active
        neverUsed = true;

    }

    public void changeTiming(int seconds) {
        this.seconds = seconds * NANO;
    }

    public String toString() {
        System.out.println("Start :" + start + "[] End " + end);
        return "Start :" + start + "[] End " + end;
    }

    public float getTime() {
 
        System.out.println("getTime " + (seconds / NANO));
        return (seconds / NANO);
    }

}
