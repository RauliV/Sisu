package fi.tuni.prog3.sisu;

import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Viljami
 */
public class TestAttainment {
    @Test
    public void testAttainment() throws MalformedURLException{
        Course c = new Course("code-1","name-1",1,"description-1",
                            new URL("http://www.example.com/docs/resource1.html"),false);
        Attainment a = new Attainment(c, 5, "2022-2-22");
        if(a.getCourse().equals(c)){
            if(a.getGrade() == 5){
                if(a.getDate().equals("2022-2-22")){
                    Course c2 = new Course("code-2","name-1",1,"description-1",
                            new URL("http://www.example.com/docs/resource1.html"),false);
                    Attainment a2 = new Attainment(c2, 5, "2022-2-22");
                    a2.setGrade(1);
                    if(a2.getGrade() == 1){
                        a2.setDate("2022-2-21");
                        if(a2.getDate().equals("2022-2-21")){
                            assertTrue(a.compareTo(a2) != 0);
                        }
                    }
                    
                }
            }
        }
    }
}
