package fi.tuni.prog3.sisu;

import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Viljami
 */
public class TestCourse {
    @Test
    public void testCourse() throws MalformedURLException{
        Course c = new Course();
        c.setCode("code-1");
        c.setShowGrades(true);
        c.setCredits(5);
        if(c.getCode().equals("code-1")){
            if(c.showsGrades()){
                if(c.getCredits() == 5){
                    Course c2 = new Course("code-1","name-1",1,"description-1",
                            new URL("http://www.example.com/docs/resource1.html"),false);
                    if(c2.getName().equals(c2.toString())){
                        assertTrue(c.compareTo(c2) == 0);
                    }
                }
            }
        }
    }
}
