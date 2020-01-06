package ucd.creativecomputation.knowledgebase.nocobjects;

import java.util.*;

import static ucd.creativecomputation.alexa.NarratorStreamHandler.*;
import static ucd.creativecomputation.toolkit.StoryFiles.VALID_CHARACTERS;


/**
 * Class to represent the Knowledge Data needed for retrieving
 * matching stories for a audience-given attribute. This Object
 * makes use of Tony Veales Knowledge Bases and Knowledge Base Modul.
 *
 * This class still contains a lot of previous code, which is also functional
 * but only under different circumstances. Instead of deleting it, the author
 * decided to keep it, however as commented sections.
 * @author Thomas Mildner
 */
public class KnowledgeData{

    // Various class variables.
    private List<String> character = new ArrayList<>();

    private Hashtable noc_activities        = null;
    private Hashtable noc_origin            = null;
    private Hashtable noc_allQualities      = null;

    private Hashtable noc_attributes        = null;
    private Hashtable noc_advocation        = null;
    private Hashtable noc_allPeople         = null;

    private Hashtable affordances           = null;
    private Hashtable weapon_arsenal        = null;

    private Hashtable positive_counterparts = null;
    private Hashtable negative_counterparts = null;

    private Hashtable tc_for                = null;
    private Vector<String> noc_character    = null;
    private Vector<String> noc_weapons      = null;


    /**
     * Constructor to apply specific actions on the NOC List relevant for this Alexa Skill.
     *  takes the field to be used.
     *  takes the key to be used.
     */
    public KnowledgeData(){

        noc_activities          = NOC_LIST.getInvertedField("Typical Activity");
        noc_origin              = NOC_LIST.getInvertedField("Address 1");
        noc_origin              = NOC_LIST.getInvertedField("Address 2", noc_origin);
        noc_origin              = NOC_LIST.getInvertedField("Address 3", noc_origin);
        noc_origin              = NOC_LIST.getInvertedField("Fictional World", noc_origin);
        noc_allQualities        = NOC_LIST.getInvertedField("Negative Talking Points");
        noc_allQualities        = NOC_LIST.getInvertedField("Positive Talking Points", noc_allQualities);

        noc_attributes          = NOC_LIST.getInvertedField("Genres");
        noc_attributes          = NOC_LIST.getInvertedField("Domains", noc_attributes);
        noc_attributes          = NOC_LIST.getInvertedField("Category", noc_attributes);
        noc_attributes          = NOC_LIST.getInvertedField("Typical Activity", noc_attributes);
        noc_attributes          = NOC_LIST.getInvertedField("Negative Talking Points", noc_attributes);
        noc_attributes          = NOC_LIST.getInvertedField("Positive Talking Points", noc_attributes);

        noc_advocation          = NOC_LIST.getInvertedField("Genres");
        noc_advocation          = NOC_LIST.getInvertedField("Category", noc_advocation);
        noc_advocation          = NOC_LIST.getInvertedField("Domains", noc_advocation);
        noc_advocation          = NOC_LIST.getInvertedField("Typical Activity", noc_advocation);

        noc_allPeople           = NOC_LIST.getInvertedField("Character");
        tc_for                  = CAUSALITY_LIST.getInvertedField("For");
        noc_character           = NOC_LIST.getAllFrames();

        affordances             = AFFORDANCES_LIST.getInvertedField("Past Tense");
        weapon_arsenal          = WEAPON_ARSENAL_LIST.getInvertedField("Affordances");

        positive_counterparts   = COUNTERPARTS_LIST.getInvertedField("Positive Counterpart");
        negative_counterparts   = COUNTERPARTS_LIST.getInvertedField("Negative Counterpart");
    }

    /**
     * Method to add the values of a specific given key to the character list.
     * This method works with Hashtables and Strings as given attributed.
     * @param table
     *  The table to retrieve a set of keys from.
     * @param attribute
     *  The attribute to be searched for inside the set of keys.
     * @return
     *  returns true, if the characterList is not empty, otherwise false.
     */
    private boolean createKeyValues(Hashtable table, String attribute){
        Set<String> keys = table.keySet();
        List<String> chars;
        for(String key : keys){
            if(key.toLowerCase().contains(attribute.toLowerCase())){
                chars = (List<String>) table.get(key);
                if(!chars.isEmpty()){
                    character.addAll(chars);
                }
            }
        }

        return !character.isEmpty();
    }

    /**
     * Method to add the values of a specific given key to the character list.
     * This method works with Vectors and Strings as given attributed.
     * @param vector
     *  The vector to retrieve a set of keys from.
     * @param attribute
     *  The attribute to be searched for inside the set of keys.
     * @return
     *  returns true, if the characterList is not empty after the search, otherwise false.
     */
    private boolean createKeyValues(Vector<String> vector, String attribute){
        Set<String> keys = new HashSet<>(vector);
        for(String key : keys){
            if(key.toLowerCase().contains(attribute.toLowerCase())){
                character.add(key);
            }
        }
        return !character.isEmpty();
    }

    /**
     * Method to check wheather a given attribute is a character name.
     * @param entryAttribute
     *  given attribute.
     * @return
     *  returns true, if the given attribute is a character name, otherwise false.
     */
    private boolean isName(String entryAttribute){
        return createKeyValues(noc_character, entryAttribute);
        /*if(noc_character.contains(entryAttribute)){
            System.out.println("Matching characters are: " + noc_allPeople.get(entryAttribute));
            return true;
        }*/
    }

    /**
     * Method to find characters for actions. This method first looks
     * at the CAUSALITY_LIST for the action, and retrieves the activity
     * that matches a character on the NOC_LIST. This character will be
     * added to the list of characters.
     * @param entryAttribute
     *  attribute a character should represent.
     * @return
     *  returns true, if the characterList is not empty after the search, otherwise false.
     */
    private boolean findCharactersForActions(String entryAttribute) {
        Set<String> keys = tc_for.keySet();
        List<String> activities_1 = new ArrayList<>();
        List<String> chars_1 = new ArrayList<>();
        for(String key : keys){
            if(key.contains(entryAttribute.toLowerCase())){
                activities_1.add(key);
            }
        }
        Set<String> keys_2 = noc_activities.keySet();
        for(String act : activities_1){
            for(String key : keys_2) {
                if(key.contains(act)){
                    chars_1 = (List<String>)noc_activities.get(key);
                }
                if(!chars_1.isEmpty()){
                    character.addAll(chars_1);
                }
            }
        }
        return !character.isEmpty();
    }

    /**
     * Method to find characters for a given Advocation. Advocations would include
     * characters Genre, Category, Domains, and typical Activities as retrieved from
     * Tony Veales The NOC List.
     * @param entryAttribute
     *  The given attribute.
     * @return
     *  returns true, if characters could be found for the given attribute. Otherwise false.
     */
    private boolean findCharactersForAdvocation(String entryAttribute){
        return createKeyValues(noc_advocation, entryAttribute);
        /*if (noc_advocation.containsKey(entryAttribute)){
            this.character = (List<String>) noc_advocation.get(entryAttribute);
            System.out.println("Matching characters are: " + noc_advocation.get(entryAttribute));
            return true;
        }
        return false;*/
    }

    /**
     * Method to find characters for a given Place. Places would include characters
     * Address, City, State or Country, and fictional World as retrieved from
     * Tony Veales The NOC List.
     * @param entryAttribute
     *  The given attribute.
     * @return
     *  returns true, if characters could be found for the given attribute. Otherwise false.
     */
    private boolean findCharactersForPlace(String entryAttribute ){
        return createKeyValues(noc_origin, entryAttribute);
        /*if (noc_origin.containsKey(entryAttribute)) {
            this.character = (List<String>) noc_origin.get(entryAttribute);
            System.out.println("Matching characters are: " + noc_origin.get(entryAttribute));
            return true;
        }
        return false;*/
    }

    /**
     * Method to find characters for a given Quality. Qualities would include characters
     * Positive Talking Points and Negative Talking Points as retrieved from
     * Tony Veales The NOC List.
     * @param entryAttribute
     *  The given attribute.
     * @return
     *  returns true, if characters could be found for the given attribute. Otherwise false.
     */
    private boolean findCharactersForQuality(String entryAttribute){
        return createKeyValues(noc_allQualities, entryAttribute);
        /*if (noc_allQualities.containsKey(entryAttribute)){
            this.character = (List<String>) noc_allQualities.get(entryAttribute);
            System.out.println("Matching characters are: " + noc_allQualities.get(entryAttribute));
            return true;
        }
        return false;*/
    }

    /**
     * Return a Character matching to a field from Tony Veale's NOC List.
     * @param entryAttribute
     *  entry attribute, the character should somehow be connected with.
     *  This can be a place, a genre, a characteristic and some more things.
     * @return
     *  returns a character, randomly chosen, from a list of matching characters
     */
    public String getCharacterFromAttribute(String entryAttribute) {
        String characterName    = "";
        Random rnd              = new Random();

        if (findCharactersForActions(entryAttribute)) {
            characterName = character.get(rnd.nextInt(character.size()));
            System.out.println("\t\t\t" + "A Character is found in Actions: " + characterName);
        }
        else if (isName(entryAttribute)) {
            characterName = character.get(rnd.nextInt(character.size()));
            System.out.println("\t\t\t" + "The Attribute is a Character Name: " + characterName);
        }
        else if (findCharactersForPlace(entryAttribute)) {
            characterName = character.get(rnd.nextInt(character.size()));
            System.out.println("\t\t\t" + "A Character is found in Origins: " + characterName);
        }
        else if (findCharactersForAdvocation(entryAttribute)) {
            characterName = character.get(rnd.nextInt(character.size()));
            System.out.println("\t\t\t" + "A Character is found in Advocations: " + characterName);
        }
        else if (findCharactersForQuality(entryAttribute)) {
            characterName = character.get(rnd.nextInt(character.size()));
            System.out.println("\t\t\t" + "A Character is found in Qualities: " + characterName);
        }
        else {
            System.err.println("\t\t\t" + "No matching Character could be found for the attribute");
        }

        System.out.println("\t\t\t" + "Checking if " + characterName + " is valid for the attribute " + entryAttribute + " ...");

        if (isValidCharacterName(characterName)) {
            if(characterName.contains("-")){
                characterName = characterName.replaceAll("-", " ");
            }
            if(characterName.contains(".")){
                characterName = characterName.replaceAll(".", "");
            }
            if(characterName.contains("\"")){
                characterName = characterName.replaceAll("\"", "");
            }
            return characterName;
        }
        System.out.println("The character was not valid.");

        return getCharacterFromAttribute(entryAttribute);
    }

    /**
     * Function to check weather a name has a storyfile attached to it.
     * Returns true, if the name is part of the {@see VALID_CHARACTERS}
     * Array, otherwise it returns false.
     * @param pName
     *  Name variable to be validated.
     * @return
     *  returns true, if the name is part of the {@see VALID_CHARACTERS}
     *  Array, otherwise it returns false.
     */
    private boolean isValidCharacterName(String pName) {
        String characterName = pName.toLowerCase();
        if(characterName.contains("-")){
            characterName = characterName.replaceAll("-", " ");
        }
        if(characterName.contains(".")){
            characterName = characterName.replaceAll(".", "");
        }
        if(characterName.contains("\"")){
            characterName = characterName.replaceAll("\"", "");
        }

        for (String storyName : VALID_CHARACTERS) {
            if (storyName.contains(characterName)) {
                return true;
            }
        }
        return false;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Getter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Getter method to retrieve all characters as a List.
     * @return
     *  returns a list of all characters from the NOC list.
     */
    public Vector<String> getAllCharacters(){
        return noc_character;
    }


    //////////////////////////////////////////
    public static void main(String[] args) {
        KnowledgeData kd = new KnowledgeData();

        List<String> allCharacters = kd.getAllCharacters();

        for(String s : allCharacters){
            System.out.println(s);
        }

        /*String[] keyWords = {"lord of the rings", "love", "simpsons", "happiness", "hate", "wisdom", "strength", "romance", "steven fry", "comedy"};
        int k = 1;
        for (int i = 0; i < 5; i++){
            String keyWord = keyWords[i];
            System.out.println("Searched keyword is: " + keyWord + "\n\n");
            for (int j = 0; j < 5; j++) {
                System.out.println("\t" + k + ".\t The character is: " + kd.getCharacterFromAttribute(keyWord) + "\n");
                k++;
            }
        }*/
    }
}
