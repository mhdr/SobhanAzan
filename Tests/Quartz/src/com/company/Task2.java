package com.company;

import org.joda.time.DateTime;
import org.quartz.*;

/**
 * Created by Mahmood on 3/4/2016.
 */
public class Task2 implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap= context.getMergedJobDataMap();
        Scheduler scheduler= (Scheduler) dataMap.get("scheduler");

        try {
            System.out.println(String.format("%s Task 2 Job 1 Exists : %s",new DateTime().toString()
                    ,scheduler.checkExists(new JobKey("job1","group1"))));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
