package fi.tuni.prog3.sisu;

import static fi.tuni.prog3.sisu.signUpController.validateYears;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Main controller class of the UI. Handles several user actions.
 * @author Onni, Rauli
 */

public class mainWindowController implements Initializable {

    @FXML
    private Text avg_grade;
    @FXML
    private PieChart chart;
    @FXML 
    private Text total_done_credits;
    @FXML 
    private Text degree_text;
    @FXML 
    private Text total_all_credits;
    @FXML
    private LineChart<Integer, Double> lineChart;
    @FXML
    private NumberAxis periodsAxis;
    @FXML 
    private NumberAxis creditsAxis;
    @FXML 
    private Button saveUser;
    @FXML
    private Button deleteUser;

    //topbar
    @FXML
    private Button quit;
    @FXML
    private Button logOut;
    @FXML
    private Button langFi;
    @FXML
    private Button langEn;

    //Treemenu
    @FXML
    private Button expand_all;
    @FXML
    private Button collapse_all;
    @FXML 
    private Label attainment_error;
    @FXML
    private TextField choice_box_text;
    @FXML
    private ListView<DegreeProgramme> degreelist;
    @FXML
    private Label degree_info;
    @FXML
    private Label profileSuccessLabel;
    @FXML
    private Label profileErrorLabel;
    @FXML
    private AnchorPane attainment_pane;
    @FXML
    private Label tree_info;
    @FXML
    private WebView web_view;  
    @FXML 
    private CheckBox check_box;
    @FXML
    private ChoiceBox<Integer> choice_box;
    @FXML
    private DatePicker date_picker;
    @FXML
    private WebView tree_description;
    @FXML
    private TreeView<SisuItem> treeView;
    @FXML
    private Label degreeProgammeErrorLabel;
    @FXML
    private TextField profileName;
    @FXML
    private TextField profileStudentNum;
    @FXML
    private TextField profileStartYear;
    @FXML
    private TextField profileEndYear;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab structureTab;
    @FXML
    private Tab degree_tab;

    private ArrayList<DegreeProgramme> programList;
    private DegreeProgramme chosenProgram = new DegreeProgramme();
    private static ObservableList<PieChart.Data> pcData;
    private Image image;
    private boolean isLoadedDegree = false;
    private ImageView iView;

    /**
     * Initializes the Main window
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known
     * @param rp The resources used to localize the root object, or null if the root object was not localized
     * @author Onni, Rauli
     */

    @Override
    public void initialize(URL url, ResourceBundle rp){
                profileErrorLabel.setWrapText(true);
        try{ 
            web_view.getEngine().loadContent("");
            attainment_pane.setVisible(false);
            attainment_error.setVisible(false);
            tree_info.setText("");
            iView = new ImageView();
            InputStream stream = new FileInputStream("src/main/resources/check.png");
            image = new Image(stream);          
            iView.setImage(image);       
            iView.setFitHeight(16);
            iView.setFitWidth(16);
            
            if (UserData.getCurrent().getDegree().getCode() != null){
                isLoadedDegree = true;
                initDegreeProgramme();
            }
      
            programList = ParseData.getDegrees();
            for(DegreeProgramme d : programList){
                degreelist.getItems().add(d);
            }
   
            if (ParseData.getLanguage().equals("en")){
                langEn.setVisible(false);
                langFi.setVisible(true);
            }
            else if (ParseData.getLanguage().equals("fi")){
                langEn.setVisible(true);
                langFi.setVisible(false);
            }
            else {
                langEn.setVisible(false);
                langFi.setVisible(false);
            }
            myPie();
            changeMyPie(0, 100);
        }
        catch (IOException e){
            System.err.println("Error reading check.png" + e.getMessage());
        }
    }


    /**
     * Sets the current language english.
     * Asks first for confirmation
     * @param event of the mouse cliced top bar "en"
     * @author Rauli
     */

    public void setLanguageEnglish(MouseEvent event){
        try{
            URL url = new File("src/main/resources/alert.fxml").toURI().toURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent parent = fxmlLoader.load();
            AlertDialogController dialogController = fxmlLoader.<AlertDialogController>getController();
            dialogController.setAlertText("Are you sure you want change degree language to\nEnglish when available? \n"+
                                        "\nThis will take some time. (Up to 1 min)");
                
            Scene scene = new Scene(parent);
            dialogController.asking(2, event);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        }       
        catch (IOException e){ 
            System.err.println("Error reading alert.fxml" + e.getMessage());  
        }
    }


    /**
     * Sets the current language Finnish
     * @param event of the moouse cliced top bar "fi"
     * @author Rauli
     */

    public void setLanguageFinnish(MouseEvent event){
        try{
            ParseData.changeLanguage("fi");
            Node  source = (Node)  event.getSource(); 
            Stage stage2  = (Stage) source.getScene().getWindow();
            stage2.close();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
            URL url = new File("src/main/resources/mainWindow.fxml").toURI().toURL();  
            Parent mainWindowParent = FXMLLoader.load(url);
            Scene mainWindowScene = new Scene(mainWindowParent);
            stage2.setScene(mainWindowScene);
            stage2.show();
        }
        catch (IOException e){
            System.err.println("Error reading mainWindow.fxml" + e.getMessage());
        }
    }


    /**
     * Updates the user's personal data from profile tab
     * @param event of the mouse clicked on "Save"
     * @author Onni
     */
    
    public void saveU(MouseEvent event){
        try{  
            String name = profileName.getText();
            String stuNum = profileStudentNum.getText();
            String startYear = profileStartYear.getText();
            String endYear = profileEndYear.getText();
            if(validateFieldChanges(stuNum,name,startYear,endYear)){
                Student s = UserData.findCurrentStudent();
                s.setName(name);
                s.setStudentNumber(stuNum);
                s.setStartYear(Integer.parseInt(startYear));
                s.setEndYear(Integer.parseInt(endYear));
                UserData.setCurrent(UserData.findCurrentStudent());
                UserData.writeToJson();
                profileErrorLabel.setText("");
                profileSuccessLabel.setText("Changes have been saved");
            }
        }
        catch (IOException e){
            System.err.println("Save error"+ e.getMessage());
        }
    }


    /**
     * Deletes the current user profile after confirmation.
     * @param event of the mouse clicked on "Delete" in profile tab.
     * @author Rauli
     */

    public void deleteU(MouseEvent event){
        try{
            URL url = new File("src/main/resources/alert.fxml").toURI().toURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent parent = fxmlLoader.load();        
            AlertDialogController dialogController = fxmlLoader.<AlertDialogController>getController();
            dialogController.setAlertText("Are you sure you want to delete this profile? \n"+
                                           UserData.getCurrent().getName()+ "  " + 
                                           UserData.getCurrent().getStudentNumber());         
            
            Scene scene = new Scene(parent);
            dialogController.asking(1, event);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        }
        
        catch (IOException e){
            System.err.println("Error reading alert.fxml" + e.getMessage());  
        };
    
    }


    /**
     * Logs out the user, saves infromation and returns to SignUp -page.
     * @param event of the mouse clicked on "Log Out" in top bar.
     * @author Rauli
     */

    public void logOutUser(MouseEvent event){
        saveU(event);
        try{
            URL url = new File("src/main/resources/signUp.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();       
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException e){
            System.err.println("Error reading signUp.fxml" + e.getMessage());  

        }
    }

    /**
     * Quits the application after saving the data of the user.
     * @param event of the mouse clicked on "Quit" in top bar.
     * @author Rauli
     */

    public void quitProgram(MouseEvent event){
        saveU(event);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Builds the line chart in my profile tab
     * @param values
     * @author Rauli
     */

    private void lineChartBuild(double[] values){
        lineChart.getData().clear();
        int studyTimeYears = UserData.getCurrent().getEndYear() - UserData.getCurrent().getStartYear();
        int studyTimePeriods = 4*studyTimeYears;
        double plannedCreditPeriod = UserData.getCurrent().getDegree().getCredits()/studyTimePeriods;
            
        double futureRequired = values[0];
        lineChart.setTitle("Study progress");
        int periodNow = values.length-1;

        XYChart.Series<Integer, Double> completed = new XYChart.Series<Integer, Double>();
        completed.setName("Completed");
        XYChart.Series<Integer, Double> planned = new XYChart.Series<Integer, Double>();
        planned.setName("Planned");
        XYChart.Series<Integer, Double> needed = new XYChart.Series<Integer, Double>();
        needed.setName("Needed from now on");
        for (int period = periodNow; period < studyTimePeriods; period ++){
            needed.getData().add(new XYChart.Data<Integer, Double>(period, futureRequired));
        }

        for (int period = 1; period < values.length; period ++){
            completed.getData().add(new XYChart.Data<Integer, Double>(period, values[period]));
            planned.getData().add(new XYChart.Data<Integer, Double>(period, plannedCreditPeriod));
        }
      
        lineChart.getData().add(completed);
        lineChart.getData().add(planned);
        lineChart.getData().add(needed);
    }

    private void changeMyPie(int done, int total){
        pcData.get(0).setPieValue(done);
        pcData.get(1).setPieValue(total-done);
        pcData.get(0).getNode().setStyle("-fx-pie-color: purple");
        pcData.get(1).getNode().setStyle("-fx-pie-color: whitesmoke");
    }

    private void myPie(){   
        pcData = FXCollections.observableArrayList();
        PieChart.Data compl = new PieChart.Data("Done", 0);
        PieChart.Data todo = new PieChart.Data("ToDo", 180); 
        pcData.add(compl);
        pcData.add(todo);
        chart.setData(pcData);
        chart.setLegendVisible(false);
        chart.setLabelLineLength(1);
    }


    /**
     * Handles the clicks in degree -tree in the degree tab of 
     * the main window.
     * @param event of the mouse clicked on list of Degree window.
     * @author Onni, Rauli
     */

    public void handleItemClick(MouseEvent event){
        WebEngine myEngine = web_view.getEngine();
        myEngine.loadContent("");        

        degreeProgammeErrorLabel.setText("");
        DegreeProgramme selectedItem = degreelist.getSelectionModel().getSelectedItem();
        try {
            selectedItem = ParseData.fillDegreeData(selectedItem);
        }
        catch (Exception e){};

        String infoText = "Name: " + selectedItem.getName() + "\n" + "Credits: " + selectedItem.getCredits()
                + "\n" + "Code: " + selectedItem.getCode();
        degree_info.setText(infoText);
        
            if (selectedItem.getDescription() == null){
                try{
                    File john = new File("src/main/resources/pulp-fiction-john-travolta.gif");
                    myEngine.loadContent("<img src=\""+john.toURI()+"\"/>");  
                }
                catch (Exception e){
                    myEngine.loadContent("No information available");//tiukkapipoisille
                }            
            } 
        
  
        else {          
            myEngine.loadContent(selectedItem.getDescription());   
        }     
        chosenProgram = selectedItem;
    }

    private void recurseTree(TreeItem<SisuItem> item, boolean expanded){
        for (var item2 : item.getChildren()){
            item2.setExpanded(expanded);
            recurseTree(item2, expanded);
        }
    }

    /**
     * Expands the whole TreeView of the Degree structure tab.
     * @param event of the mouse clicked on "expand" -button"on the Study Structure.
     * @author Rauli
     */

    public void expandAll(MouseEvent event){
        var root = treeView.getRoot();
        root.setExpanded(true);
        recurseTree(root, true);
        expand_all.setVisible(false);
        collapse_all.setVisible(true);
    }

    /**
     * Collapses the whole TreeView of the Degree in study structure tab.
     * @param event of the mouse clicked on "collapse" -button"on the Study Structure.
     * @author Rauli
     */

    public void collapseAll(MouseEvent event){
        var root = treeView.getRoot();
        recurseTree(root, false);
        root.setExpanded(false);
        expand_all.setVisible(true);
        collapse_all.setVisible(false);
    }


    /**
     * The largest single operation method of the class handling the Sturcture of Studies.
     * Builds the Treeview of the <br> <strong>Structure of studies</strong>:
     * <ul>
     * <li>Recurses the Module structure of SisuItems</li>
     * <li>Adds courses to the tree checkin for duplicates</li>
     * <li>Sorts the TreeItem  courses by course, modules by name</li>
     * <li>Indicates the selection with awesome check -mark</li>
     * </ul>
     * <br> Creates a WebView and provides information of the course.
     * <br> Handles the selection and information of <br><strong>Attainments</strong> of the chosen Course
     * <ul>
     * <li>Manages the Completed -checkbox according to the given/loaded information</li>
     * <li>Manages the Grade -choicebox accordin to the given/loaded information if the Course is graded</li>
     * <li>Manages the date of completin, restricting the selection from Autum of Start Year to now</li>
     * </ul>
     * @param event of the mouse clicked on the Module/Course/Attainment pane
     * @author Rauli
     */

    public void handleTreeItemClick(MouseEvent event){
     
        WebEngine myEngine = tree_description.getEngine();
        myEngine.loadContent("");
        TreeItem<SisuItem> selectedTItem = treeView.getSelectionModel().getSelectedItem();
        attainment_pane.setVisible(false);
        if (selectedTItem == null){
            selectedTItem = treeView.getRoot();
        } else {
            SisuItem selectedItem = selectedTItem.getValue();
            boolean isFreshModule = false;
            if (selectedItem.getCourses().isEmpty()){
                isFreshModule = true;
                System.out.println("TreeItemillä ei ole lapsia");
            }

            //Tarkistetaan, onko oksalle jo lisätty kursseja duplikaattien 
            //välttämiseksi.
            boolean hasCourses = false;
            for (var child : selectedTItem.getChildren()){
                if (child.getValue().isCourse()){
                    hasCourses = true;
                }
            }

            if (!hasCourses){
                for (URL url : selectedItem.getCourseURLs()){
                               
                    Course newCourse = ParseData.createCourse(url);
                    if (isFreshModule){
                        selectedItem.addCourse(newCourse);
                        System.out.println("Tämä kurssi on uusi");
                    } 
                    else {
                        System.out.println("Tämä kurssi oli jo moduulissa \n puuhun pistin silti");
                    }

                    TreeItem<SisuItem> CTItem;
                    if (UserData.getCurrent().isCourseCompleted(newCourse)){
                        ImageView nView = new ImageView(image);
                        nView.setFitHeight(16);
                        nView.setFitWidth(16);
                        CTItem = new TreeItem<SisuItem>(newCourse, nView);
                    }
                    else{
                        CTItem = new TreeItem<SisuItem>(newCourse);

                    }
                    selectedTItem.getChildren().add(CTItem);
                    selectedTItem.setExpanded(true);   
                }       
            } 
            else {
                System.out.println("Puussa oli jo kursseja, enkä täyttänyt");
            }            
           TreeItem<SisuItem> spareItem = new TreeItem<SisuItem>();
            spareItem.getChildren().addAll(selectedTItem.getChildren().sorted((l,r) -> l.getValue().compareTo(r.getValue())));
            selectedTItem.getChildren().clear();
            selectedTItem.getChildren().addAll(spareItem.getChildren());
               
            if (selectedItem.getDescription() == null){

                try{
                    //URL url = getClass().getResource("src/main/resources/pulp-fiction-john-travolta.gif");
                    //_ImageView imgView = new ImageView();
                    //mgView.setImage(new Image(this.getClass().getResource("src/main/resources/pulp-fiction-john-travolta.gif").toExternalForm()));
                    //myEngine.g
                    final File john = new File("src/main/resources/pulp-fiction-john-travolta.gif");
                    //static var john2 = john.toURI();
                    myEngine.loadContent("<img src=\""+john.toURI()+"\"/>");  
                }
                catch (Exception e){
                    myEngine.loadContent("No information available");//tiukkapipoisille
                } 
            } 
            else {
                myEngine.loadContent(selectedItem.getDescription());
            }
            String infoText = "";
            if (selectedItem.getName() != null){
                infoText = "Name: " + selectedItem.getName() + "\n";
            }

            if (selectedItem.isCourse()){
                Course tempCourse = (Course) selectedItem;
                infoText = infoText + "Credits: " + tempCourse.getCredits()
                           + "\n" + "Code: " + tempCourse.getCode();
                attainment_pane.setVisible(true);

                //Building a date string of the startingpoint of studies
                String startYear = Integer.toString(UserData.getCurrent().getStartYear());
                String strStartDate = startYear + "-09-01";
                LocalDate startDate = LocalDate.parse(strStartDate);
                
                //setting calendar invisible before studies and after this moment
                final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(startDate) || (item.isAfter(LocalDate.now()))) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #EEEEEE;");
                                }
                            }
                        };
                    }
                };

                date_picker.setDayCellFactory(dayCellFactory);

                //Koskee kaikkia kursseja, arvosanalla tai ilman
                if (UserData.getCurrent().isCourseCompleted(tempCourse)){
                    check_box.setSelected(true);
                    date_picker.setValue(LocalDate.parse(UserData.getCurrent().getDate(tempCourse)));                
                }
                else{
                    check_box.setSelected(false);
                    date_picker.getEditor().clear();
                    date_picker.setValue(null);
                }

                //tästä alkaa arvioitujen kurssien marssi
                choice_box.getItems().clear();
                if (tempCourse.showsGrades()){
                    choice_box.setVisible(true);
                    choice_box_text.setVisible(true);
                    if (UserData.getCurrent().isCourseCompleted(tempCourse)){

                        choice_box.setValue(UserData.getCurrent().getGrade(tempCourse));
                    }
                    else{
                        choice_box.setValue(null);
                    }

                    for (int grade = 1; grade < 6; grade ++){
                        choice_box.getItems().add(grade);
                    }

                } else {
                    choice_box.setVisible(false);
                    choice_box_text.setVisible(false);
                }               
            }
            else if (selectedItem.isStudy()){
                    StudyModule temp = (StudyModule) selectedItem;
                    infoText = infoText + "Credits: " + temp.getCredits()
                        + "\n" + "Code: " + temp.getCode(); 
            }            
            else if (selectedItem.isDegree()){
                    DegreeProgramme temp = (DegreeProgramme) selectedItem;
                    infoText = infoText + "Credits: " + temp.getCredits()
                        + "\n" + "Code: " + temp.getCode(); 
            }       
        tree_info.setText(infoText);
        }   
    }

    /**
     * Saves the Attainment data.
     * Gives feedback with disappearing, colored text of selection
     * @param event of the mouse clicked on "Save" on the Attainment pane
     * @author Rauli
     */
    public void saveAttainment(MouseEvent event){

        LocalDate completionDate = date_picker.getValue();
        Object gradeObject = choice_box.getValue();

        int grade = -1;
        if (!choice_box.isVisible()){
            grade = 0;
        }
        else {
            if (gradeObject != null){
                grade = (int) gradeObject; // 0 = hyv
            }
        }

        boolean completed = check_box.isSelected();
   
        //pitäisi aina olla kurssi, koska muissa ei näytetä
        TreeItem<SisuItem> selectedTItem =  treeView.getSelectionModel().getSelectedItem();
        SisuItem selectedItem = selectedTItem.getValue();
        Course selectedCourse = (Course) selectedItem;

        System.out.println("Name: " + selectedCourse.getName() + "\n" +
                            "Grade: " + grade + "\n" +
                            "Date: " + completionDate + "\n" +
                            "is completed: " + completed);
        String result = UserData.getCurrent().updateAttainment(selectedCourse, grade, completionDate, completed);
       
        if (result.equals("Attainment removed.") ||
            result.equals("Attainment created.") ||
            result.equals("Attainment updated.")){
            attainment_error.setStyle("-fx-text-fill: green");
            if (completed){
                ImageView nView = new ImageView(image);
                nView.setFitHeight(16);
                nView.setFitWidth(16);
                selectedTItem.setGraphic(nView);
            }
            else {
                selectedTItem.setGraphic(null);
            }
        }
        else {
            attainment_error.setStyle("-fx-text-fill: red");
        }

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        attainment_error.setText(result);
        attainment_error.setVisible(true);
        delay.setOnFinished(eve -> attainment_error.setVisible(false));
        delay.play();
    }

    // Recursion to get the moduletree of selected degree.
    private TreeItem<SisuItem> getChdn(SisuItem module, TreeItem<SisuItem> node){

        //Magic
        TreeItem<SisuItem> newNode = new TreeItem<>();
        for (SisuItem rule : module.getRules()){
            newNode = new TreeItem<SisuItem>(rule);
            node.getChildren().add(getChdn(rule, newNode)); 
        }
        return node;         
    }

    /**
     * Landing method whenn entering the Structure of studies tab.
     * Clears the views, prepares information for the TreeView and
     * starts the recursion, collapses the tree hides the Attainment
     * pane and feendback text.
     * @throws IOException on saving User data or reading info of degrees.
     * @author Onni, Rauli
     */
    public void initDegreeProgramme() throws IOException {
        if (isLoadedDegree){
            chosenProgram = UserData.getCurrent().getDegree();  
            isLoadedDegree = false;       
        } 

        if (chosenProgram == null){
            degreeProgammeErrorLabel.setText("Please choose a degree programme");
        }
        
        chosenProgram = ParseData.createDegreeTree(chosenProgram);
        attainment_pane.setVisible(false);
        chosenProgram = ParseData.fillDegreeData(chosenProgram);
        UserData.getCurrent().setDegree(chosenProgram);
    
        boolean isNewTree;
        if (treeView.getRoot() == null){
            isNewTree = true;
        }
        else if (treeView.getRoot().getValue() != chosenProgram){ 
            isNewTree = true;
        }
        else {
            isNewTree = false;
        }

        if (isNewTree){
            treeView.setRoot(null);         
            UserData.writeToJson();
            TreeItem<SisuItem> node = new TreeItem<SisuItem>(chosenProgram);               
            treeView.setRoot(node);
            getChdn(chosenProgram, node);
            recurseTree(treeView.getRoot(), false);
            treeView.getRoot().setExpanded(false);
            expand_all.setVisible(true);
            collapse_all.setVisible(false);
        }
        web_view.getEngine().loadContent("");
        attainment_pane.setVisible(false);
        attainment_error.setVisible(false);
        tree_info.setText("");
        mainTabPane.getSelectionModel().select(structureTab);       
    }


    /**
     * Landing method when chosing the My Profile -tab.
     * Sets up the Profile edition possibility, creates Pie, LineChart, 
     * degreeinformation and average/total credits views.
     * @author Onni, Rauli
     */
    public void setMyProfile(){
        lineChartBuild(UserData.getCurrent().getAttainmentsPerPeriod());
        profileName.setText(UserData.getCurrent().getName());
        profileStudentNum.setText(UserData.getCurrent().getStudentNumber());
        profileStartYear.setText(Integer.toString(UserData.getCurrent().getStartYear()));
        profileEndYear.setText(Integer.toString(UserData.getCurrent().getEndYear()));
        int totalCredits = UserData.getCurrent().getDegree().getCredits();       
        int currentCredits = UserData.getCurrent().getCurrentCredits();
        String avgGrade = String.format("%.1f", UserData.getCurrent().getAverageGrade());
        int allTotalCredits = UserData.getCurrent().getAllCredits();
        String tdCredits = Integer.toString(currentCredits) + " / " + Integer.toString(totalCredits);
        String atCredits = Integer.toString(allTotalCredits)+ ")";

        if (avgGrade.equals("NaN")){
            avgGrade = "--";
        }

        degree_text.setText( UserData.getCurrent().getDegree().getName());
        total_done_credits.setText(tdCredits);
        total_all_credits.setText(atCredits);
        avg_grade.setText(avgGrade);
        PauseTransition delay = new PauseTransition(Duration.seconds(0.1));
        delay.setOnFinished(eve -> changeMyPie(currentCredits,totalCredits));
        delay.play();
    }

    /**
     * Checks if the information given to edit the profile is correct.
     * @param studentNum Student number
     * @param studentName Student Name
     * @param start Starting year of studies
     * @param end Ending year of studies
     * @return true if information is correct, false if incorrect.
     * @author Viljami, Onni
     */
    private boolean validateFieldChanges(String studentNum, String studentName, String start, String end){
        String checkNumResult = signUpController.validateStudentNumber(studentNum);
        if(!checkNumResult.equals("ok")){
            profileSuccessLabel.setText("");
            profileErrorLabel.setText(checkNumResult);
            return false;
        }
        if (studentName == null || studentName.isEmpty() || studentName.trim().isEmpty()){
            profileSuccessLabel.setText("");
            profileErrorLabel.setText("Name is null, empty or blank.");
            return false;
        }
        try{
            int startYear = Integer.parseInt(start);
            int endYear = Integer.parseInt(end);
            String checkYearsResult = signUpController.validateYears(startYear, endYear);
            if(!checkYearsResult.equals("ok")){
                profileSuccessLabel.setText("");
                profileErrorLabel.setText(checkYearsResult);
                return false;
            }
        }
        catch(NumberFormatException e){
            profileSuccessLabel.setText("");
            profileErrorLabel.setText("The years must be numbers.");
            return false;
        }
        return true;
    }
}
    
 
 
