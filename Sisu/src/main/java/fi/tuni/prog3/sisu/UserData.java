package fi.tuni.prog3.sisu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import javafx.fxml.LoadException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling user data.
 * @author Viljami
 */
public class UserData {
    private static List<Student> students = new ArrayList<>();
    private static Student current;

    /**
     * Clears all students from memory
     */

    public static void clear(){
        students = new ArrayList<>();
    }

    /**
     * Sets the currently logged in student.
     * @param student The student to be set.
     */
    public static void setCurrent(Student student){
        current = student;
    }

    /**
     * Returns the currently logged in student.
     * @return The currently logged in student.
     */
    public static Student getCurrent(){
        return current;
    }

    /**
     * Adds a student to the list.
     * @param student Student to be added.
     */
    public static void addStudent(Student student){
        students.add(student);
    }
    
    /**
     * Removes a student from the list.
     * @param studentNumber Student number referring to the student to be removed.
     */
    public static void removeStudent(String studentNumber){
        if(students.removeIf(student -> student.getStudentNumber().equals(studentNumber)))
        {
            System.out.println("Student removed.");
        }
        else{
            System.out.println("Student was not found.");
        }
    }
    
    /**
     * Checks if a student already exists.
     * @param studentNumber Student number of the student.
     * @return True if student was found, false if not.
     */
    public static boolean isStudentAlready(String studentNumber){
        return students.stream().anyMatch(student -> (student.getStudentNumber().equals(studentNumber)));
    }

    /**
     * Returns the current student from the list.
     * @return The current student from the list.
     */
    public static Student findCurrentStudent(){
        for(var student : students){
            if(current.compareTo(student) == 0){
                return student;
            }
        }
        return current;
    }
    /**
     * Returns the list of students.
     * @return The list of students.
     */
    public static List<Student> getStudents(){
        return students;
    }
    
    /**
     * Reads the <code>students.json</code> file and creates the list of
     * students based on its contents.<br>
     * If the file's not found, prints out <code>students.json file not found.</code>
     */
    public static void readFromJson(){
        try{
            //if file empty -> return
            BufferedReader br = new BufferedReader(new FileReader("students.json"));
            if(br.readLine() == null){
                return;
            }

            JsonArray studentFields = new JsonParser().parse(new FileReader("students.json")).getAsJsonArray();
            for( var studentField : studentFields){
                JsonObject studentObject = studentField.getAsJsonObject();
                String studentNumber = studentObject.get("studentNumber").getAsString();
                String studentName = studentObject.get("name").getAsString();
                int startYear = studentObject.get("startYear").getAsInt();
                int endYear = studentObject.get("endYear").getAsInt();
                Student student = new Student(studentNumber, studentName, startYear, endYear);
                JsonObject degree = studentObject.get("degree").getAsJsonObject();
                if (degree.has("source")){
                    URL source = new URL(degree.get("source").getAsString());
                    DegreeProgramme newDegree = new DegreeProgramme();
                    newDegree.setSource(source);
                    newDegree = ParseData.fillDegreeData(newDegree);
                    student.setDegree(newDegree);
                    //student.setDegreeSource(new URL(degree.get("source").getAsString()));
                }
                JsonArray attainmentFields = studentObject.get("attainments").getAsJsonArray();
                for(var attainmentField : attainmentFields){
                    JsonObject attainmentObject = attainmentField.getAsJsonObject();
                    JsonObject courseObject = attainmentObject.get("course").getAsJsonObject();
                    String code = courseObject.get("code").getAsString();
                    String courseName = courseObject.get("name").getAsString();
                    int credits = courseObject.get("credits").getAsInt();
                    String description;
                    if (courseObject.has("description")){
                        description = courseObject.get("description").getAsString();
                    }
                    else {
                        description = "";
                    }
                    URL source = new URL(courseObject.get("source").getAsString());
                    boolean showGrades = courseObject.get("showGrades").getAsBoolean();
                    Course course = new Course(code, courseName, credits, description, source, showGrades);
                    int grade = attainmentObject.get("grade").getAsInt();
                    String date = attainmentObject.get("date").getAsString();
                    student.addAttainment(new Attainment(course, grade, date));
                }
                students.add(student);
            }
        }
        catch(FileNotFoundException e){
            System.err.println("students.json file not found.");
        }
        catch(MalformedURLException e){
            System.err.println("A source URL of a course wasn't valid.");
        }
        catch(IOException e){
            System.err.println("An I/O error occurred.");
        }
        catch(JsonIOException e){
            System.err.println("A Json I/O error occurred.");

        }
        catch(JsonParseException e){
            System.err.println("A Json parse error occurred.");

        }
        catch(final Exception e){
            System.err.println("Unknown error occurred.");

        }
    }
    
    /**
     * Writes students to the <code>students.json</code> file.<br>
     * The file is saved into the <code>Sisu</code> folder.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeToJson() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = Files.newBufferedWriter(Path.of("students.json"))) {
            gson.toJson(students, writer);
        }
    }
}
