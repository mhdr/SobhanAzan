package com.nasimeshomal;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Mahmood on 3/5/2016.
 */
public class TurnOnSpeakerJob implements Job {
    @Override
    public void execute(JobExecutionContext context){
        Logger logger= LoggerFactory.getLogger(TurnOnSpeakerJob.class);

        try {
            RaspGPIO.getInstance().TurnOnAmp();
            logger.info(String.format("%s : Speaker is On.",new DateTime().toString()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.trace(String.format("%s : Exeption while turning Speaker On ==> %s",new DateTime().toString(),e.getMessage()));
        }

    }
}
