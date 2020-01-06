package ucd.local.localnocobjects;

import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import static ucd.local.LocalStreamHandler.LOCAL_NOC_LIST;

/**
 * Class representing matching characters to a LocalNocCharacter.
 * @author
 * Thomas Mildner
 */
public class LocalCharacterCounterParts extends LocalKnowledgeData{

    // Various class variables.
    private LocalNocCharacter crossCharacter                 = null;
    private LocalNocCharacter negativeCounterPart            = null;
    private LocalNocCharacter positiveCounterPart            = null;
    private Vector<LocalNocCharacter> positiveCounterParts   = new Vector<>();
    private Vector<LocalNocCharacter> negativeCounterParts   = new Vector<>();

    /**
     * Construcor to create a CharacterCounterParts based on a LocalNocCharacter.
     * @param crossCharacter
     *  LocalNocCharacter the CharacterCounterParts shall match.
     */
    public LocalCharacterCounterParts(LocalNocCharacter crossCharacter) {
        this.crossCharacter = crossCharacter;

        setNegativeCounterParts();
        setPositiveCounterParts();

        // Weather the gendered counter parts consist of
        // positive or negative talking points is determinded by a coin toss.
        Vector<LocalNocCharacter> maleCounterParts;
        Vector<LocalNocCharacter> femaleCounterParts;

        if(coinToss()){
            System.out.println("Today males are negative and females are positive!");
            maleCounterParts    = maleCounterParts(negativeCounterParts);
            femaleCounterParts  = femaleCounterParts(positiveCounterParts);

            negativeCounterPart = maxNegatgiveCounterPart(maleCounterParts);
            positiveCounterPart = maxPositiveCounterPart(femaleCounterParts);
        }
        else {
            System.out.println("Today females are negative and males are positive!");
            maleCounterParts    = maleCounterParts(positiveCounterParts);
            femaleCounterParts  = femaleCounterParts(negativeCounterParts);

            positiveCounterPart = maxPositiveCounterPart(maleCounterParts);
            negativeCounterPart = maxNegatgiveCounterPart(femaleCounterParts);
        }
    }

    /**
     * Construcor to create a CharacterCounterParts based on a NOC character's name.
     * @param characterName
     *  Name of a NOC Character the CharacterCounterParts shall match.
     */
    public LocalCharacterCounterParts(String characterName) {
        crossCharacter      = new LocalNocCharacter(characterName);

        setNegativeCounterParts();
        setPositiveCounterParts();

        // Weather the gendered counter parts consist of
        // positive or negative talking points is determined by a coin toss.
        Vector<LocalNocCharacter> maleCounterParts;
        Vector<LocalNocCharacter> femaleCounterParts;

        if(coinToss()){
            System.out.println("Today males are negative and females are positive!");
            maleCounterParts    = maleCounterParts(negativeCounterParts);
            femaleCounterParts  = femaleCounterParts(positiveCounterParts);

            negativeCounterPart = maxNegatgiveCounterPart(maleCounterParts);
            positiveCounterPart = maxPositiveCounterPart(femaleCounterParts);
        }
        else {
            System.out.println("Today females are negative and males are positive!");
            maleCounterParts    = maleCounterParts(positiveCounterParts);
            femaleCounterParts  = femaleCounterParts(negativeCounterParts);

            positiveCounterPart = maxPositiveCounterPart(maleCounterParts);
            negativeCounterPart = maxNegatgiveCounterPart(femaleCounterParts);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                           Setting up the CrossChracter's attributes                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Method to set a vector of all characters that share positive talking points with the CharacterCounterParts.
     */
    private void setPositiveCounterParts(){

        Vector<String> allCharacters = getAllCharacters();

        LocalNocCharacter tmpCharacter;
        Vector<String> ptpTmpCharacter;
        Vector<String> ptpCrossCharacter;

        for (String character : allCharacters) {
            tmpCharacter        = new LocalNocCharacter(character);
            ptpTmpCharacter     = tmpCharacter.getPositiveTalkingPoints();
            ptpCrossCharacter   = crossCharacter.getPositiveTalkingPoints();

            if(hasIntersection(ptpTmpCharacter, ptpCrossCharacter) && ptpTmpCharacter != ptpCrossCharacter){
                positiveCounterParts.add(tmpCharacter);
            }
        }
    }

    /**
     * Function to find and return the character with the
     * largest intersection of positive talking points with the CharacterCounterParts.
     * @param characters
     *  vector of all characters, that share positive talking points with the CharacterCounterParts.
     * @return
     *  returns the charactern with the largest intersection
     *  of positive talking points with the CharacterCounterParts.
     */
    private LocalNocCharacter maxPositiveCounterPart(Vector<LocalNocCharacter> characters){

        LocalNocCharacter tmpCharacter = null;
        LocalNocCharacter character = null;
        Vector intersection;
        Vector ptpTmpCharacter;
        Vector ptpCrossCharacter = crossCharacter.getPositiveTalkingPoints();

        // finding character with most intersections.
        int max = 0;
        for(Object c : safe(characters)) {
            character = (LocalNocCharacter) c;
            ptpTmpCharacter = character.getPositiveTalkingPoints();
            intersection = LOCAL_NOC_LIST.intersect(ptpTmpCharacter, ptpCrossCharacter);
            //System.out.println(intersection.size() + "; " + intersection + "; " + character.getName());
            int tmp = intersection.size();
            if(tmp > max) {
                max = tmp;
                tmpCharacter = character;
            }
        }

        // return only if tmpCharacter could be set
        if(tmpCharacter != null) {
            return tmpCharacter;
        }

        System.err.println("No intersection could be found.");
        return null;
    }

    /**
     * Method to set a vector of all characters that share negative talking points with the CharacterCounterParts.
     */
    private void setNegativeCounterParts(){
        Vector<String> allCharacters = getAllCharacters();

        LocalNocCharacter tmpCharacter;
        Vector<String> ntpTmpCharacter;
        Vector<String> ntpCrossCharacter;

        for (String character : allCharacters) {
            tmpCharacter        = new LocalNocCharacter(character);
            ntpTmpCharacter     = tmpCharacter.getNegativeTalkingPoints();
            ntpCrossCharacter   = crossCharacter.getNegativeTalkingPoints();

            if(hasIntersection(ntpTmpCharacter, ntpCrossCharacter) && ntpTmpCharacter != ntpCrossCharacter){
                negativeCounterParts.add(tmpCharacter);
            }
        }
    }

    /**
     * Function to find and return the character with the
     * largest intersection of negative talking points with the CharacterCounterParts.
     * @param characters
     *  vector of all characters, that share negative talking points with the CharacterCounterParts.
     * @return
     *  returns the charactern with the largest intersection
     *  of negative talking points with the CharacterCounterParts.
     */
    private LocalNocCharacter maxNegatgiveCounterPart(Vector<LocalNocCharacter> characters){

        LocalNocCharacter tmpCharacter   = null;
        LocalNocCharacter character      = null;
        Vector intersection;
        Vector ntpTmpCharacter;
        Vector ntpCrossCharacter = crossCharacter.getNegativeTalkingPoints();

        // finding character with most intersections.
        int max = 0;
        for(Object c : safe(characters)) {
            character       = (LocalNocCharacter) c;
            ntpTmpCharacter = character.getNegativeTalkingPoints();
            intersection    = LOCAL_NOC_LIST.intersect(ntpTmpCharacter, ntpCrossCharacter);
            int tmp         = intersection.size();

            if(tmp > max) {
                max = tmp;
                tmpCharacter = character;
                //System.out.println("Current tmp char is: " + tmpCharacter.getName());
            }
        }

        // return only if tmpCharacter could be set
        if(tmpCharacter != null) {
            return tmpCharacter;
        }

        System.err.println("No intersection could be found.");
        return null;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Helper Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper method to determine weather two vectors have an intersection.
     * This can be used to find equalities between two characters.
     * @param v1
     *  Vector of attributes (for first character)
     * @param v2
     *  Vector of attributes (for second character)
     * @return
     *  returns true, if an intersection between both vectors could be set, otherwise false.
     */
    private boolean hasIntersection(Vector<String> v1, Vector<String> v2) {
        if (v1 == null || v1.isEmpty() || v2 == null || v2.isEmpty()) {
            return false;
        }

        Vector intersection = LOCAL_NOC_LIST.intersect(v1, v2);
        if (intersection != null && intersection.size() >= 1) {
            return true;
        }

        return false;
    }

    /**
     * Helper mehtod to determine weather a vector of characters contains any females to
     * end up with a variation in positive and negative talking point characters.
     */
    private Boolean hasFemales(Vector<LocalNocCharacter> characters) {
        if (characters == null || characters.isEmpty()) {
            System.out.println("The vector of characters is empty or does not exist!");
            return false;
        }

        LocalNocCharacter character = null;
        for (Object c : safe(characters)) {
            character = (LocalNocCharacter) c;

            if (character.getGender().equals("female")){
                return true;
            }
        }
        return false;
    }

    /**
     * Helper method to return a subset of a character vector containing only males.
     * @param characters
     *  vector of characters.
     * @return
     *  returns a subset of character vector contianing only males.
     */
    private Vector<LocalNocCharacter> maleCounterParts(Vector<LocalNocCharacter> characters){
        LocalNocCharacter tmpCharacter;
        Vector<LocalNocCharacter> males = new Vector<>();

        for (Object c : safe(characters)) {
            tmpCharacter = (LocalNocCharacter) c;
            if (tmpCharacter.getGender() != null && tmpCharacter.getGender().equals("male")) {
                males.add(tmpCharacter);
            }
        }

        return males;
    }

    /**
     * Helper method to return a subset of a character vector containing only females.
     * @param characters
     *  vector of characters.
     * @return
     *  returns a subset of character vector contianing only females.
     */
    private Vector<LocalNocCharacter> femaleCounterParts(Vector<LocalNocCharacter> characters){
        LocalNocCharacter tmpCharacter;
        Vector<LocalNocCharacter> males = new Vector<>();

        for (Object c : safe(characters)) {
            tmpCharacter = (LocalNocCharacter) c;
            if (tmpCharacter.getGender() != null && tmpCharacter.getGender().equals("female")) {
                males.add(tmpCharacter);
            }
        }

        return males;
    }

    /**
     * Helper method ot determine which counterpart will be male or female.
     * @return
     *  returns either true of false at random.
     */
    private boolean coinToss(){
        Random flip= new Random();

        int toss = flip.nextInt(2);
        if (toss == 0){
            return true;
        }

        return false;
    }

    /**
     * Helper method to only allow checks on safe elements of a Vector.
     * @param pVector
     *  Vector to secure.
     * @return
     *  returns a safe Vector.
     */
    private Vector safe(Vector pVector) {
        return pVector == null ? (Vector) Collections.EMPTY_LIST : pVector;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Getter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Getter function to return a negative counterpart of the CrossChracter.
     * @return
     *  returns a negative counterpart of the CharacterCounterParts.
     */
    public LocalNocCharacter getNegativeCounterPart() {
        return negativeCounterPart;
    }

    /**
     * Getter function to return a positive counterpart of the CrossChracter.
     * @return
     *  returns a positive counterpart of the CharacterCounterParts.
     */
    public LocalNocCharacter getPositiveCounterPart() {
        return positiveCounterPart;
    }


    ////////////////////////////////////////
    public static void main(String[] args) {
        LocalCharacterCounterParts crossCharacter = new LocalCharacterCounterParts("Donald Trump");

        System.out.println("negative counter part " + crossCharacter.negativeCounterPart.getName());
        System.out.println("positive counter part " + crossCharacter.positiveCounterPart.getName());
        //System.out.println(crossCharacter.maxNegatgiveCounterPart(crossCharacter.negativeCounterParts).getName());
    }
}