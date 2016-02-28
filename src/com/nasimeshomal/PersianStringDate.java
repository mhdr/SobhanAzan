package com.nasimeshomal;

import net.time4j.calendar.PersianCalendar;

import java.util.ArrayList;

/**
 * Created by ma.ramezani on 2/28/2016.
 */
public class PersianStringDate {

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
