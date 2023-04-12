import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
 

public class App extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

@Override 
public void start(Stage pstage) throws UnsupportedAudioFileException, IOException{
    pstage.setTitle("SoundStage");

    GridPane pane = new GridPane();

    Button btSound = new Button();
    btSound.setText("Start");
    Button btFrames = new Button();
    btFrames.setText("Get frames");

    Label EndLab = new Label();
    //Label voiceLab = new Label();
    Label pathLab = new Label();
    Label posLabel = new Label();
    Label randLabel = new Label();
    Label title = new Label();

    TextField EndField = new TextField();
    TextField pathField = new TextField();
    TextField posField = new TextField();
    TextField randField = new TextField();

    EndLab.setText("\tWhat percent of the clip should the end position be?");
    EndLab.setTextFill(Color.WHITE);
    pathLab.setText("What is the path to the audio file you would like to use?");
    pathLab.setTextFill(Color.WHITE);
    posLabel.setText("What percent of the clip should the start position be?");
    posLabel.setTextFill(Color.WHITE);
    randLabel.setText("\tHow many frames of randomness should be implemented?");
    randLabel.setTextFill(Color.WHITE);
    title.setText("SoundStage");
    title.setFont(new Font("Courier New",20));
    title.setTextFill(Color.WHITE);

    pane.add(title, 1,0);
    pane.add(pathLab,0,2);
    pane.add(pathField,0,3);
    pane.add(posLabel,0,4);
    pane.add(posField,0,5);
    pane.add(EndLab,1,4);
    pane.add(EndField,1,5);
    pane.add(randLabel,1,2);
    pane.add(randField,1,3);
    pane.add(btSound,0,10);
    pane.add(btFrames,0,12);

    btFrames.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e){
            try {
                try {
                    printFrames(pathField.getText());
                } catch (LineUnavailableException e1) {
                    System.out.println("Line Unavailable");
                    }
            } catch (UnsupportedAudioFileException e1) {
                System.out.println("Audio File not supported, try .wav");
            } catch (IOException e1) {
                System.out.println("No file");
            }
        }
    }
    );

    btSound.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e){
            try {
                String path = pathField.getText();
                String end = EndField.getText();
                String rand = randField.getText();
                String start = posField.getText();
                startSound(path, rand, start, end);
            } catch (UnsupportedAudioFileException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                    System.out.println("No file :(");
            } catch (LineUnavailableException e1) {
                System.out.println("Line Unavailable");
            }
        }
    });

    BackgroundFill backgroundFill = new BackgroundFill(Color.valueOf("#114655"), new CornerRadii(0), new Insets(0));
    Background background = new Background(backgroundFill);
    pane.setBackground(background);

    Scene scene = new Scene(pane, 700, 500);

    pstage.setScene(scene);

    pstage.show();
}

public static void startSound(String path, String rand, String start, String end) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
    Clip clip;
    AudioInputStream audioInputStream;
    audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
    clip = AudioSystem.getClip();
    clip.open(audioInputStream);
    int frames = clip.getFrameLength();
    double startPos = Double.parseDouble(start)/100 * frames;
    double endPos = Double.parseDouble(end)/100 * frames;
    Random randResult = new Random();
    clip.setLoopPoints((int)startPos, (int)endPos);
    clip.loop(Clip.LOOP_CONTINUOUSLY);
    long startTime = System.currentTimeMillis();
    //int lebron = 100000;
    while(System.currentTimeMillis() != startTime + 20000){
        int startRand = (int)startPos + randResult.nextInt(Integer.parseInt(rand));
        int endRand = (int)endPos + randResult.nextInt(Integer.parseInt(rand));
        clip.setLoopPoints(startRand, endRand);
        //System.out.println(lebron);
        //lebron--;
    }
    clip.close();
}

public static void printFrames(String path) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
    Clip clip;
    AudioInputStream audioInputStream;
    audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
    clip = AudioSystem.getClip();
    clip.open(audioInputStream);
    System.out.println(clip.getFrameLength());
    clip.close();
    audioInputStream.close();
}
}