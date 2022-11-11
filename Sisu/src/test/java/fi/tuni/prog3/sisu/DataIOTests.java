package fi.tuni.prog3.sisu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DataIOTests{


    @Test
    public void URLTest(){

        assertThrows(MalformedURLException.class, () ->
        {ParseData.parseModulesFrom(new URL("Not really an URL"));});
    }

    @Test
    public void buildDegreeTest() throws IOException{
        ParseData.buildDegreeList();
        assertTrue(ParseData.getDegrees().size() > 0);
    }

    @Test 
    public void createCourseTest() throws MalformedURLException{
        URL courseUrl = new URL("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId=uta-ykoodi-47926&universityId=tuni-university-root-id");
        Course testCourse = ParseData.createCourse(courseUrl);
        assertTrue(testCourse.getCode().equals("MATH.MA.110"));
        assertTrue(testCourse.getRules().size() == 0);
        assertTrue(testCourse.getCredits() == 5);
        assertTrue(testCourse.getSource().equals(courseUrl));
    }
}