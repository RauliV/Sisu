package fi.tuni.prog3.sisu;

import java.net.URL;

/**
 * Defines a course. Students can take up courses and get attainments for
 * passing them.
 * @author Viljami
 */
public class Course extends SisuItem{//} implements Comparable<SisuItem> {
    private  String code;
    private int credits;
    private boolean showGrades;
    
    /**
     * Constructs a course with the given parameters.
     * @param code The course code.
     * @param name Name of the course.
     * @param credits The amount of credits of the course.
     * @param description Description of the course.
     * @param source Source URL of the course.
     * @param showGrades Boolean value if the course is graded or not.
     */
    public Course(String code, String name, int credits, String description,
            URL source, boolean showGrades){
        this.code = code;
        this.credits = credits;
        this.showGrades = showGrades;
        this.setName(name);
        this.setDescription(description);
        this.setSource(source);
    }

    /**
     * Empty constructor.
     */
    public Course(){
    
    }
    
    /**
     * Courses can be compared with SisuItems directly.
     * @param other SisuItem that the course is compared to.
     * @return <ul>
     *          <li>0 if the courses are equal.</li>
     *          <li>Less than 0 if the course's course code is lexicographically less than the other's.</li>
     *          <li>Greater than 0 if the course's course code is lexicographically greater than the other's (more characters).</li>
     *         </ul>
     */
    @Override
    public int compareTo(SisuItem other){
        if (other.isCourse()){
            Course x = (Course) other;
            return this.getCode().compareTo(x.getCode());
        }
        else {
            return 0;
        }
    }

    /**
     * Returns the course code.
     * @return The course code.
     */
    public String getCode(){
        return code;
    }

    /**
     * Sets the course code.
     * @param code The course code to be set.
     */
    public void setCode(String code){
        this.code = code;
    }
    
    /**
     * Returns the boolean value if a course shows grades or not.
     * @return The boolean value if a course shows grades or not.
     */
    public boolean showsGrades(){
        return showGrades;
    }
  
    /**
     * Sets the boolean value if a course shows grades or not.
     * @param showGrades The boolean value to be set.
     */
    public void setShowGrades(boolean showGrades ){
        this.showGrades = showGrades;

    }
    /**
     * Returns the amount of credits of the course.
     * @return The amount of credits of the course.
     */
    public int getCredits(){
        return credits;
    }

    /**
     * Sets the amount of credits of the course.
     * @param credits The amount of credits of the course.
     */
    public void setCredits(int credits){
        this.credits = credits;
    }
    
    /**
     * Returns the course in string form (name).
     * @return The course in string form.
     */
    @Override  
    public String toString() {
    
            return this.getName();
              
    } 
    

}
