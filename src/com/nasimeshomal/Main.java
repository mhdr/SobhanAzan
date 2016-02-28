package com.nasimeshomal;

import net.time4j.PlainDate;
import net.time4j.SystemClock;
import net.time4j.calendar.PersianCalendar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        PlainDate today = SystemClock.inLocalView().today();
        PersianCalendar from = today.transform(PersianCalendar.class);
        PersianCalendar to=PersianCalendar.of(1394,12,20);

        ArrayList<String> dates= PersianStringDate.GetDates(from,to);

        String baseUrl="http://www.tabnak.ir/fa/prayer/time/25/25_163/";

        for (String date:dates)
        {
            String url=String.format("%s%s",baseUrl,date);
            Document doc = Jsoup.connect(url).get();
            doc.charset(Charset.forName("UTF-8"));
            Elements elements= doc.getElementsByClass("ptime_col2");

            String azanSob=Fa2En.Convert(elements.get(0).text());
            String azanZohr=Fa2En.Convert(elements.get(2).text());
            String azanMaghreb=Fa2En.Convert(elements.get(4).text());

            System.out.print(date.toString());
            System.out.println(String.format(" => Sob : %s , Zohr : %s , Maghreb : %s",azanSob,azanZohr,azanMaghreb));
        }

    }
}
