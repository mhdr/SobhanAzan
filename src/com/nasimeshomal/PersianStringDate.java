package com.nasimeshomal;

import net.time4j.PlainDate;
import net.time4j.calendar.PersianCalendar;
import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Created by ma.ramezani on 2/28/2016.
 */
public class PersianStringDate {

    public static DateTime GetGregorianDateTime(String dateStr,String timeStr)
    {
        String yearStr=dateStr.substring(0,4);
        String monthStr=dateStr.substring(4,6);
        String dayStr=dateStr.substring(6,8);

        String hourStr= timeStr.split(":")[0];
        String minuteStr= timeStr.split(":")[1];
        String secondStr= timeStr.split(":")[2];

        int year=Integer.parseInt(yearStr);
        int month=Integer.parseInt(monthStr);
        int day=Integer.parseInt(dayStr);

        PersianCalendar jalali=PersianCalendar.of(year,month,day);
        PlainDate plainDate=jalali.transform(PlainDate.class);

        int hour=Integer.parseInt(hourStr);
        int minute=Integer.parseInt(minuteStr);
        int second=Integer.parseInt(secondStr);

        DateTime dateTime=new DateTime(plainDate.getYear(),plainDate.getMonth(),plainDate.getDayOfMonth(),hour,minute,second);

        return dateTime;
    }

    public static ArrayList<String> GetDates(PersianCalendar from, PersianCalendar to)
    {
        ArrayList<String> result=new ArrayList<>();

        PersianCalendar current=from;
        PersianCalendar end=to.plus(1, PersianCalendar.Unit.DAYS);

        while (!current.isSimultaneous(end))
        {
            int year=current.getYear();
            int month=current.getMonth().getValue();
            int day=current.getDayOfMonth();

            String monthStr= String.valueOf(month);

            if (month<=9)
            {
                monthStr=String.format("0%d",month);
            }

            String dayStr= String.valueOf(day);

            if (day<=9)
            {
                dayStr=String.format("0%d",day);
            }

            String currentStr=String.format("%s%s%s",year,monthStr,dayStr);
            result.add(currentStr);
            current= current.plus(1, PersianCalendar.Unit.DAYS);
        }

        return result;
    }

    public static ArrayList<String> GetDates(PersianCalendar from, int count)
    {
        ArrayList<String> result=new ArrayList<>();

        PersianCalendar current=from;
        int currentCounter=0;

        while (currentCounter<count)
        {
            int year=current.getYear();
            int month=current.getMonth().getValue();
            int day=current.getDayOfMonth();

            String monthStr= String.valueOf(month);

            if (month<=9)
            {
                monthStr=String.format("0%d",month);
            }

            String dayStr= String.valueOf(day);

            if (day<=9)
            {
                dayStr=String.format("0%d",day);
            }

            String currentStr=String.format("%s%s%s",year,monthStr,dayStr);
            result.add(currentStr);
            current= current.plus(1, PersianCalendar.Unit.DAYS);
            currentCounter++;
        }

        return result;
    }
}
