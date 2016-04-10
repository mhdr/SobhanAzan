package FetchData;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ma.ramezani on 4/10/2016.
 */
public class Formatter {
    public static String format(Date date)
    {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd H:mm:ss");
        String result=formatter.format(date);
        return result;
    }

    public static String format(DateTime date)
    {
        Date input=date.toDate();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd H:mm:ss");
        String result=formatter.format(input);
        return result;
    }

    public static Date parse(String dateStr) throws ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy.MM.dd H:mm:ss");
        Date result=formatter.parse(dateStr);
        return result;
    }
}
