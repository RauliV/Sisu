package fi.tuni.prog3.sisu;

import java.net.URL;
import java.util.ArrayList;

/**
 * An abstract parent class for modules and courses
 */
public abstract class SisuItem implements Comparable<SisuItem>{

    private String name;
    private URL source;
    private String description;

    /**
     * List of rules in module
     */
    public ArrayList<Modules> rules = new ArrayList<>();

    /**
     * List of courses in module
     */
    public ArrayList<Course> courses = new ArrayList<>();

    /**
     * list of courseURLs, where to fing them.
     */
    public ArrayList<URL> courseURLs = new ArrayList<>();


    /**
     * Checks if SisuItem is a module
     * @return true if it is, false if not.
     */
    public boolean isModule(){
        return  ((this.isDegree()) ||
                (this.isGrouping()) ||
                (this.isStudy()));
    }

    /**
     * Checks if SisuItem is a Course
     * @return true if it is a course, false if not.
     */
    public boolean isCourse(){
        return this instanceof Course;
    }
   
    /**
     * Checks if Modules is a DegreeProgramme.
     * @return True if Modules is a DegreeProgramme.
     */
    public boolean isDegree(){
        return this instanceof DegreeProgramme;
    }
    
    /**
     * Checks if Modules is a StudyModule.
     * @return True if Modules is a StudyModule.
     */
    public boolean isStudy(){
        return this instanceof StudyModule;
    }
    
    /**
     * Checks if Modules is a GroupingModule.
     * @return True if Modules is a GroupingModule.
     */
    public boolean isGrouping(){
        return this instanceof GroupingModule;
    }


    @Override
    public abstract int compareTo(SisuItem sisuItem);



       /**
     * Adds a new Course to the Module's container.
     * @param course a Course to be added.
     */
    public void addCourse(Course course){
        courses.add(course);
    }

    /**
 * Returns the course URLs of the GroupingModule.
 * @return The list of the course URLs.
 */
    public ArrayList<URL> getCourseURLs(){
        return courseURLs;
    }

    /**
     * Adds a new URL in the list of URLs of courses
     * @param url where to find a Course
     */
    public void addCourseUrl(URL url){
        courseURLs.add(url);

    }
    
        /**
     * Returns a list of rules of the Module.
     * @return A list of rules of the Module.
     */
    public ArrayList<Modules> getRules(){
        return rules;
    }
    
    /**
     * Returns a list of courses of the Module.
     * @return A list of courses of the Module.
     */
    public ArrayList<Course> getCourses(){
        return courses;
    }

    /**
     * Returns the description of the Module.
     * @return The description of the Module.
     */
    public String getDescription(){
        return description;
    }

       
    /**
     * Sets the description of the Module.
     * @param description The description to be set.
     */
    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String toString(){
        return this.getName();
    }

       /**
     * Sets the name of the Module.
     * @param name The name to be set.
     */
    public void setName(String name){
        this.name = name;
    }

       /**
     * Sets the source url of the Module.
     * @param source The source url to be set.
     */
    public void setSource(URL source){
        this.source = source;
    }

     /**
     * Returns the source url of the Module.
     * @return The source url of the Module.
     */
    public URL getSource(){
        return this.source;
    }

   /**
     * Returns the name of the Module.
     * @return The name of the Module.
     */
    public String getName(){
        return this.name;
    }
}
