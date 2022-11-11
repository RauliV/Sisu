package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Viljami
 */
public class TestStudent {
    @Test
    public void testStudentOnly(){
        Student s = new Student("student-1","name-1",2020,2025);
        if(s.getStudentNumber().equals("student-1")){
            if(s.getName().equals("name-1")){
                if(s.getStartYear() == 2020){
                    if(s.getEndYear() == 2025){
                        s.setStudentNumber("student-2");
                        s.setName("name-2");
                        s.setStartYear(2021);
                        s.setEndYear(2024);
                    }
                }
            }
        }
        if(s.toString().equals("name-2 student-2")){
            if(s.getStartYear() == 2021){
                if(s.getEndYear() == 2024){
                    Student s2 = new Student("student-2","name-1",2020,2025);
                    assertTrue(s.compareTo(s2) == 0);
                }
            }
        }
    }
    
    @Test
    public void testStudentWithAttainments() throws MalformedURLException{
        Course c = new Course("code-1","name-1",1,"description-1",
                            new URL("http://www.example.com/docs/resource1.html"),false);
        Attainment a = new Attainment(c, 5, "2022-02-22");
        Student s = new Student("student-1","name-1",2020,2025);
        if(s.getAttainments().isEmpty()){
            s.addAttainment(a);
            if(s.getGrade(c) == 5){
                if(s.getDate(c).equals("2022-02-22")){
                    if(s.getAttainments().size() == 1){
                        if(s.isCourseCompleted(c)){
                            if(s.getAllCredits() == 1){
                                s.removeAttainment(a);
                                assertTrue(s.getAttainments().isEmpty());
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Test
    public void testUpdateAttainment() throws MalformedURLException{
        Course c = new Course("code-1","name-1",1,"description-1",
                            new URL("http://www.example.com/docs/resource1.html"),false);
        Attainment a = new Attainment(c, 5, "2022-02-22");
        Student s = new Student("student-1","name-1",2020,2025);
        s.addAttainment(a);
        LocalDate ld = LocalDate.now();
        ld = ld.plusDays(1);
        if(s.updateAttainment(c,0,null,false).equals("Attainment removed.")){
            if(s.getAttainments().isEmpty()){
                s.addAttainment(a);
                if(s.updateAttainment(c, 6, ld, true).equals("Invalid grade.")){
                    if(s.updateAttainment(c, 1, null, true).equals("Date is null.")){
                        if(s.updateAttainment(c, 1, ld, true).equals("Invalid date.")){
                            ld.minusDays(1);
                            if(s.updateAttainment(c, 1, ld, true).equals("Attainment updated.")){
                                if(a.getGrade() == 0){
                                    Course c2 = new Course("code-2","name-1",1,"description-1",
                                    new URL("http://www.example.com/docs/resource1.html"),false);
                                    Attainment a2 = new Attainment(c2, 5, "2022-02-22");
                                    assertTrue(s.updateAttainment(c2, 1, ld, true).equals("Attainment created."));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Test
    public void testStudentWithDegree() throws MalformedURLException, IOException{
        Student s = new Student("student-1","name-1",2020,2025);
        s.setDegreeSource(new URL("https://sis-tuni.funidata.fi/kori/api/modules/otm-c85c4c2a-4563-48e6-9efe-d20ca506bd1e"));
        DegreeProgramme dp = new DegreeProgramme();
        dp.setSource(s.getDegreeSource());
        ParseData.createDegreeTree(dp);
        ParseData.fillDegreeData(dp);
        s.setDegree(dp);
        Course c = new Course("MATH.APP.210","name1",5,"desc1",
                new URL("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId\u003duta-ykoodi-47933\u0026universityId\u003dtuni-university-root-id"),true);
        Course c2 = new Course("FIL.FIA.004","name1",5,"desc1",
                new URL("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId\u003duta-ykoodi-40845\u0026universityId\u003dtuni-university-root-id"),true);
        Course c3 = new Course("HIS.100","name1",5,"desc1",
                new URL("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId\u003dotm-39cb0f5d-8ca2-4858-bacc-1fbd8caf0922\u0026universityId\u003dtuni-university-root-id"),true);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        Attainment a = new Attainment(c,1,dtf.format(LocalDate.now()));
        Attainment a2 = new Attainment(c2,3,dtf.format(LocalDate.now()));
        Attainment a3 = new Attainment(c3,5,dtf.format(LocalDate.now()));
        s.addAttainment(a);
        s.addAttainment(a2);
        s.addAttainment(a3);
        if(s.getCurrentCredits() == 15){
            if(s.getAverageGrade() == 3){
                assertTrue(s.getAttainmentsPerPeriod()[0] == Double.valueOf(165/12) && s.getAttainmentsPerPeriod().length == 9);
            }
        }
    }
    
}
