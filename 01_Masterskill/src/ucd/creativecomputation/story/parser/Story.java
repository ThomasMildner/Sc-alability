package ucd.creativecomputation.story.parser;

import javafx.util.Pair;
import ucd.creativecomputation.knowledgebase.nocobjects.character.Character;

import java.io.File;
import java.util.List;

/**
 * Class representing a story. Every story consists of a plot, a separate introduction and ending,
 * a list of character that take part in the story, the story as a list of {@see StoryAct}s,
 *
 * The class has a default constructor that works with a sample story.
 */
public class Story {

    // Various class variables
    private List<Pair<Parser.Role, Character>> characterList  = null;
    private List<String> fullPlot       = null;
    private List<String> shortPlot      = null;
    private Pair introduction           = null;
    private Pair ending                 = null;
    private List<StoryAct> storyActs    = null;

    /**
     * Constructor taking a storyFile to receive a specific story from {@see Reader} in {@see Parser}.
     * @param storyFile
     *  Story File that includes the story as text this object will be created on.
     */
    public Story(File storyFile) {
        Parser parser = new Parser(storyFile);
        fullPlot    = parser.getFullPlot();
        shortPlot   = parser.getShortPlot();
        storyActs   = parser.getStoryActs();
        characterList   = parser.getCharacterList();
        introduction    = parser.getPairedIntroduction();
        ending          = parser.getPairedEnding();
    }

    /**
     * Default constructor that uses a single sample story.
     */
    public Story(){
        Parser parser = new Parser();
        fullPlot    = parser.getFullPlot();
        shortPlot   = parser.getShortPlot();
        storyActs   = parser.getStoryActs();
        characterList   = parser.getCharacterList();
        introduction    = parser.getPairedIntroduction();
        ending          = parser.getPairedEnding();
    }

    /**
     * Function to return a character object by it's individual Role.
     * @param role
     *  the role, the character has.
     * @return
     *  the matching character to the {@param role}.
     */
    private Character getCharacterByRole(Parser.Role role) {
        for(Pair p : characterList) {
            if(p.getKey() == role) {
                return (Character) p.getValue();
            }
        }
        return null;
    }

    /**
     * Function to list all Story Act sentences as prints for control.
     */
    private void listStoryActSentences() {
        for(StoryAct sa : storyActs){
            for(Dialogue d : sa.getDialogueList()) {
                System.out.println(d.getDialogueRole() + " " + getSpeechLine(d));
            }
        }
    }

    /**
     * Function to return the speechline of a {@see Dialogue} of a character with necessary voice tags.
     * @param dialogue
     *  input dialogue the speechlines are extracted from.
     * @return
     *  returns the spechline of a Dialogue with necessary voice tags.
     */
    public String getSpeechLine(Dialogue dialogue) {
        Parser.Role role = dialogue.getDialogueRole();
        String line = dialogue.getDialogueSentence();
        Character character;
        String voice = "";

        for(Pair p : characterList) {
            if (p.getKey() == role) {
                character = (Character) p.getValue();
                voice = character.getVoice();
            }
        }
        String output = "<" + voice + ">" + line + "</" + voice + ">";
        return output;
    }

    public static void main(String[] args) {
        Story story = new Story();
        story.listStoryActSentences();
    }
}
