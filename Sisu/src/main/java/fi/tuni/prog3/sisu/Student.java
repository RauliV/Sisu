package fi.tuni.prog3.sisu;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a student. Students can take up courses and get attainments for
 * passing them.
 * @author Viljami
 */
public class Student implements Comparable<Student>{
    private String studentNumber;
    private String name;
    private int startYear;
    private int endYear;
    private URL degreeSource;
    private DegreeProgramme degree = new DegreeProgramme();
    private List<Attainment> attainments = new ArrayList<>();
    private boolean courseFound = false;
    private Course checkCourse;

    /**
     * Find an attainment of the student with a course.
     * @param course The course of the attainment searched.
     * @return The attainment of the course. If the attainment is not found,
     * returns null instead.
     */
    private Attainment findAttainment(Course course){
        for(var att : attainments){
            if(att.getCourse().compareTo(course) == 0){
                return att;
            }
        }
        return null;
    }
    
    /**
     * Finds all the courses in the module and its children recursively.
     * @param module The module to be searched for courses.
     */
    private void findAllCoursesUnderModule (Modules module){
        for(URL cUrl : module.getCourseURLs()){
            if(cUrl.equals(checkCourse.getSource())){
                courseFound = true;
            }
        }
        for(Modules rule : module.getRules()){
            if(!courseFound){
                this.findAllCoursesUnderModule(rule);
            }
        }
    }
       
    /**
     * Calculates the credits attained per period in the given year in the student's current degree.
     * @param year The year in question.
     * @return An array of either 2 or 4 integers representing the credits
     * attained per period in the given year in the student's current degree.
     */
    private int[] getYearAttainments(int year){
        String[] ymd;
        int y;
        int m;
        int p;
        if(year == startYear || year == endYear)
        {
            int[] result = new int[2];
            for(var att : attainments){
                checkCourse = att.getCourse();
                findAllCoursesUnderModule(this.getDegree());
                if(courseFound){
                    ymd = att.getDate().split("-");
                    y = Integer.parseInt(ymd[0]);
                    if(y == year){
                        m = Integer.parseInt(ymd[1]);
                        if(year == startYear){
                            p = getPeriod(m)-3;
                        }
                        else{
                            p = getPeriod(m)-1;
                        }
                        result[p] += att.getCourse().getCredits();
                    }
                    courseFound = false;
                }
            }
            return result;
        }
        else{
            int[] result = new int[4];
            for(var att : attainments){
                checkCourse = att.getCourse();
                findAllCoursesUnderModule(this.getDegree());
                if(courseFound){
                    ymd = att.getDate().split("-");
                    y = Integer.parseInt(ymd[0]);
                    if(y == year){
                        m = Integer.parseInt(ymd[1]);
                        p = getPeriod(m)-1;
                        result[p] += att.getCourse().getCredits();
                    }
                    courseFound = false;
                }
            }
            return result;
        }     
    }
    
    /**
     * Returns which period it is during the given month.
     * @param months The month in question.
     * @return 1 or 2 in Spring, 3 or 4 in Autumn.
     */
    private int getPeriod(int months){
        if(months < 4){
            return 1;
        }
        else if(months < 7){
            return 2;
        }
        else if(months < 10){
            return 3;
        }
        else{
            return 4;
        }
    }
        
    /**
     * Constructs a student with the given parameters.
     * @param studentNumber The student number of the student.
     * @param name The name of the student.
     * @param startYear The year the student started their studies.
     * @param endYear The estimated graduation year of the student.
     */
    public Student(String studentNumber, String name, int startYear,
            int endYear){
        this.studentNumber = studentNumber;
        this.name = name;
        this.startYear = startYear;
        this.endYear = endYear;
    }
  
    /**
     * Returns the student number of the student.
     * @return The student number of the student.
     */
    public String getStudentNumber(){
        return studentNumber;
    }
    
    /**
     * Returns the name of the student.
     * @return The name of the student.
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns the year the student started their studies.
     * @return The year the student started their studies.
     */
    public int getStartYear(){
        return startYear;
    }
    
    /**
     * Returns the estimated graduation year of the student.
     * @return The estimated graduation year of the student.
     */
    public int getEndYear(){
        return endYear;
    }
    
    /**
     * Return the source URL of the degree chosen by the student.
     * @return The source URL of the degree chosen by the student.
     */
    public URL getDegreeSource(){
        return degreeSource;
    }
    
    /**
     * Returns the degree of the student.
     * @return The degree of the student.
     */
    public DegreeProgramme getDegree(){
        return degree;
    }
    
    /**
     * Sets the student number of the student.
     * @param studentNumber The student number to be set.
     */
    public void setStudentNumber(String studentNumber){
        this.studentNumber = studentNumber;
    }
    
    /**
     * Sets the name of the student.
     * @param name The name to be set.
     */
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * Sets the year the student started their studies.
     * @param startYear The start year to be set.
     */
    public void setStartYear(int startYear){
        this.startYear = startYear;
    }
    
    /**
     * Sets the estimated graduation year of the student.
     * @param endYear The estimated graduation year to be set.
     */
    public void setEndYear(int endYear){
        this.endYear = endYear;
    }
     
    /**
     * Sets the source URL of the degree chosen by the student.
     * @param degreeSource The source URL of the degree to be set.
     */
    public void setDegreeSource(URL degreeSource){
        this.degreeSource = degreeSource;
    }
    
    /**
     * Sets the degree of the student.
     * @param degree The degree to be set.
     */
    public void setDegree(DegreeProgramme degree) {
        this.degree = degree;
    }
    
    /**
     * Returns the attainments of the student.
     * @return The attainments of the student.
     */
    public List<Attainment> getAttainments(){
        return attainments;
    }
    
    /**
     * Get a date from an attainment of a course.
     * @param course The course of the attainment.
     * @return The date of the attainment of the course. If the attainment
     * of the course isn't found, returns null.
     */
    public String getDate(Course course){
        Attainment att = findAttainment(course);
        if(att != null){
            return att.getDate();
        }
        return null;
    }
    
    /**
     * Get a grade from an attainment of a course.
     * @param course The course of the attainment.
     * @return The grade of the attainment of the course. If the attainment
     * of the course isn't found, returns -1.
     */
    public int getGrade(Course course){
        Attainment att = findAttainment(course);
        if(att != null){
            return att.getGrade();
        }
        return -1;
    }
    
    /**
     * Adds a new attainment for the student.
     * @param attainment The attainment to be added.
     */
    public void addAttainment(Attainment attainment){
        attainments.add(attainment);
    }
    
    /**
     * Removes an attainment from the student.
     * @param attainment The attainment to be removed.
     */
    public void removeAttainment(Attainment attainment){
        attainments.remove(attainment);
    }
    
    /**
     * Checks if the student has completed the given course.
     * @param course The course to be checked for completion.
     * @return True if the student has completed the course, false if not.
     */
    public boolean isCourseCompleted(Course course){
        for(var att : attainments){
            if( att.getCourse().compareTo(course) == 0){
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the amount of credits attained in the selected DegreeProgramme of the student.
     * @return The amount of credits attained in the selected DegreeProgramme of the student.
     */
    public int getCurrentCredits(){
        int result = 0;
        for(var att : this.getAttainments()){
            checkCourse = att.getCourse();
            findAllCoursesUnderModule(this.getDegree());             
            if(courseFound){
                result += att.getCourse().getCredits();
                courseFound = false;
            }
        }
        return result;
    }

    /**
     * Sums up all the credits the student has attained.
     * @return The total sum of credits the student has attained.
     */
    public int getAllCredits(){
        int result = 0;
        for(var att: attainments){
            result += att.getCourse().getCredits();
        }
        return result;
    }
    
    /**
     * Calculates the student's average grade in the selected DegreeProgramme.
     * @return The student's average grade in the selected DegreeProgramme.
     */
    public double getAverageGrade(){
        double totalGrade = 0;
        double courseAmount = 0;
        for(var att : this.getAttainments()){
            checkCourse = att.getCourse();
            findAllCoursesUnderModule(this.getDegree());             
            if(courseFound){
                if(att.getGrade() != 0){
                    totalGrade += att.getGrade();      
                    courseAmount++;
                }
                courseFound = false;
            }
        }
        return totalGrade/courseAmount;
    }

    /**
     * Returns an array of the student's attainments per period in their current
     * degree since they started studying. The first element of the returned 
     * array is the credits per period that the student must meet in order to
     * graduate in time.
     * @return The student's attainments per period in their current
     * degree since they started studying + the credits per period that the
     * student must meet in order to graduate in time (first element).
     */
    public double[] getAttainmentsPerPeriod(){
        ArrayList<Integer> creditsPerPeriod = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yyyy");  
        String[] now = dtf.format(LocalDateTime.now()).split("/");
        int currentYear = Integer.parseInt(now[1]);
        int currentMonths = Integer.parseInt(now[0]);
        int studentCredits = 0;
        for(int y = startYear ; y <= currentYear ; y++){
            for(var credits : getYearAttainments(y)){
                creditsPerPeriod.add(credits);
                studentCredits += credits;
            }
        }
        if(currentYear == endYear){
            if(getPeriod(currentMonths) == 1){
                creditsPerPeriod.remove(creditsPerPeriod.size()-1);
            }
        }
        else if(currentYear == startYear){
            if(getPeriod(currentMonths) == 3){
                creditsPerPeriod.remove(creditsPerPeriod.size()-1);
            }
        }
        else{
            int itemsToRemove = 4-getPeriod(currentMonths);
            while(itemsToRemove > 0){
                creditsPerPeriod.remove(creditsPerPeriod.size()-1);
                itemsToRemove--;
            }
        }
        int periodsLeft = (endYear - startYear)*4 - creditsPerPeriod.size();
        int creditsLeft = degree.getCredits() - studentCredits;
        double targetCredits;
        if(periodsLeft == 0){
            targetCredits = creditsLeft;
        }
        else{
            targetCredits = creditsLeft/periodsLeft;
        }
        double[] result = new double[creditsPerPeriod.size()+1];
        result[0] = targetCredits;
        for(int i = 1 ; i < result.length ; i++){
            result[i] = creditsPerPeriod.get(i-1);
        }
        return result;
    }
    
    /**
     * Handles changes to the student's attainments list. No changes are made if
     * any of the parameters aren't valid.
     * @param selectedCourse The selected course.
     * @param grade The grade of the selected course.
     * @param completionDate The date of the new attainment.
     * @param completed Boolean value if the course is marked completed or not.
     * @return String of "Attainment removed." or "Attainment updated."
     * or "Attainment created." if everything is ok.<br>
     * String of "Invalid grade." if the grade is invalid.<br>
     * String of "Date is null." if the date is null.<br>
     * String of "Invalid date." if the date is invalid.
     */
    public String updateAttainment(Course selectedCourse, int grade, 
            LocalDate completionDate, boolean completed){
        Attainment att = findAttainment(selectedCourse);
        if(!completed){
            if(att != null){
                attainments.remove(att);
                return "Attainment removed.";
            }
        }
        if(grade < 0 || grade > 5){
            return "Invalid grade.";
        }
        if(completionDate == null){
            return "Date is null.";
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.parse(dtf.format(LocalDateTime.now()));
        if(completionDate.isAfter(currentDate)){
            return "Invalid date.";
        }
        if(att != null){
            if(selectedCourse.showsGrades()){
                att.setGrade(grade);
            }
            else{
                att.setGrade(0);
            }
            att.setDate(dtf.format(completionDate));
            return "Attainment updated.";
        }
        attainments.add(new Attainment(selectedCourse, grade, dtf.format(completionDate)));
        return "Attainment created.";
    }
    
    /**
     * Return the student in string form (name studentNumber).
     * @return The student in string form.
     */
    @Override
    public String toString(){
        String s = this.name + " " + this.studentNumber;
        return s;
    }
    
    /**
     * Comparing students is done with student numbers.
     * @param other The student compared to.
     * @return <ul>
     *          <li>0 if the student numbers are equal.</li>
     *          <li>Less than 0 if the student number is lexicographically less than the other's.</li>
     *          <li>Greater than 0 if the student number is lexicographically greater than the other's (more characters).</li>
     *         </ul>
     */
    @Override
    public int compareTo(Student other){
        return studentNumber.compareTo(other.studentNumber);
    }
}
