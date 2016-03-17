package com.nasimeshomal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Mahmood on 3/5/2016.
 */
public class TurnOnSpeakerJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        RaspGPIO.getInstance().getSpeakerPin().high();
    }
}
