package com.nasimeshomal;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Mahmood on 3/5/2016.
 */
public class TurnOnSpeakerJob implements Job {
    @Override
    public void execute(JobExecutionContext context){
        Logger logger= LoggerFactory.getLogger(TurnOnSpeakerJob.class);

        try {
            RaspGPIO.getInstance().getSpeakerPin().high();
            logger.info("%s : Speaker is On.",new Date().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.trace("%s : Exeption while turning Speaker On ==> %s",new Date().toString(),e.getMessage());
        }

    }
}
