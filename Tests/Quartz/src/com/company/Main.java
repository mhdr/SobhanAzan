package com.company;

import org.joda.time.DateTime;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Main {

    public static void main(String[] args) throws SchedulerException {

        DateTime dateTime=new DateTime();
        dateTime =dateTime.plusMinutes(1);

        JobDetail job= JobBuilder.newJob(Task1.class).withIdentity("job1","group1").build();

        SimpleTrigger trigger= (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity("trigger1","group1")
                .startAt(dateTime.toDate())
                .build();


        System.out.println(String.format("Start : %s",new DateTime().toString()));
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
