package FetchData;

import net.time4j.PlainDate;
import net.time4j.SystemClock;
import net.time4j.calendar.PersianCalendar;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        Boolean useJson=true;

        PlainDate today = SystemClock.inLocalView().today();
        PersianCalendar from = null;
        PersianCalendar to=null;
        ArrayList<String> dates;

        if (args.length==3)
        {
            from = today.transform(PersianCalendar.class);

            int year= Integer.parseInt(args[0]);
            int month= Integer.parseInt(args[1]);
            int day=Integer.parseInt(args[2]);
            to=PersianCalendar.of(year,month,day);
            dates= PersianStringDate.GetDates(from,to);
        }
        else
        {
            from = today.transform(PersianCalendar.class);
            dates= PersianStringDate.GetDates(from,4*365);
        }

        String baseUrl="http://www.tabnak.ir/fa/prayer/time/25/25_163/";

        if (useJson) {

            ArrayList<Azan> azanTimes=new ArrayList<Azan>();

            for (String date : dates) {
                String url = String.format("%s%s", baseUrl, date);
                Document doc = Jsoup.connect(url).get();
                doc.charset(Charset.forName("UTF-8"));
                Elements elements = doc.getElementsByClass("ptime_col2");

                String azanSob = Fa2En.Convert(elements.get(0).text());
                String azanZohr = Fa2En.Convert(elements.get(2).text());
                String azanMaghreb = Fa2En.Convert(elements.get(4).text());

                System.out.print(date.toString());
                System.out.println(String.format(" => Sob : %s , Zohr : %s , Maghreb : %s", azanSob, azanZohr, azanMaghreb));

                DateTime sobDateTime = PersianStringDate.GetGregorianDateTime(date.toString(), azanSob);
                DateTime zohrDateTime = PersianStringDate.GetGregorianDateTime(date.toString(), azanZohr);
                DateTime maghrebDateTime = PersianStringDate.GetGregorianDateTime(date.toString(), azanMaghreb);

                Azan azan1=new Azan();
                azan1.AzanDateTime=sobDateTime.toString();
                azan1.AzanType=1;


                Azan azan2=new Azan();
                azan2.AzanDateTime=zohrDateTime.toString();
                azan2.AzanType=2;

                Azan azan3=new Azan();
                azan3.AzanDateTime=maghrebDateTime.toString();
                azan3.AzanType=3;

                azanTimes.add(azan1);
                azanTimes.add(azan2);
                azanTimes.add(azan3);
            }

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("Azan",azanTimes);

            File AzanFile=new File("Azan.json");
            AzanFile.createNewFile();
            FileWriter writer=new FileWriter(AzanFile);
            jsonObject.writeJSONString(writer);
            writer.flush();
            writer.close();
        }
        else {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:Azan.db");
            connection.setAutoCommit(false);

            for (String date:dates)
            {
                Statement statement= null;
                try {
                    statement = connection.createStatement();

                    String url=String.format("%s%s",baseUrl,date);
                    Document doc = Jsoup.connect(url).get();
                    doc.charset(Charset.forName("UTF-8"));
                    Elements elements= doc.getElementsByClass("ptime_col2");

                    String azanSob=Fa2En.Convert(elements.get(0).text());
                    String azanZohr=Fa2En.Convert(elements.get(2).text());
                    String azanMaghreb=Fa2En.Convert(elements.get(4).text());

                    System.out.print(date.toString());
                    System.out.println(String.format(" => Sob : %s , Zohr : %s , Maghreb : %s",azanSob,azanZohr,azanMaghreb));

                    DateTime sobDateTime=PersianStringDate.GetGregorianDateTime(date.toString(),azanSob);
                    DateTime zohrDateTime=PersianStringDate.GetGregorianDateTime(date.toString(),azanZohr);
                    DateTime maghrebDateTime=PersianStringDate.GetGregorianDateTime(date.toString(),azanMaghreb);

                    String command=String.format("INSERT INTO Times(AzanDateTime,AzanType) VALUES ('%s',%d);",sobDateTime.toString(),1);
                    statement.execute(command);

                    String command2=String.format("INSERT INTO Times(AzanDateTime,AzanType) VALUES ('%s',%d);",zohrDateTime.toString(),2);
                    statement.execute(command2);

                    String command3=String.format("INSERT INTO Times(AzanDateTime,AzanType) VALUES ('%s',%d);",maghrebDateTime.toString(),3);
                    statement.execute(command3);

                    statement.close();
                    connection.commit();
                } catch (SQLException e) {
                    if (e.getErrorCode()==19)
                    {
                        System.out.println(String.format("%s is already on the database.",date));
                    }
                    else {
                        e.printStackTrace();
                    }
                }
            }

            connection.close();
        }

    }
}
