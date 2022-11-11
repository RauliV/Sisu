package fi.tuni.prog3.sisu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * A class to read data from Kori API, 
 * parse it, construct Modules and Courses
 * and fills them with information of wanted language
 * @author Rauli
 */
public class ParseData {

    private static String lang = "fi";
    private static HashMap<URL, Course> courseList = new HashMap<>();
    private static HashMap<URL, Modules> moduleList = new HashMap<>();
    private static ArrayList<DegreeProgramme> degreeListSaved = new ArrayList<>();
    private static ArrayList<DegreeProgramme> engDegreeListSaved = new ArrayList<>();
    
    /**
     * Method to get current list of degrees (en/fin).
     * @return list of degrees.
     */
    public static ArrayList<DegreeProgramme> getDegrees(){
        return degreeListSaved;
    }
    
    /** 
     *  Private method
     *  Täyttää tavanomaiset, toistuvat, tiedot moduuliin 
     * @param module 
     * @param root
     */
    private static void fillTheBasics(Modules module, JsonObject root){
      
        String name = checkLanguage(root.getAsJsonObject(), "name");
        String groupId = root.getAsJsonObject().get("groupId").getAsString();
        System.out.println(name);  
        module.setName(name);
        module.setGroupId(groupId);
    }

    
    /** 
     * @param ruleRoot Where to start gathering roles
     * @param ruleList of rules gathered.
     * @return HashMap<URL, String>
     */
    // Kerää erityyppiset rulet listaan rekursiivisesti. 
    // Modulerulen alla on aina moduleGroupId -viittaus lapsimoduuliin
    // CourseUnitRule viittaa kurssiin
    // Compositerulen kanssa samasta JsonObjektista löytyy rules -array
    // Creditrulen kanssa kokonaisuuden op -määrä (jos on)
    private static HashMap<URL, String> getRules(JsonElement ruleRoot, HashMap<URL, String> ruleList){
        try{

            String ruleType = ruleRoot.getAsJsonObject().get("type").getAsString();

            if (ruleType.equals("ModuleRule")){               
                String source = "https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId="
                + ruleRoot.getAsJsonObject().get("moduleGroupId").getAsString() + "&universityId=tuni-university-root-id";
                URL url = new URL(source);
                ruleList.put(url, "Module");
            }
            else if (ruleType.equals("CourseUnitRule")){
                String source = "https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId="
                + ruleRoot.getAsJsonObject().get("courseUnitGroupId").getAsString() + "&universityId=tuni-university-root-id";
                URL url = new URL(source);
                ruleList.put(url, "Course");
            }
            else if(ruleType.equals("CompositeRule")){
                JsonArray ruleArray = ruleRoot.getAsJsonObject().get("rules").getAsJsonArray();
                for (JsonElement thisRule : ruleArray){
                    getRules(thisRule, ruleList);
                }
            }
            else if (ruleType.equals("CreditsRule")){
                getRules(ruleRoot.getAsJsonObject().get("rule"), ruleList);
            }
            return ruleList;
        }
        catch (MalformedURLException e){
            System.err.println("Error loadin mainlist Json file" + e.getMessage());
        }
        return null;
    }

    /**
     * Fills the skeleton of a DegreeProgramme module with credits and description
     * @param degree DegreeProgramme module that is wanted to be filled
     * @return Filled DegreeProgramme module
     * @throws IOException if Json -file of the module is not fould
     */
    public static DegreeProgramme fillDegreeData(DegreeProgramme degree) throws IOException{
        URL url = degree.getSource();
        JsonObject root = readElement(url).getAsJsonObject();
        int credits = root.get("targetCredits").getAsJsonObject().get("min").getAsInt();
        degree.setCredits(credits);       
        String code = root.get("code").getAsString();
        degree.setCode(code);       
        fillTheBasics(degree, root);
        String description = checkLanguage(root, "learningOutcomes");
        degree.setDescription(description);
        return degree;
    }

    /**
     * Method to ask current language.
     * @return current language.
     */
    public static String getLanguage(){
        return lang;
    }

    /**
     * Resets the current degree database and replaces it with
     * the new of of wanted language, Finnish or English.
     * @param language to witch degree information data is to be changed.
     */

    public static void changeLanguage(String language){
        
        lang = language;
        try{
            UserData.writeToJson();
            ParseData.courseList = new HashMap<>();
            ParseData.moduleList = new HashMap<>();
            ParseData.buildDegreeList();
            DegreeProgramme userDegree = UserData.getCurrent().getDegree();
            userDegree.rules = new ArrayList<>();
            if (userDegree.getSource() != null){
                userDegree = ParseData.fillDegreeData(userDegree);
            }
            ParseData.buildDegreeList();               
            ArrayList<DegreeProgramme> dList = getDegrees();               
            ArrayList <DegreeProgramme> engDegrees = new ArrayList<>();
            if (lang == "en"){ 
                if (engDegreeListSaved.size() == 0) {      
                    for (int x = 0; x < dList.size();x ++){                           
                        DegreeProgramme newDegree = ParseData.fillDegreeData(dList.get(x));
                        engDegrees.add(newDegree);
                        degreeListSaved = engDegrees;   
                        engDegreeListSaved = engDegrees;
                    } 
                }
                else {
                    degreeListSaved = engDegreeListSaved;
                }                 
            }       
        }      
        catch (IOException e){
            System.err.println("Error reading degreelist" + e.getMessage());
        }  
    }
    
    /** 
     * Subroutine to read the file of Json data from Kori
     * @param url where data is read from
     * @return JsonElement of the root of the this file
     * @throws IOException if the is not found
     */
    private static JsonElement readElement (URL url) throws IOException{

        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET");
        http.connect();
        Scanner scan = new Scanner(url.openStream());
        String temp = scan.nextLine();
        scan.close();
        http.disconnect();
        JsonElement root =  JsonParser.parseString(temp);
        return root;
    }

    /**
     * Constructs DegreeProgramme objecta of all degrees from the Tuni database
     * of year 2021 and gathers them in a list.
     * @throws IOException if file not found
     */
    public static void buildDegreeList() throws IOException{
       
        ArrayList<DegreeProgramme> degreeList = new ArrayList<>();
        URL url = new URL("https://sis-tuni.funidata.fi/kori/api/module-search?curriculumPeriodId=uta-lvv-2021&universityId=tuni-university-root-id&moduleType=DegreeProgramme&limit=1000");
        JsonElement root = readElement(url);
        JsonArray  degrees = root.getAsJsonObject().get("searchResults").getAsJsonArray();
        
        for (JsonElement degree: degrees){
            String id = degree.getAsJsonObject().get("id").getAsString();
            DegreeProgramme newDegree = new DegreeProgramme(id);
            fillTheBasics(newDegree, degree.getAsJsonObject());
            String source = "https://sis-tuni.funidata.fi/kori/api/modules/" + id;
            URL url_deg = new URL(source);
            newDegree.setSource(url_deg);
            degreeList.add(newDegree);
        }
        degreeListSaved = degreeList; 
    }

    
    /** Parses values according to the default language.
     *  If language is not found, first available is used
     *  Also checks if atrribute exists at all or is null
     * @param root Where to find wanted attribute
     * @param attribute Wanted attribute from json
     * @return String Value of wanted attribute or null, if it is not found.
     */
   
    private static String checkLanguage(JsonObject root, String attribute){

        if (root.has(attribute)){
            if (root.getAsJsonObject().get(attribute).isJsonObject()){
                if (root.getAsJsonObject().get(attribute).getAsJsonObject().has(lang)){
                    attribute = root.getAsJsonObject().get(attribute).getAsJsonObject().get(lang).getAsString();
                }
                else {   //take first key if wanted not available
                    Set<String> keys = root.getAsJsonObject().get(attribute).getAsJsonObject().keySet();
                    attribute = root.getAsJsonObject().get(attribute).getAsJsonObject().get(keys.iterator().next()).getAsString();
                }
            }
            else {
                if (root.getAsJsonObject().get(attribute).isJsonNull()){
                    attribute = null;
                }
                else{
                    attribute = root.getAsJsonObject().get(attribute).getAsString();
                }
            }
            return attribute;
        }
        else{
            System.out.println("Not found " + attribute);
            return null;
        }
    }

    
    /** Creates Course object from url given as parameter if not already created
     * @param url Url of the course to be created
     * @return Course Created course
     */
    
    public static Course createCourse(URL url){
        try{
            // check if course already exists
            if (courseList.containsKey(url)){
                System.out.println(courseList.get(url).getName() + " oli jo");
                return courseList.get(url);
            }  
            JsonElement root = readElement(url);

            JsonArray courseArray = root.getAsJsonArray();
            root = courseArray.get(0).getAsJsonObject();

            String name = checkLanguage(root.getAsJsonObject(), "name");
            String code = "";
            if (root.getAsJsonObject().get("code").isJsonNull()){
                code = null;
            }
            else {
                code = root.getAsJsonObject().get("code").getAsString();
            }       
            int credits_max = 0;
            JsonElement x = root.getAsJsonObject().get("credits").getAsJsonObject().get("max");
             
            if (!x.isJsonNull()){
                credits_max = root.getAsJsonObject().get("credits").getAsJsonObject().get("max").getAsInt();
            }          
            String content = checkLanguage(root.getAsJsonObject(), "content");
            Course returnCourse = new Course();
            returnCourse.setCode(code);
            returnCourse.setCredits(credits_max);
            returnCourse.setDescription(content);
            returnCourse.setSource(url); 
            returnCourse.setName(name);

            if (root.getAsJsonObject().get("gradeScaleId") != null){
                System.out.println(root.getAsJsonObject().get("gradeScaleId").getAsString());
                if (root.getAsJsonObject().get("gradeScaleId").getAsString().equals("sis-hyl-hyv")){
                    returnCourse.setShowGrades(false);
                }
                else if (root.getAsJsonObject().get("gradeScaleId").getAsString().equals("sis-0-5")){
                    returnCourse.setShowGrades(true);
                }
            }
            courseList.put(url, returnCourse);
            System.out.println("        Lisätty kurssi: " + name + " (" + code + ")" 
                                + " " + credits_max + "op");
            return returnCourse;
        }

        catch (IOException e){
            System.err.println("Error reading coursedata" + e.getMessage());
        }
        return null;
    }

    /**
     * Fills DegreeProgramme module object recursively from child to child
     * @param degree object to be filled
     * @return completed tree of objects
     */
    public static DegreeProgramme createDegreeTree(DegreeProgramme degree){

        URL url = degree.getSource();
        try{
            
            JsonElement root = readElement(url);
            HashMap<URL, String> ruleList = new HashMap<>();
            getRules(root.getAsJsonObject().get("rule"), ruleList);
            degree.setUrlRuleList(ruleList);
                         
            for (Map.Entry<URL, String> entry : ruleList.entrySet()){
    
                if (entry.getValue() == "Course"){
                    //optimoidaan, eikä haeta kursseja vielä tässä kohtaa
                    //degree.addCourse(createCourse(entry.getKey()));
                }    
                else if (entry.getValue() == "Module"){

                    degree.addRule(parseModulesFrom(entry.getKey()));
                }
            }
            return degree;
        }
        catch(IOException e){
            System.err.println("Error building the degreetree" + e.getMessage());
        }
        return null; 
    }

    /**
     * Recursive method to build tree of Modules objects upwards from the url of wanted module
     * Adds also courses as leaves, last nodes of the tree
     * @param url of the module tree which needs to be filled from.
     * @return completed module tree from base module
     * @throws IOException if Json -file is not found
     */
    public static Modules parseModulesFrom (URL url) throws IOException{

        try{
            if (moduleList.containsKey(url)){
                System.out.println(moduleList.get(url).getName() + " on jo lisätty");
                return moduleList.get(url);
            }
            JsonElement root = readElement(url);

            if (root.isJsonArray() && (root.getAsJsonArray().size() == 1)){
                root = root.getAsJsonArray().get(0);
            }
            String type  = root.getAsJsonObject().get("type").getAsString();
            Modules returnModule = null;
    
            if (type.equals("GroupingModule")){
                GroupingModule thisModule = new GroupingModule();
                String name = checkLanguage(root.getAsJsonObject(), "name");
                thisModule.setName(name);
                String description = checkLanguage(root.getAsJsonObject().get("rule")
                                    .getAsJsonObject(), "description");
                thisModule.setDescription(description);
                returnModule = thisModule;
                String groupId = root.getAsJsonObject().get("groupId").getAsString();
                returnModule.setGroupId(groupId);
            }
            else if (type.equals("StudyModule")){ 
                StudyModule thisModule = new StudyModule();
                String code = root.getAsJsonObject().get("code").getAsString();
                thisModule.setCode(code);
                returnModule = thisModule;                         
                String description = checkLanguage(root.getAsJsonObject(), "contentDescription");
                
                if (description == null){
                    description = checkLanguage(root.getAsJsonObject(), "outcomes");
                }
                returnModule.setDescription(description);
                fillTheBasics(returnModule, root.getAsJsonObject());
            }

            returnModule.setSource(url);
            HashMap<URL, String> ruleList = new HashMap<>();
            getRules(root.getAsJsonObject().get("rule"), ruleList);

            for (Map.Entry<URL, String> entry : ruleList.entrySet()){
                if (entry.getValue() == "Course"){
                    returnModule.addCourseUrl(entry.getKey());    
                }    
                else{
                    returnModule.addRule(parseModulesFrom(entry.getKey()));
                }
            }
            moduleList.put(url, returnModule);
            return returnModule;
        }
        catch (IOException e){
            System.err.println("Error building degreetree" + e.getMessage());
        }
       return null;
    }

    /** 
     * @param args[]
     */
    public static void main(String args[]){

    }
}

