package com.nasimeshomal;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by ma.ramezani on 3/5/2016.
 */
public class MainJob implements Job{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        Azan azan=null;

        try {
            azan = this.getNextAzan();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (azan!=null)
        {
            // TODO check if scheduler is present

            int offsetForPlay=48;
            int offsetForTurningSpeakerOn=60;

            DateTime timeToPlayAzan=DateTime.parse(azan.getAzanDateTime()).minusSeconds(offsetForPlay);

            JobDetail job= JobBuilder.newJob(PlayJob.class).withIdentity(azan.getAzanDateTime(),"Play").build();

            SimpleTrigger trigger= (SimpleTrigger) TriggerBuilder.newTrigger()
                    .withIdentity(azan.getAzanDateTime(),"Play")
                    .startAt(timeToPlayAzan.toDate())
                    .build();

            Scheduler scheduler= null;
            try {
                scheduler = new StdSchedulerFactory().getScheduler();
                scheduler.start();
                scheduler.scheduleJob(job,trigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }

            DateTime timeToTurnOnSpeaker=DateTime.parse(azan.getAzanDateTime()).minusSeconds(offsetForTurningSpeakerOn);

            JobDetail job2= JobBuilder.newJob(TurnOnSpeakerJob.class).withIdentity(azan.getAzanDateTime(),"Speaker").build();

            SimpleTrigger trigger2= (SimpleTrigger) TriggerBuilder.newTrigger()
                    .withIdentity(azan.getAzanDateTime(),"Speaker")
                    .startAt(timeToTurnOnSpeaker.toDate())
                    .build();

            Scheduler scheduler2= null;
            try {
                scheduler2 = new StdSchedulerFactory().getScheduler();
                scheduler2.start();
                scheduler2.scheduleJob(job2,trigger2);
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
