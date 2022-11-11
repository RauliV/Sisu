package fi.tuni.prog3.sisu;

import java.util.ArrayList;

/**
 * An abstract class which provides general methods and attributes for the
 * following classes:
 * <ul>
 * <li>DegreeProgramme</li>
 * <li>StudyModule</li>
 * <li>GroupingModule</li>
 * </ul>
 * @author Viljami
 */
public abstract class Modules extends SisuItem {

    private String id;
    private String groupId;
  
    @Override
    public abstract int compareTo(SisuItem sisuItem);

 
    
    /**
     * Returns the id of the Module.
     * @return The id of the Module.
     */
    public String getId(){
        return id;
    }
    
    
    /**
     * Returns the group id of the Module.
     * @return The group id of the Module.
     */
    public String getGroupId(){
        return groupId;
    }
      
    
    /**
     * Adds a rule to the Module's container.
     * @param rule The rule to be added.
     */
    public void addRule(Modules rule){
        rules.add(rule);
    }
    
    /**
     * Adds many rules to the Module's container.
     * @param ruleArr A list of rules to be added.
     */
    public void addRule(ArrayList<Modules> ruleArr){
        ruleArr.forEach(rule -> {rules.add(rule);});
    }
    
 
    
    /**
     * Sets the code of the Module.
     * @param id The code to be set.
     */
    public void setId(String id){
        this.id = id;
    }
        
    /**
     * Sets the group id of the Module.
     * @param groupId The group id to be set.
     */
    public void setGroupId(String groupId){
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        if ((this.getCourseURLs().size() != 0) && 
            (this.getCourses().size() == 0) &&
            (this.getRules().size() == 0)) {
                return ("\u25B6 " + this.getName());

        }
        else {
            return this.getName();

        }

        //return super.toString();
    }
            
}
