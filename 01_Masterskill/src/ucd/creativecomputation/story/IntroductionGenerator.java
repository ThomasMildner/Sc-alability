package ucd.creativecomputation.story;
import ucd.creativecomputation.knowledgebase.nocobjects.KnowledgeData;
import ucd.creativecomputation.knowledgebase.nocobjects.character.NocCharacter;
import ucd.creativecomputation.knowledgebase.nocobjects.NocWeapon;
import ucd.local.localnocobjects.LocalKnowledgeData;

import java.util.List;
import java.util.Random;

/**
 * Class representing an introduction for a character. This class is supposed to be expandable
 * for different alternatives for introductions.
 *
 * Featured introductions:
 *  Weaponized 'Boom' introduction
 *
 * @author
 * Thomas Mildner
 */
public class IntroductionGenerator {

    // Various class variables.
    private NocCharacter nocCharacter   = null;
    private Random random       = new Random();
    private String character    = "";
    private String introduction = "";

    // Array containing a selection of the infamous Doctor Smith's Insults.
    private String[] insults    = {"Addlepated Amateur" ,"Addlepated Armorbearer" ,"Arrogant Automaton"
        ,"Astigmatic Automaton" ,"Babbling Birdbrain" ,"Babbling Bumpkin" ,"Bellicose Bumpkin"
        ,"Blithering Booby" ,"Blithering Bumpkin" ,"Bookmaking Booby"
        ,"Bow your Bubble" ,"Bubble-headed Booby" ,"Bumbling Birdbrain" ,"Bumbling Bag of Bolts"
        ,"Bumbling Bucket of Bolts" ,"Bumbling Booby" ,"Bumptious Braggart" ,"Cackling Cacophony"
        ,"Cackling Canister" ,"Cackling Clod" ,"Cackling Cuckoo" ,"Calamitous Clump"
        ,"Cantankerous Cold-hearted Clump" ,"Caterwauling Clod" ,"Cautious Clump"
        ,"Clod-like Collection of Condensers" ,"Clumsy Clump" ,"Computerized Clod"
        ,"Confused Compass" ,"Cowardly Clump" ,"Cumbersome Clod" ,"Cumbersome Clump"
        ,"Defective Detective" ,"Demented Diode" ,"Deplorable Dunderhead" ,"Disreputable Dunderhead"
        ,"Doddering Dunderhead" ,"Ferrous Frankenstein" ,"Floundering Flunky" ,"Foolish Fop"
        ,"Frightful Fractious Frump" ,"Gargantuan Goose" ,"Garrulous Gargoyle" ,"Gigantic Gargoyle"
        ,"Great Goose" ,"Gregarious Gremlin" ,"Hard-headed Harbinger of Evil" ,"Hardware Hyena"
        ,"Hopeless Heap of Tainted Tin","Incompetent Idiot" ,"Incompetent Imbecile" ,"Ineffectual Ineptitude"
        ,"Inept Idiot" ,"Infamous Informer" ,"Ingot of Ingratitude" ,"Insensitive Idiot" ,"Insipid Ineptitude"
        ,"Iron-born Ingrate" ,"Jangling Junkheap" ,"Juvenile Junk Pile" ,"Lame-brained Lump" ,"Lead-lined Lump"
        ,"Lily-livered Lump" ,"Mechanical Meddler" ,"Mechanical Misfit" ,"Mechanical Monolith"
        ,"Mechanized Maidservant" ,"Mediocre Medical Misfit" , "Mess of Metal" ,"Metallic Monstrosity"
        ,"Misguided Mechanical Misery", "Monstrous Mechanized Misguided Moron"
        ,"Nattering Ninny" ,"Pathetic Pomposity" ,"Pedagogical Pip-Squeak" ,"Plasticized Parrot"
        ,"Pompous Pig Squeak" ,"Ponderous Plumber" ,"Pot-bellied Prankster" ,"Pot-headed Prankster"
        ,"Primitive Pile of Pistons" ,"Pusillanimous Pinhead" ,"Pusilanimous Puppet", "Pusillanimous Punka"
        ,"Ramshackle Romeo" ,"Ridiculous Robot" ,"Ridiculous Roustabout" ,"Roly-Poly Rowdy"
        ,"Rusty Rasputin","Sanctimonious Scatterbrain" ,"Scurrilous Scatterbrain" ,"Sententious Sloth"
        ,"Sickening Cybernetic" ,"Silly Sausage" ,"Silly Sloth" ,"Silver-plated Sellout" ,"Tarnished Trumpet"
        ,"Tin-plated Tinhorn" ,"Tin-plated Traitor" ,"Tintinnabulating Tin Can" ,"Tiresome Thesaurus"
        ,"Traitorous Tin Tabulation" ,"Treasonous Tyrant" ,"Tyrannical Tin Plate" ,"Unctuous Underling"
        ,"Ungrateful Underling" ,"Wobbling Weakling"};

    /**
     * Constructor setting up a character introduction.
     * @param character
     *  receives a character name as a String.
     */
    public IntroductionGenerator(String character){
        this.nocCharacter = new NocCharacter(character);
    }

    /**
     * Constructor setting up a character introduction.
     * @param character
     *  receives a NocCharacter object.
     */
    public IntroductionGenerator(NocCharacter character){
        this.nocCharacter = character;;
    }

    /**
     * Function to generate an introduction.
     * @return
     *  returns a boom introduction.
     */
    /*private String generateIntroduction(){
        String[] introductions = {
                "Prepare to be " + pastTense + " " + determiner + " " + weapon + "!",
                "Expect to be " + pastTense + " " + determiner + " " + weapon + "!",
                "Get ready to be " + pastTense + " " + determiner + " " + weapon + "!",
        };

        if(weapon.equals("0") || affordance.equals("0") || pastTense.equals("0")){
            return "Hm, I can't think of a good character. NAA0, who would you suggest?";
        }
        return (introductions[random.nextInt(introductions.length)].replaceAll("null", ""));
    }*/


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Getter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Function to return one of the modular introductions.
     * As time goes by, more can be added to make them more variable.
     * @return
     *  returns a random introduction for a character.
     */
    public String getIntroduction(){
        Random random = new Random();
        try {
            // As more come, add more introductions to this array in the future.
            String[] introductions = {getBoomIntroduction(), getCrossOverIntroduction()};
            String introduction = introductions[random.nextInt(introductions.length)];
            if (introduction.equals("$")){
                throw new Exception("No introduction could be set for " + character);
            }
            return introduction;
        }
        catch (Exception npe) {
            System.err.println("No introduction could be set for " + character);
            System.err.println(npe.getMessage());
            return "Hey " + getDoctorSmithInsult() + ". I think we should introduce another character";
        }
    }

    /**
     * Function to return a weaponized 'BOOM' introduction for a single character.
     * @return
     *  returns a weaponized 'BOOM' introduction for a single character.
     */
    public String getBoomIntroduction() {
        NocWeapon nocWeapon = new NocWeapon(nocCharacter);

        String weapon       = nocWeapon.getWeaponOfChoice();
        String pastTense    = nocWeapon.getPastTense();
        String determiner   = nocWeapon.getDeterminer();
        String affordance   = nocWeapon.getAffordance();

        String[] introductions  = {
                "Prepare to be " + pastTense + " " + determiner + " " + weapon + "!",
                "Expect to be " + pastTense + " " + determiner + " " + weapon + "!",
                "Get ready to be " + pastTense + " " + determiner + " " + weapon + "!",
        };

        // If one of the return statements is a '0' meaning none could be found, a general response will be returned.
        if(weapon.equals("0") || affordance.equals("0") || pastTense.equals("0")){
            // Returned a non-statement. Now instead, a key is passed to envoke a different enactment.
            // return "Hm, I can't think of a good character. NAA0, who would you suggest?";
            return "$"; // Return '$' to signify not a good introduction could be found.
        }

        try {
            return (introductions[random.nextInt(introductions.length)].replaceAll("null", ""));
        }
        catch (Exception e) {
            System.err.println("There was a problem in getting a BoomIntroduction for " + character);
            System.err.println(e.getMessage());
            // Returned a nonstatement. Now instead, a key is passed to envoke a different enactment.
            // return "Hey " + getDoctorSmithInsult() + ". I think we should introduce another character";
            return "$"; // Return '$' to signify not a good introduction could be found.
        }
    }

    /**
     * Function to return a cross character introduction for a single character.
     * @return
     *  returns a cross character introduction introduction for a single character.
     */
    public String getCrossOverIntroduction() {
        //CharacterCounterParts crossCharacters = new CharacterCounterParts(nocCharacter);
        String positiveCounterPart = nocCharacter.getPositiveCounterpart();
        String negativeCounterPart = nocCharacter.getNegativeCounterpart();
        try {
            if (positiveCounterPart.equals("") || negativeCounterPart.equals("")){
                return "$";
            }
            return "What do you get if you cross " + positiveCounterPart
                    + " and " + negativeCounterPart + "?";
        }
        catch (Exception e) {
            System.err.println("There was a problem in getting a CrossOverIntroduction for " + character);
            System.err.println(e.getMessage());
            // Returned a non-statement. Now instead, a key is passed to envoke a different enactment.
            // return "Hey " + getDoctorSmithInsult() + ". I think we should introduce another character";
            return "$"; // Return '$' to signify not a good introduction could be found.
        }
    }

    private String getDoctorSmithInsult(){
        return insults[random.nextInt(insults.length)];
    }

    public String getCharacter(){
        return character;
    }

    //////////////////////////////////////////
    public static void main(String[] args) {
        /*KnowledgeData kd = new KnowledgeData();
        List<String> allCharacters = kd.getAllCharacters();
        IntroductionGenerator introductionGenerator;
        for (String character : allCharacters){
            try {
                introductionGenerator = new IntroductionGenerator(character);
                System.out.println(character + "\t | \t" + introductionGenerator.getIntroduction());
                System.out.println(character + "\t | \t" + introductionGenerator.getCrossOverIntroduction());
            }
            catch(NullPointerException npe){
                System.err.println("There was a problem in getting an introduction for " + character);
                System.err.println(npe);
            }
        }*/

        LocalKnowledgeData kd = new LocalKnowledgeData();
        List<String> allCharacters = kd.getAllCharacters();
        IntroductionGenerator introductionGenerator;

        for (String character : allCharacters){
            try {
                introductionGenerator = new IntroductionGenerator(character);
                //System.out.println(character + "\t | \t" + introductionGenerator.getIntroduction());
                System.out.println(character + "\t | \t" + introductionGenerator.getCrossOverIntroduction());
            }
            catch(NullPointerException npe){
                System.err.println("There was a problem in getting an introduction for " + character);
                System.err.println(npe);
            }
        }
    }
}
