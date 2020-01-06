package ucd.creativecomputation.story.parser;

import javafx.util.Pair;
import ucd.creativecomputation.knowledgebase.nocobjects.character.Character;
import ucd.local.localnocobjects.LocalNocCharacter;
import ucd.creativecomputation.knowledgebase.nocobjects.character.NarratorCharacter;

import java.io.File;
import java.util.*;

/**
 * Class representing a Parser Object that parses a dialogue story
 */
public class Parser {

    public enum Role {
        CHARACTER_A,
        CHARACTER_B,
        CHARACTER_C,
        CHARACTER_D,
        NARRATOR,
        NO_ROLE
    }

    private String story                = null;
    private List<String> storyDelimited = null;

    private List<Pair<Parser.Role, Character>> characterList = new ArrayList<>();
    private String[] voices = {"femaleOne", "femaleTwo", "femaleThree",
            "maleOne", "maleTwo", "maleThree"};
    private String narratorVoice = "";

    /**
     * Constructor receiving a Story File that the {@see Reader} passes a story from.
     * @param storyFile
     */
    public Parser(File storyFile) {

        Reader reader = new Reader(storyFile);
        story = reader.getStory().replaceAll("\t\t", "\t");    // Filter occasional double tabs.
        storyDelimited = Arrays.asList(story.split("\t"));

        setCharacter();
    }

    /**
     * Default constructor that uses a sample story.
     */
    public Parser() {

        Reader reader = new Reader();                                            // Using default constructor to work with test a file only.
        story = reader.getStory().replaceAll("\t\t", "\t");    // Filter occasional double tabs.
        storyDelimited = Arrays.asList(story.split("\t"));

        setCharacter();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          Read Plot                                            //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Function to return the full plot of a story. A plot is a list of coherent actions that take place in the
     * Story Act.
     * @return
     *  returns the full plot of a story.
     */
    public List<String> getFullPlot() {
        String plot = storyDelimited.get(0);
        plot = plot.substring(plot.indexOf("[")+1,plot.indexOf("]"));
        return Arrays.asList(plot.split(":"));
    }

    /**
     * Function to return the short plot of the story. This is the main act usually of three actions.
     * @return
     *  reuturns the short plot of the story.
     */
    public List<String> getShortPlot() {
        String plot = storyDelimited.get(0).replaceAll("\\[.*\\]", "");
        return Arrays.asList(plot.split(":"));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                       Read Character                                          //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Method to set all characters including the narrator. This method also assigns the voices based on the
     * character's gender.
     * TODO: IMPORTANT To use this with Alexa the character change the created character to NocCharacter.
     * TODO: As long as the Object if of type LocalNocCharacter it only works for testing purposes.
     */
    private void setCharacter() {
        List<String> characterTuples = Arrays.asList(storyDelimited.get(1).split(";"));
        List<Pair<Role, Character>> characterPairs = new ArrayList<>();
        Character character;

        Role role;
        String name;
        for (String tuple : characterTuples) {
            role = getRole(tuple.split("=")[0]);
            name = tuple.split("=")[1];

            character = new LocalNocCharacter(name); //TODO: Change to online version

            // Set the character's voices based on their gender and assigns the next available voice to the object.
            if(character.getGender().equals("female")) {
                int idx = 0;
                while (voices[idx].equals("X")) {
                    idx++;
                }
                character.setVoice(voices[idx]);
                voices[idx] = "X";
            }
            else if(character.getGender().equals("male")){
                int idx = 3;
                while (voices[idx].equals("X")) {
                    idx++;
                }
                character.setVoice(voices[idx]);
                voices[idx] = "X";
            }
            characterPairs.add(new Pair<>(role, character));
        }
        // Create and add the Narrator to the list. It gets the first open voice.
        character = new NarratorCharacter();
        setNarratorVoice();
        character.setVoice(getNarratorVoice());
        characterPairs.add(new Pair(Role.NARRATOR, character));

        characterList = characterPairs;
    }

    /**
     * Function to return a list Pairs consisting of a character and their Role.
     * @return
     *  reuturns a list of character paired with their Role.
     */
    public List<Pair<Role, Character>> getCharacterList() {
        return characterList;
    }

    /**
     * Helper function to receive the role for a character based on their assigned letter in the original text.
     * @param role
     *  receives the assigned letter of a character from the original text.
     * @return
     *  returns the role for a character.
     */
    private Role getRole(String role){
        switch (role) {
            case "A":
                return Role.CHARACTER_A;
            case "B":
                return Role.CHARACTER_B;
            case "A-friend":
                return Role.CHARACTER_C;
            case "B-friend":
                return Role.CHARACTER_D;
            case "N":
                return Role.NARRATOR;
            default:
                return Role.NO_ROLE;
        }
    }

    /**
     * Method to set the Narrator's voice. It gets the first available voice.
     */
    private void setNarratorVoice() {
        int idx = 0;
        while(voices[idx].equals("X")) {
            idx++;
        }
        narratorVoice = voices[idx];
    }

    /**
     * Function to return the Narrator's voice.
     * @return
     *  returns the Narrator's voice.
     */
    public String getNarratorVoice(){
        return narratorVoice != null ? narratorVoice : voices[voices.length-1];
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                      Read Introduction                                        //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Function to return the introduction of the story. It is the first two narrative sentences of the story.
     * @return
     *  returns the story's introduction as a String.
     */
    public String getIntroduction() {
        return storyDelimited.get(2) + "\t" + storyDelimited.get(3);
    }

    /**
     * Function to return the introduction of the story. It is the first two narrative sentences of the story.
     * @return
     *  returns the story's introduction as a Pair of the Narrator's role and the String.
     */
    public Pair<Role, String> getPairedIntroduction() {
        return new Pair<Role, String>(Role.NARRATOR, getIntroduction());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                         Read Ending                                           //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Function to return the ending of the story. It is the story's last sentence.
     * @return
     *  returns the ending of the story as a String.
     */
    public String getEnding() {
        return storyDelimited.get(storyDelimited.size()-1);
    }

    /**
     * Function to return the ending of the story. It is the story's last sentence.
     * @return
     *  returns the ending of the story as a Pair of the Narrator's role and a String.
     */
    public Pair<Role, String> getPairedEnding() {
        return new Pair<Role, String>(Role.NARRATOR, getEnding());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                         Read Dialogue                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Function to return a {@see Dialogue} object from an input sentence.
     * @param subline
     *  receives a single sentence from the story.
     * @return
     *  returns a Dialogue of the input sentence.
     */
    private Dialogue toDiaogue(String subline) {
        Role role           = getRole(String.valueOf(subline.charAt(0)));
        List<String> performances  = new ArrayList<>();
        String sentence = "";

        if(subline.contains("{")) {
            String perf             = subline.substring(subline.indexOf(":"), subline.lastIndexOf("}")+1);
            List<String> tempList   = Arrays.asList(perf.replaceFirst(":","").split("}\\{"));
            for(String elem : tempList) {
                performances.add(elem.replaceAll("[({),(})]", ""));
            }
        }
        sentence = subline.substring(subline.lastIndexOf("}")+1);
        return new Dialogue(role, performances, sentence);
    }

    /**
     * Function to return the story's body.
     * @return
     *  returns the story's body as a List of sentences.
     */
    private List<String> getBody() {
        return storyDelimited.subList(4, storyDelimited.size()-1);
    }

    /**
     * Function to return a list of the story's {@StoryAct}s.
     * @return
     *  returns a list of the story's {@StoryAct}s.
     */
    public List<StoryAct> getStoryActs() {
        List<StoryAct> storyActs    = new ArrayList<>();

        for(String line : getBody()) {
            List<Dialogue> dialogues    = new ArrayList<>();
            String action               = "";

            action              = line.substring(line.indexOf("["), line.lastIndexOf("]")+1);
            String l            = line.substring(line.lastIndexOf("]")+1);
            String[] sDialogues = l.split("\\|");

            for(String d : sDialogues) {
                dialogues.add(toDiaogue(d));
            }
            storyActs.add(new StoryAct(action, dialogues));
        }

        return storyActs;
    }



    /////////////////////////////////////////////
    public static void main(String[] args) {
        Parser parser = new Parser();

        System.out.println("SHORT PLOT:\t\t" + parser.getShortPlot());
        System.out.println("FULL PLOT:\t\t" + parser.getFullPlot());
        System.out.println("NARRATOR VOICE:\t" + parser.getNarratorVoice());
        System.out.print("CHARACTER:\t\t");
        parser.getCharacterList().forEach(character ->
                System.out.print(character.getKey() + " " + character.getValue().getName() + ", " + character.getValue().getVoice() +  " | "));
        System.out.println();
        System.out.println("INTRODUCTION:\t" + parser.getIntroduction());
        System.out.println("\nDIALOGUE:");
        parser.getStoryActs();
        for(StoryAct sa : parser.getStoryActs()) {
            System.out.println("\t" + sa.toString());
        }
        System.out.println("ENDING:\t\t" + parser.getEnding());
    }
}
