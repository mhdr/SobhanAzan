package PlayAzan;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

/**
 * Created by Mahmood on 3/2/2016.
 */
public class Main extends Application{
    public static void main(String[] args){
        Application.launch(args);
        //Media media=new Media(new File("Azan1.mp3").toURI().toString());
        //MediaPlayer mediaPlayer=new MediaPlayer(media);
        //mediaPlayer.play();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Media media=new Media(new File("Azan1.mp3").toURI().toString());
        MediaPlayer mediaPlayer=new MediaPlayer(media);
        mediaPlayer.play();
    }
}
