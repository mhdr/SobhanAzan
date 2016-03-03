package com.company;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Mahmood on 3/3/2016.
 */
public class Task1 implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println(String.format("Job : %s",new DateTime().toString()));
    }
}
