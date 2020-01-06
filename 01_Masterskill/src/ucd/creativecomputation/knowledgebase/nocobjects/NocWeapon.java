package ucd.creativecomputation.knowledgebase.nocobjects;

import ucd.creativecomputation.knowledgebase.nocobjects.character.NocCharacter;

import static ucd.creativecomputation.alexa.NarratorStreamHandler.AFFORDANCES_LIST;
import static ucd.creativecomputation.alexa.NarratorStreamHandler.NOC_LIST;
import static ucd.creativecomputation.alexa.NarratorStreamHandler.WEAPON_ARSENAL_LIST;

/**
 * Class representing a weapon of choice from Tony Veale's the NOC List.
 *
 * @author
 * Thomas Mildner
 */
public class NocWeapon extends KnowledgeData {

    // Various class variables
    private String weaponOfChoice   = "";
    private String character        = "";
    private String determiner       = "";
    private String affordance       = "";
    private String pastTense        = "";

    /**
     * Class constructor setting up a weapon of choice for a noc character.
     * @param nocCharacter
     */
    public NocWeapon (NocCharacter nocCharacter){
        this.character      = nocCharacter.getName();

        setWeaponOfChoice();
        setAffordance(weaponOfChoice);
        setDeterminer(weaponOfChoice);
        setPastTense(affordance);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Setter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Setter method to retrieve the weapon of choice for a noc character.
     */
    private void setWeaponOfChoice(){
        if(character == null){
            weaponOfChoice = "0";
        }

        try {
            weaponOfChoice = NOC_LIST.getFirstValue("Weapon of Choice", character);
        }
        catch(NullPointerException npe){
            System.out.println("No weapon for " + character + " was found.");
            System.err.println(npe.getMessage());
            weaponOfChoice = "0";
        }
    }

    /**
     * Setter method to retrieve a weapon's determiner.
     * @param weapon
     *  a weapon to retrieve a determiner from.
     */
    private void setDeterminer(String weapon){
        if(weapon == null || weapon.equals("0")){
            determiner = " ";
        }

        try {
            determiner = WEAPON_ARSENAL_LIST.getFirstValue("Determiner", weapon);
        }
        catch (NullPointerException npe){
            System.out.println("No determiner for " + weapon + " was found.");
            System.err.println(npe.getMessage());
            determiner =  " ";
        }
    }

    /**
     * Setter method to retrieve a weapon's affordance.
     * @param weapon
     *  a weapon to retrieve its affordance from.
     */
    private void setAffordance(String weapon){
        if(weapon == null || weapon.equals("0")){
            affordance = "0";
        }

        try {
            affordance =  WEAPON_ARSENAL_LIST.getFirstValue("Affordances", weapon);
        }
        catch (NullPointerException npe){
            System.out.println("No affordance for " + weapon + " was found.");
            System.err.println(npe.getMessage());
            affordance = " 0 ";
        }

    }

    /**
     * Setter method to set a past tense of an affordance.
     * @param affordance
     *  a affordance to retrieve the past tense from.
     */
    private void setPastTense(String affordance){
        if(affordance == null || affordance.equals("0")){
            pastTense = " killed ";
        }

        try {
            pastTense = AFFORDANCES_LIST.getFirstValue("Past Tense", affordance);
        }
        catch (NullPointerException npe){
            System.out.println("No past tense for " + affordance + " was found.");
            System.err.println(npe.getMessage());
            pastTense = " killed ";
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Getter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Getter method to return the weapon of choice of a character.
     * @return
     *  returns the character's weapon of choice.
     */
    public String getWeaponOfChoice() {
        return weaponOfChoice;
    }

    /**
     * Getter method to return the past tense of an affordance.
     * @return
     *  returns the past tense of an affordance.
     */
    public String getPastTense() {
        return pastTense;
    }

    /**
     * Getter method to return a weapon's affordance.
     * @return
     *  returns a weapon's affordance.
     */
    public String getAffordance() {
        return affordance;
    }

    /**
     * Getter method to return a weapon's determiner.
     * @return
     *  returns the determiner for a weapon.
     */
    public String getDeterminer() {
        return determiner;
    }

    ////////////////////////////////////////
    public static void main(String[] args) {
        NocCharacter character = new NocCharacter("Adam Sandler");
        NocWeapon weapon = new NocWeapon(character);

        System.out.println(weapon.getAffordance()
                + weapon.getDeterminer()
                + weapon.getPastTense()
                + weapon.getWeaponOfChoice());
    }
}
