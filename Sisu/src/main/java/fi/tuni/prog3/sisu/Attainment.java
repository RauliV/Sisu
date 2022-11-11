package fi.tuni.prog3.sisu;

/**
 * Defines an attainment. Students get attainments for passing courses.
 * @author Viljami
 */
public class Attainment implements Comparable<Attainment> {
    private final Course course;
    private int grade;
    private String date;
    
    /**
     * Constructs an attainment with the given parameters.
     * @param course The attained course.
     * @param grade Grade given to the student.
     * @param date Date of the student passing the course.
     */
    public Attainment(Course course, int grade, String date){
        this.course = course;
        this.grade = grade;
        this.date = date;
    }

    /**
     * Returns the attained course.
     * @return The attained course.
     */
    public Course getCourse(){
        return course;
    }
    
    /**
     * Returns the grade given to the student.
     * @return The grade given to the student.
     */
    public int getGrade(){
        return grade;
    }
    
    /**
     * Returns the date of the student passing the course.
     * @return The date of the student passing the course.
     */
    public String getDate(){
        return date;
    }

    /**
     * Sets the grade of the attainment.
     * @param grade The grade to be set.
     */
    public void setGrade(int grade) {
        this.grade = grade;
    }

    /**
     * Sets the date of the attainment.
     * @param date The date to be set.
     */
    public void setDate(String date) {
        this.date = date;
    }
    
    /**
     * Comparing attainments is equivalent to comparing courses.
     * @param other The attainment to compare to.
     * @return <ul>
     *          <li>0 if the courses are equal.</li>
     *          <li>Less than 0 if the course's course code is lexicographically less than the other's.</li>
     *          <li>Greater than 0 if the course's course code is lexicographically greater than the other's (more characters).</li>
     *         </ul>
     */
    @Override
    public int compareTo(Attainment other){
        return course.compareTo(other.course);
    }
}
