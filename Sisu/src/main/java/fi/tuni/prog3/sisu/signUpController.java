package fi.tuni.prog3.sisu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller class for the sign-up window
 * @author Onni, Rauli, Viljami
 */

public class signUpController implements Initializable {
    @FXML
    private TextField studentNumber;
    @FXML
    private TextField name;
    @FXML
    private TextField startYear;
    @FXML
    private TextField endYear;
    @FXML
    private Label signUpError;
    @FXML
    private Label loadUserError;
    @FXML
    private ComboBox<Student> userDropDown;

    /**
     * Initializes sign-up window
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known
     * @param rp The resources used to localize the root object, or null if the root object was not localized
     * @author Onni, Rauli
     */
    @Override
    public void initialize(URL url, ResourceBundle rp){
        loadUserError.setWrapText(true);
        signUpError.setWrapText(true);
        UserData.clear();
        UserData.readFromJson();
        if(UserData.getStudents().isEmpty()){
            loadUserError.setText("No users currently available. Please create a new user");
        }
        else{
            userDropDown.getItems().addAll(UserData.getStudents());
        }
    }

    /**
     * Handles sign-up button click
     * @param event mouse-click event
     * @throws IOException if an I/O error occurs
     * @author Onni
     */
    public void signUpButtonClick(ActionEvent event) throws IOException {
        if(validateFields()){
            if(UserData.isStudentAlready(studentNumber.getText())){
                signUpError.setText("Student with the same student number already exists!");
            }
            else{
                String name = this.name.getText();
                String studentNumber = this.studentNumber.getText();
                int startYear = Integer.parseInt(this.startYear.getText());
                int endYear = Integer.parseInt(this.endYear.getText());
                Student student = new Student(studentNumber, name, startYear, endYear);
                UserData.addStudent(student);
                UserData.setCurrent(student);
                UserData.writeToJson();

                //get stage information
                URL url = new File("src/main/resources/mainWindow.fxml").toURI().toURL();
                Parent mainWindowParent = FXMLLoader.load(url);
                Scene mainWindowScene = new Scene(mainWindowParent);
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(mainWindowScene);
                stage.show();
            }
        }
    }

    /**
     * Handles sign-in button click
     * @param event mouse-click event
     * @throws IOException if an I/O error occurs
     * @author Onni, Rauli
     */
    public void signInButtonClick(ActionEvent event) throws IOException {
        if(userDropDown.getValue() != null){
            UserData.setCurrent((Student) userDropDown.getValue());
            if (UserData.getCurrent().getDegree().getCode() != null){
                ParseData.changeLanguage("fi");
            }
            DegreeProgramme degProg = UserData.getCurrent().getDegree();
            if (degProg.getSource() != null){
                degProg = ParseData.fillDegreeData(degProg);
                UserData.getCurrent().setDegree(degProg);
            }
            URL url = new File("src/main/resources/mainWindow.fxml").toURI().toURL();
            Parent mainWindowParent = FXMLLoader.load(url);
            Scene mainWindowScene = new Scene(mainWindowParent);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(mainWindowScene);
            stage.show();
        }
    }
    
    /**
     * Validates the user inputs of the sign up window.
     * @return True if the user inputs were valid, false if not.
     * @author Viljami
     */
    private boolean validateFields(){
        String studentNum = this.studentNumber.getText();
        String checkNumResult = validateStudentNumber(studentNum);
        if(!checkNumResult.equals("ok")){
            signUpError.setText(checkNumResult);
            return false;
        }
        String studentName = this.name.getText();
        if (studentName == null || studentName.isEmpty() || studentName.trim().isEmpty()){
            signUpError.setText("No name given.");
            return false;
        }
        try{
            int startYear = Integer.parseInt(this.startYear.getText());
            int endYear = Integer.parseInt(this.endYear.getText());
            String checkYearsResult = validateYears(startYear, endYear);
            if(!checkYearsResult.equals("ok")){
                signUpError.setText(checkYearsResult);
                return false;
            }
        }
        catch(NumberFormatException e){
            signUpError.setText("The years must be numbers.");
            return false;
        }
        return true;
    }
    
    /**
     * Validates student numbers.
     * @param studentNum The student number to be validated.
     * @return "ok" if all tests were passed, otherwise describes what was wrong.
     */
    public static String validateStudentNumber(String studentNum){
        if (studentNum == null || studentNum.isEmpty() || studentNum.trim().isEmpty()){
            return "No student number given.";
        }
        else if(studentNum.length() < 6 || studentNum.length() > 7){
            return "The student number must be 6 to 7 characters long.";
        }
        if (studentNum.length() == 7) {
            if (studentNum.charAt(0) != 'H'){
                return "The student number should begin with 'H' if it's 7 characters long.";
            }
            else{
                studentNum = studentNum.substring(1);
            }
        }
        try{
            Integer.parseInt(studentNum);
            return "ok";
        }
        catch(NumberFormatException e){
            return "The student number should be a number.";
        }
    }
    
    /**
     * Validates start and end years.
     * @param startYear The start year to be validated.
     * @param endYear The end year to be validated.
     * @return "ok" if all tests were passed, otherwise describes what was wrong.
     */
    public static String validateYears(int startYear, int endYear){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy"); 
        int currentYear = Integer.parseInt(dtf.format(LocalDateTime.now()));
        if(startYear > currentYear){
            return "Start year can be the current year at most.";
        }
        else if(startYear+10 < currentYear){
            return "Start year can be max 10 years earlier than the current year.";
        }
        else if(endYear < currentYear){
            return "Estimated graduation year can be the current year at least.";
        }
        else if(endYear-10 > currentYear){
            return "Estimated graduation year can be max 10 years after the current year";
        }
        else if(startYear >= endYear){
            return "Start year must be before the end year.";
        }
        return "ok";
    }
}
