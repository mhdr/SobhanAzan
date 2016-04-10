package com.nasimeshomal;

import javazoom.jl.decoder.JavaLayerException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws SchedulerException, SQLException, IOException, JavaLayerException, ClassNotFoundException, ParseException {

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
