package com.nasimeshomal;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Mahmood on 3/5/2016.
 */
public class PlayJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            this.PlayAzan();
            Thread.sleep(4000);
            RaspGPIO.getInstance().getSpeakerPin().low();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
