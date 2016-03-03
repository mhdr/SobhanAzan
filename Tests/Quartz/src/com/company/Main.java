package com.company;

import org.joda.time.DateTime;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Main {

    public static void main(String[] args) throws SchedulerException {

        DateTime dateTime=new DateTime();
        dateTime =dateTime.plusMinutes(1);

        JobDetail job= JobBuilder.newJob(Task1.class).withIdentity("job1","group1").build();
        JobDetail job2=JobBuilder.newJob(Task2.class).withIdentity("job2","group2").build();

        System.out.println(job.getKey().toString());

        SimpleTrigger trigger= (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity("trigger1","group1")
                .startAt(dateTime.toDate())
                .build();

        SimpleTrigger trigger2=TriggerBuilder.newTrigger()
                .withIdentity("trigger2","group2")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).withRepeatCount(10))
                .build();

        System.out.println(String.format("Start : %s",new DateTime().toString()));
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);

        job2.getJobDataMap().put("scheduler",scheduler);
        Scheduler scheduler2=new StdSchedulerFactory().getScheduler();
        scheduler2.start();
        scheduler2.scheduleJob(job2,trigger2);

        System.out.println(scheduler.checkExists(new JobKey("job1","group1")));
    }
}
