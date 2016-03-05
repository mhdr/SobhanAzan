package com.nasimeshomal;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws SchedulerException, SQLException, FileNotFoundException, JavaLayerException {

        JobDetail job= JobBuilder.newJob(MainJob.class).withIdentity("Main","Main").build();

        SimpleTrigger trigger= TriggerBuilder.newTrigger()
                .withIdentity("Main","Main")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1).repeatForever())
                .build();

        //Scheduler scheduler=new StdSchedulerFactory().getScheduler();
        //scheduler.start();
        //scheduler.scheduleJob(job,trigger);

        Azan azan = getNextAzan();

        System.out.println(azan.toString());
    }

    private static Azan getNextAzan() throws SQLException {
        DateTime today=new DateTime();
        String todayStr=today.toString("yyyy-MM-dd");

        Connection connection = DriverManager.getConnection("jdbc:sqlite:Azan.db");

        Statement statement = connection.createStatement();

        ResultSet rs= statement.executeQuery("SELECT * FROM Times;");
        Azan azan=null;

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



        Seconds s=Seconds.secondsBetween(now,DateTime.parse(tempList2.get(0).getAzanDateTime()));

        int seconds=s.getSeconds();

        return azan;
    }

    private static void PlayAzan() throws FileNotFoundException, JavaLayerException {
        FileInputStream fileInputStream = new FileInputStream("Azan1.mp3");
        Player player=new Player(fileInputStream);
        player.play();
    }
}
