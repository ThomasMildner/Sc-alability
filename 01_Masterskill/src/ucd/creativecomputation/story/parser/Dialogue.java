package ucd.creativecomputation.story.parser;

import java.util.List;

/**
 * Class representing a Dialogue object.
 * A dialogue consists of a {@see Parser.Role}, a performance and a sentence.
 */
public class Dialogue {

    // Various class variables.
    private Parser.Role role            = null;
    private List<String> performances   = null;
    private String sentence             = null;

    /**
     * Constructor setting up a new dialogue object.
     * @param role
     *  the role or character of the dialogue.
     * @param performances
     *  the performance happening within the sentence.
     * @param sentence
     *  the sentence to be uttered.
     */
    public Dialogue(Parser.Role role, List<String> performances, String sentence){
        this.role           = role;
        this.performances   = performances;
        this.sentence       = sentence;
    }

    /**
     * Function to return the dialogue's role.
     * @return
     *  returns the role.
     */
    public Parser.Role getDialogueRole(){
        return role;
    }

    /**
     * Function to return a List of performances that happen during the sentence.
     * @return
     *  returns a List of performances.
     */
    public List<String> getDialoguePerformance(){
        return performances;
    }

    /**
     * Function to return the dialogue's sentence.
     * @return
     *  returns the sentence.
     */
    public String getDialogueSentence() {
        if(sentence.contains(":")){
            return sentence.substring(sentence.lastIndexOf(":")+1); // Filters left over character assignments and colons.
        }
        return sentence;
    }

    @Override
    public String toString() {
        return "ROLE: " + getDialogueRole()
                + ", PERFORMANCE: " + getDialoguePerformance()
                + ", SENTENCE: " + getDialogueSentence();
    }
}
