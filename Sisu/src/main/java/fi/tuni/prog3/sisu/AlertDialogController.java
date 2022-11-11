package fi.tuni.prog3.sisu;

import java.io.File;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Confirmation dialog to delete current profile or 
 * change the language to English.
 * @author Rauli
 */
public class AlertDialogController {

   
    @FXML
    private Label alertText;

    private MouseEvent parentEvent;
    private int askingValue = 0;

    /**
     * Handles the actions, when confirmed.
     * @param event of the mouse click "ok".
     */
  
    public void onOkButtonPressed(MouseEvent event){

        // 1 = delete user
        if (askingValue == 1){
            try{
                UserData.removeStudent(UserData.getCurrent().getStudentNumber());
                UserData.writeToJson();
                URL url = new File("src/main/resources/signUp.fxml").toURI().toURL();
                Parent root = FXMLLoader.load(url);
                Scene scene = new Scene(root);        
                Stage stage = (Stage)((Node)parentEvent.getSource()).getScene().getWindow();            
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                Node  source = (Node)  event.getSource(); 
                Stage stage2  = (Stage) source.getScene().getWindow();
                stage2.close();
            }
            catch (Exception e){};

        //2 = change to english
        } else if (askingValue == 2){
            
            Node  source = (Node)  event.getSource(); 
            Stage alertDialogStage  = (Stage) source.getScene().getWindow();
            try{   
                ParseData.changeLanguage("en");        
                alertDialogStage.close();
                Stage oldMainWindowStage = (Stage)((Node)parentEvent.getSource()).getScene().getWindow();
                oldMainWindowStage.close();
                URL url = new File("src/main/resources/mainWindow.fxml").toURI().toURL();       
                Parent mainWindowParent = FXMLLoader.load(url);
                Scene mainWindowScene = new Scene(mainWindowParent);
                alertDialogStage.setScene(mainWindowScene);
                alertDialogStage.show();           
            }
            catch (Exception e){};
        }      
    }

    /**
     * Handles the actions, when confirmed.
     * @param event of the mouse click "ok".
     */

    public void onCancelButtonPressed(MouseEvent event){
            Node  source = (Node)  event.getSource(); 
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
    }

    /**
     * Sets the text of the Alert
     * @param text of the Alert
     */
    public void setAlertText(String text){
        alertText.setText(text);
    }

    /**
     * Defines which one of operations will be executed.
     * @param askingId 1 = delete user, 2=change language
     * @param event of the mouse clicked either on "en" in top bar or "delete" on profile tab.
     */
    public void asking(int askingId, MouseEvent event){
        this.parentEvent = event;
        this.askingValue = askingId;
    }    
}
