package fi.tuni.prog3.sisu;

/**
 * Represents a StudyModule.
 * @author Viljami
 */
public class StudyModule extends Modules {

    private String code;
    private int credits;

    //@Override
    /*int compareTo(StudyModule other){
        return code.compareTo(other.code);
    }*/
    /**
     * Empty constructor.
     */
    public StudyModule(){
        
    }
    @Override
    public int compareTo(SisuItem other){
        if (other.isStudy()){
            StudyModule x = (StudyModule) other;
            return this.getName().compareTo(x.getName());
        }
        else {
            return 0;
        }
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
