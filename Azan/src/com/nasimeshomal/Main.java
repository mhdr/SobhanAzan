package com.nasimeshomal;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SchedulerException, SQLException, FileNotFoundException, JavaLayerException {

        JobDetail job= JobBuilder.newJob(MainJob.class).withIdentity("Main","Main").build();

        SimpleTrigger trigger= TriggerBuilder.newTrigger()
                .withIdentity("Main","Main")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1).repeatForever())
                .build();

        Scheduler scheduler=new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job,trigger);
    }
}
