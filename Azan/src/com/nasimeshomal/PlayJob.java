package com.nasimeshomal;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

/**
 * Created by Mahmood on 3/5/2016.
 */
public class PlayJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Logger logger= LoggerFactory.getLogger(TurnOnSpeakerJob.class);

        try {
            logger.info("%s : Azan is playing now ...",new Date().toString());

            this.PlayAzan();
            logger.info("%s : End of playing Azan.",new Date().toString());

            Thread.sleep(4000);
            RaspGPIO.getInstance().getSpeakerPin().low();
            logger.info("%s : Speaker is Off.",new Date().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.trace("%s : Exeption while playing Azan ==> %s",new Date().toString(),e.getMessage());
        }
    }


    private void PlayAzan(){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("Azan1.mp3");

            Player player=new Player(fileInputStream);
            player.play();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        }
    }
}
