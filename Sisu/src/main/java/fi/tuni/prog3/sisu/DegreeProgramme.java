package fi.tuni.prog3.sisu;

import java.net.URL;
import java.util.HashMap;

/**
 * Represents a DegreeProgramme.
 * @author Viljami
 */
public class DegreeProgramme extends Modules {

    private String code;
    private HashMap<URL,String> urlRuleList = new HashMap<>();
    private int credits;

     @Override
     public int compareTo(SisuItem other){
        DegreeProgramme x = (DegreeProgramme) other;
        return this.getName().compareTo(x.getName());
    }
    
    
    /**
     * Constructor for a DegreeProgramme.
     * @param id Id of the DegreeProgramme.
     */
    public DegreeProgramme(String id){
        this.setId(id);
    }
    
    /**
     * Empty constructor.
     */
    public DegreeProgramme() {
        
    }
    
      /**
     * Return the credits of the Module.
     * @return The credits of the Module.
     */
    public int getCredits(){
        return credits;
    }

       /**
     * Sets the credits of the Module.
     * @param credits The credits to be set.
     */
    public void setCredits(int credits){
        this.credits = credits;
    }
    

    /**
     * Returns the url-rule-list of the DegreeProgramme.
     * @return The url-rule-list of the DegreeProgramme.
     */
    public HashMap<URL,String> getUrlRuleList(){
        return urlRuleList;
    }
    
    /**
     * Sets the url-rule-list of the DegreeProgramme.
     * @param list The list to be set.
     */
    public void setUrlRuleList(HashMap<URL,String> list){
        urlRuleList = list;
    }

      /**
     * Sets the code of the Module.
     * @param code The code to be set.
     */
    public void setCode(String code){
        this.code = code;
    }

       /**
     * Returns the code of the Module.
     * @return The code of the Module.
     */
    public String getCode(){
        return code;
    }
  
}
