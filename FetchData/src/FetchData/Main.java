package FetchData;

import net.time4j.PlainDate;
import net.time4j.SystemClock;
import net.time4j.calendar.PersianCalendar;
import org.joda.time.DateTime;
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

public class Main {

    public static void main(String[] args) throws IOException, SQLException {

        Boolean usePlainText=true;

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

        if (usePlainText) {

            File AzanFile=new File("Azan.txt");
            AzanFile.createNewFile();
            FileWriter writer=new FileWriter(AzanFile);

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

                String line1=String.format("%s,%s\r\n",Formatter.format(sobDateTime),1);
                String line2=String.format("%s,%s\r\n",Formatter.format(zohrDateTime),2);
                String line3=String.format("%s,%s\r\n",Formatter.format(maghrebDateTime),3);

                writer.write(line1);
                writer.write(line2);
                writer.write(line3);
            }

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
