package ucd.local.localnocobjects;

import ucd.creativecomputation.knowledgebase.nocobjects.character.Character;
import ucd.creativecomputation.knowledgebase.nocobjects.errors.NoCharacterFoundException;

import java.util.Vector;

import static ucd.local.LocalStreamHandler.LOCAL_COUNTERPARTS_LIST;
import static  ucd.local.LocalStreamHandler.LOCAL_NOC_LIST;

/**
 * Class representing a character from Tony Veale's NOC LIst.
 * NocCharacters help to set up narrative modules such as a title, riddles and
 * other character based content without having to load the same data over and over again.
 *
 * @author
 * Thomas Mildner
 */
public class LocalNocCharacter extends LocalKnowledgeData implements Character {

    private String name = "";
    private Vector<String> positiveTalkingPoints = null;
    private Vector<String> negativeTalkingPoints = null;

    private String singlePositiveTalkingPoint   = "";
    private String singleNegativeTalkingPoint   = "";

    private String positiveCounterpart          = "";
    private String negativeCounterpart          = "";

    private String voice                        = null;

    /**
     * Constructor to create a NOC Character based on the name.
     * @param name
     *  Character's name.
     */
    public LocalNocCharacter(String name) {

        try {
            this.name = LOCAL_NOC_LIST.getFirstValue("Character", name);
            setSinglePositiveTalkingPoint();
            setSingleNegativeTalkingPoint();

            setPositiveTalkingPoints();
            setNegativeTalkingPoints();

            setPositiveCounterpart();
            setNegativeCounterpart();
        }
        catch (Exception e){
            System.err.println("No character was found with the name " + name + " using the NOC List.");
            System.err.println(e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Setter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Setter method to retrieve a single positive talking point for the character.
     */
    private void setSinglePositiveTalkingPoint() {
        try {
            singlePositiveTalkingPoint = LOCAL_NOC_LIST.getFirstValue("Positive Talking Points", name);
        }
        catch (NullPointerException npe){
            System.out.println("No positive talking point for " + name + " was found.");
            System.err.println(npe.getMessage());
            singlePositiveTalkingPoint= "0";
        }
    }

    /**
     * Setter method to retrieve all positive talking points for the character.
     */
    private void setPositiveTalkingPoints() {
        try {
            positiveTalkingPoints = LOCAL_NOC_LIST.getFieldValues("Positive Talking Points", name);
        }
        catch (NullPointerException npe){
            System.out.println("No positive talking point for " + name + " was found.");
            System.err.println(npe.getMessage());
        }
    }

    /**
     * Setter method to retrieve a single negative talking point for the character.
     */
    private void setSingleNegativeTalkingPoint() {
        try {
            singleNegativeTalkingPoint = LOCAL_NOC_LIST.getFirstValue("Negative Talking Points", name);
        }
        catch (NullPointerException npe){
            System.out.println("No negative talking point for " + name + " was found.");
            System.err.println(npe.getMessage());
            singlePositiveTalkingPoint= "0";
        }
    }

    /**
     * Setter method to retrieve all negative talking points for the character.
     */
    private void setNegativeTalkingPoints() {
        try {
            negativeTalkingPoints = LOCAL_NOC_LIST.getFieldValues("Negative Talking Points", name);
        }
        catch (NullPointerException npe){
            System.out.println("No negative talking point for " + name + " was found.");
            System.err.println(npe.getMessage());
        }
    }

    /**
     * TODO
     * @throws NoCharacterFoundException
     */
    private void setPositiveCounterpart() throws NoCharacterFoundException {
        if (LOCAL_COUNTERPARTS_LIST.getFirstValue("Positive Counterpart", name).equals(" ")) {
            throw new NoCharacterFoundException("No positive counterpart could be found!");
        }
        positiveCounterpart = LOCAL_COUNTERPARTS_LIST.getFirstValue("Positive Counterpart", name);
    }

    /**
     * TODO
     * @throws NoCharacterFoundException
     */
    private void setNegativeCounterpart() throws NoCharacterFoundException {
        if (LOCAL_COUNTERPARTS_LIST.getFirstValue("Negative Counterpart", name).equals(" ")) {
            throw new NoCharacterFoundException("No negative counterpart could be found!");
        }
        negativeCounterpart = LOCAL_COUNTERPARTS_LIST.getFirstValue("Negative Counterpart", name);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Getter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Getter method to return a single positive talking point of the character.
     * @return
     *  returns a single positive talking point of the character.
     */
    public String getSinglePositiveTalkingPoint(){

        return singlePositiveTalkingPoint;
    }

    /**
     * Getter method to return all positive talking points of the NOC Character.S
     *
     * @return
     *  returns all positive talking points of the NOC Character.
     */
    public Vector<String> getPositiveTalkingPoints() {
        return positiveTalkingPoints;
    }

    /**
     * Getter method to return a single negative talking point of the character.
     * @return
     *  returns a single positive talking point of the character.
     */
    public String getSingleNegativeTalkingPoint(){
        return singleNegativeTalkingPoint;
    }

    /**
     * Getter method to return all negative talking points of the NOC Character.
     * @return
     *  returns all negative talking points of the NOC Character.
     */
    public Vector<String> getNegativeTalkingPoints() {
        return negativeTalkingPoints;
    }

    /**
     * Getter method to return the gender of the NOC Character.
     * @return
     *  returns the NOC Character's gender.
     */
    public String getGender() {
        return LOCAL_NOC_LIST.getFirstValue("Gender", name);
    }

    /**
     * Getter method to return the NOC Character's name.
     * @return
     *  returns the NOC Character's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method to return the AKA of the NOC Character.
     * @return
     *  returns the NOC Character's AKA.
     */
    public String getAKA(){
        return LOCAL_NOC_LIST.getFirstValue("AKA", name);
    }

    /**
     * TODO
     * @return
     */
    public String getPositiveCounterpart() {
        return positiveCounterpart;
    }

    /**
     * TODO
     * @return
     */
    public String getNegativeCounterpart() {
        return negativeCounterpart;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          Set Voices                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Sets the voice for the character. This can only be done once while creating the object.
     * @param voice
     *  the voice to be assigned. It can only be assigned once.
     */
    public void setVoice(String voice)  {
        this.voice = this.voice == null ? voice : this.voice;
    }

    /**
     * Returns the character's voice.
     * @return
     *  returns the character's voice.
     */
    public String getVoice() {
        return voice != null ? voice : "no-voice";
    }

    ////////////////////////////////////////
    public static void main(String[] args) {
        LocalNocCharacter character = new LocalNocCharacter("Homer Simpson");
        System.out.println(character.getName());
        System.out.println(character.getAKA());
        System.out.println(character.getPositiveTalkingPoints());
        System.out.println(character.getNegativeTalkingPoints());
        System.out.println(character.getNegativeCounterpart());
        System.out.println(character.getPositiveCounterpart());
    }
}
