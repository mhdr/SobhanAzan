package com.nasimeshomal;

import org.quartz.Scheduler;

/**
 * Created by ma.ramezani on 3/6/2016.
 */
public class Statics
{
    public static Scheduler getSchedulerPlay() {
        return schedulerPlay;
    }

    public static void setSchedulerPlay(Scheduler schedulerPlay) {
        Statics.schedulerPlay = schedulerPlay;
    }

    private static Scheduler schedulerPlay=null;


    public static Scheduler getSchedulerSpeaker() {
        return schedulerSpeaker;
    }

    public static void setSchedulerSpeaker(Scheduler schedulerSpeaker) {
        Statics.schedulerSpeaker = schedulerSpeaker;
    }

    private static Scheduler schedulerSpeaker=null;
}
