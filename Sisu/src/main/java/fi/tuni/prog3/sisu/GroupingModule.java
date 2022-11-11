package fi.tuni.prog3.sisu;

/**
 * Represents a GroupingModule.
 * @author Viljami
 */
public class GroupingModule extends Modules {
    
    /**
     * Empty constructor.
     */
    public GroupingModule(){
        
    }
    @Override
    public int compareTo(SisuItem other){
        if (other.isGrouping()){
            GroupingModule x = (GroupingModule) other;
            return this.getName().compareTo(x.getName());
        }
        else {
            return 0;
        }

    }
    
}
