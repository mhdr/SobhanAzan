package PlayAzan;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.joda.time.DateTime;

import java.io.File;
import java.sql.*;

/**
 * Created by Mahmood on 3/2/2016.
 */
public class Main extends Application{
    public static void main(String[] args) throws SQLException {
        //Application.launch(args);

        Azan azan = getAzan();

        System.out.println(azan.toString());
    }

    private static Azan getAzan() throws SQLException {
        DateTime today=new DateTime();
        String todayStr=today.toString("yyyy-MM-dd");

        Connection connection = DriverManager.getConnection("jdbc:sqlite:Azan.db");

        Statement statement = connection.createStatement();

        ResultSet rs= statement.executeQuery("SELECT * FROM Times;");
        Azan azan=null;

        while (rs.next())
        {
            int azanId=rs.getInt("AzanId");
            String azanDateTime=rs.getString("AzanDateTime");
            int azanType=rs.getInt("AzanType");

            if (azanDateTime.startsWith(todayStr) && azanType==2)
            {
                azan=new Azan(azanId,azanDateTime,azanType);
            }

        }

        rs.close();
        statement.close();
        connection.close();
        return azan;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        Media media=new Media(new File("Azan1.mp3").toURI().toString());
        MediaPlayer mediaPlayer=new MediaPlayer(media);
        mediaPlayer.play();
    }
}
