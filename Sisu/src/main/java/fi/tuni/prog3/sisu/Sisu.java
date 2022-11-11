package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.scene.media.Media;  
import javafx.scene.media.MediaPlayer;  
import javafx.scene.media.MediaView;
import javafx.scene.layout.StackPane;

/**
 * Main class of the application. Here is where it all starts.
 * @author Onni, Rauli
 */
public class Sisu extends Application {  

    /** 
     * @param args application launcing parameters (null)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Transfers to the SignUp
     * @param stage of the root
     * @throws IOException if reading the fxml of the SignUp page is incorrect.
     * @author Onni
     */
    private void toSignUp(Stage stage) throws IOException{
        URL url = new File("src/main/resources/signUp.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);       
        stage.show();
    }
   
     /** 
     * Launches the intro -animation and reads the degree data meanwhile
     * @param stage of the root
     * @throws IOException if reading the intro is erroneus
     * @author Rauli
     */
    @Override
    public void start(Stage stage) throws IOException {
        
        StackPane root = new StackPane();
        String path = "src/main/resources/intro.mp4";  
        String myFile = new File(path).toURI().toString();  
        Media media = new Media(myFile);
        MediaPlayer mediaPlayer = new MediaPlayer(media);   
        MediaView mediaView = new MediaView(mediaPlayer);
        root.getChildren().add( mediaView);
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
        mediaPlayer.play();
        ParseData.buildDegreeList();
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run () {
                try {
                    toSignUp(stage);
                }
                catch (IOException e){
                    System.err.println("Error reading signUp.fxml" + e.getMessage());
                }                
            } 
        });
    }
}
