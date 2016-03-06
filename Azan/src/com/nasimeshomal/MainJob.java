package com.nasimeshomal;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by ma.ramezani on 3/5/2016.
 */
public class MainJob implements Job{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Logger logger= LoggerFactory.getLogger(MainJob.class);
        logger.info(String.format("MainJob is running : %s",new DateTime().toString()));

        Azan azan=null;

        try {
            azan = this.getNextAzan();
            logger.info(azan.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (azan!=null)
        {
            int offsetForPlay=48;
            int offsetForTurningSpeakerOn=60;

            try {
                if (Statics.getSchedulerPlay()==null)
                {
                    Statics.setSchedulerPlay(new StdSchedulerFactory().getScheduler());
                    Statics.getSchedulerPlay().start();
                    logger.info("new Scheduler is initialized for Play.");
                }

                JobKey jobKeyPlay=new JobKey(azan.getAzanDateTime(),"Play");

                if (!Statics.getSchedulerPlay().checkExists(jobKeyPlay))
                {
                    DateTime timeToPlayAzan=DateTime.parse(azan.getAzanDateTime()).minusSeconds(offsetForPlay);

                    JobDetail job= JobBuilder.newJob(PlayJob.class).withIdentity(azan.getAzanDateTime(),"Play").build();
                    logger.info(String.format("New Job is created : %s",job.getKey().toString()));

                    SimpleTrigger trigger= (SimpleTrigger) TriggerBuilder.newTrigger()
                            .withIdentity(azan.getAzanDateTime(),"Play")
                            .startAt(timeToPlayAzan.toDate())
                            .build();

                    logger.info(String.format("New Trigger is created : %s",trigger.getKey().toString()));

                    Statics.getSchedulerPlay().scheduleJob(job,trigger);

                    logger.info(String.format("New Job is scheduled for Play"));
                }
                else {
                    logger.info(String.format("Job exists : %s",jobKeyPlay.toString()));
                }

                if (Statics.getSchedulerSpeaker()==null)
                {
                    Statics.setSchedulerSpeaker(new StdSchedulerFactory().getScheduler());
                    Statics.getSchedulerPlay().start();
                    logger.info("new Scheduler is initialized for Speaker.");
                }

                JobKey jobKeySpeaker=new JobKey(azan.getAzanDateTime(),"Speaker");

                if (!Statics.getSchedulerSpeaker().checkExists(jobKeySpeaker))
                {
                    DateTime timeToTurnOnSpeaker=DateTime.parse(azan.getAzanDateTime()).minusSeconds(offsetForTurningSpeakerOn);

                    JobDetail job2= JobBuilder.newJob(TurnOnSpeakerJob.class).withIdentity(azan.getAzanDateTime(),"Speaker").build();
                    logger.info(String.format("New Job is created : %s",job2.getKey().toString()));


                    SimpleTrigger trigger2= (SimpleTrigger) TriggerBuilder.newTrigger()
                            .withIdentity(azan.getAzanDateTime(),"Speaker")
                            .startAt(timeToTurnOnSpeaker.toDate())
                            .build();

                    logger.info(String.format("New Trigger is created : %s",trigger2.getKey().toString()));

                    Statics.getSchedulerSpeaker().scheduleJob(job2,trigger2);
                    logger.info(String.format("New Job is scheduled for Speaker"));
                }
                else {
                    logger.info(String.format("Job exists : %s",jobKeySpeaker.toString()));
                }

            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

    private Azan getNextAzan() throws SQLException {
        DateTime today=new DateTime();
        String todayStr=today.toString("yyyy-MM-dd");

        Connection connection = DriverManager.getConnection("jdbc:sqlite:Azan.db");

        Statement statement = connection.createStatement();

        ResultSet rs= statement.executeQuery("SELECT * FROM Times;");

        ArrayList<Azan> tempList=new ArrayList<>();

        while (rs.next())
        {
            int azanId=rs.getInt("AzanId");
            String azanDateTime=rs.getString("AzanDateTime");
            int azanType=rs.getInt("AzanType");

            if (azanDateTime.startsWith(todayStr))
            {
                tempList.add(new Azan(azanId,azanDateTime,azanType));
            }
        }

        rs.close();
        statement.close();
        connection.close();

        DateTime now=new DateTime();

        ArrayList<Azan> tempList2=new ArrayList<>();

        for (Azan az:tempList)
        {
            DateTime azanDateTime=DateTime.parse(az.getAzanDateTime());

            if (now.isBefore(azanDateTime))
            {
                tempList2.add(az);
            }
        }

        Azan azanMinDistance=null;

        if (tempList2.size()>0)
        {
            azanMinDistance=tempList2.get(0);

            for (Azan az:tempList2)
            {
                Seconds s=Seconds.secondsBetween(now,DateTime.parse(az.getAzanDateTime()));
                int seconds=s.getSeconds();


                Seconds sMin=Seconds.secondsBetween(now,DateTime.parse(azanMinDistance.getAzanDateTime()));
                int secondsMin=sMin.getSeconds();

                if (seconds<secondsMin)
                {
                    azanMinDistance=az;
                }
            }
        }

        return azanMinDistance;
    }
}
