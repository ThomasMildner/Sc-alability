package ucd.creativecomputation.story;

import java.util.*;

import ucd.creativecomputation.toolkit.retriever.URLStoryFileRetriever;
import ucd.creativecomputation.knowledgebase.nocobjects.KnowledgeData;
import ucd.creativecomputation.toolkit.StoryFiles;

import static java.lang.Integer.parseInt;

/**
 * Class representing a Parser to clean a story object from Tony Veale's Multicharacter Story Files for Alexa to
 * say eventually. The class provides a solid parser, that filters emojis, their adjectives and articles
 * from the stories.
 * @author
 * Thomas Milner
 */
public class StoryParser {

    private Vector<String> storyLines = new Vector<String>();
    private String storyString;
    private String unparsedStory;
    private int storyIdentifier;
    // Currently not in use.
    private String emojiRegex;

    /**
     * Constructor for the class StoryParser setting up an Emoji regular expression to filter out all
     * emojis used in the stories.
     * @param characterName
     *   A name of a character will be used to return a story for a specific character.
     */
    public StoryParser(String characterName){

        // To correct the string structure from 'RandomStoryIntentHandler'.
        if (characterName.contains(" ")){
            characterName = characterName.replaceAll(" ", "-");
        }

        // Get a story based on the input character @param characterName.
        URLStoryFileRetriever retriever = new URLStoryFileRetriever();
        storyString = retriever.getStoryByName(characterName);

        // List of all emojis used in the stories.
        List<String> emojiList = Arrays.asList("1F63A", "1F63A", "1F638", "1F63C", "1F435", "1F412", "1F435", "1F436",
                                                "1F415", "1F429", "1F43A", "1F431", "1F981", "1F42F", "1F434", "1F984",
                                                "1F42E", "1F403", "1F437", "1F417", "1F40F", "1F411", "1F410", "1F42A",
                                                "1F42B", "1F418", "1F42D", "1F421", "1F42D", "1F400", "1F439", "1F430",
                                                "1F407", "1F43F", "1F43B", "1F428", "1F43C", "1F983", "1F414", "1F413",
                                                "1F425", "1F426", "1F427", "1F438", "1F40A", "1F422", "1F40D", "1F432",
                                                "1F409", "1F433", "1F40B", "1F42C", "1F41F", "1F420", "1F421", "1F419",
                                                "1F980", "1F41B", "1F41B", "1F41C", "1F40C", "1F41D", "1F41E", "1F577",
                                                "1F982", "1F364", "1F406", "1F402", "1F404", "1F424", "1F426");

        for(String s : emojiList){
            String r = "\\" + "\\u" + s;
            emojiRegex += r;
        }

        unparsedStory = getStory();
        //getStory();
    }

    /**
     * Constructor that takes a story-string as the input.
     */
    public StoryParser(String storyString, boolean isStoryString){
        if(isStoryString){
            this.storyString = storyString;
        }
        unparsedStory = getStory();
    }

    /**
     * Empty constructor that generates a random story.
     */
    public StoryParser(){
        String characterName = StoryFiles.getRandomNameFromFile();

        // Get a story based on the input character @param characterName.
        URLStoryFileRetriever retriever = new URLStoryFileRetriever();
        storyString = retriever.getStoryByName(characterName);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Getter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Function to return the {@see private String storyString}.
     * Its only use is through the {@see ItemGenerator} class,
     * which uses this string in order to regenerate it for the robot.
     * @return
     *  Returns the basic story string of this class.
     */
    public String getStoryString() {
        return storyString;
    }

    /**
     * Method returns an emoji-infested and parsed story, that will be
     * told by the narrator skill by Alexa.
     * @return
     *  returns an emoji-infested and parsed story.
     */
    public String getUncleanStory(){
        return uncleanStory(storyString);
    }

    /**
     * Method returns a cleaned and parsed story, that will be told by
     * the narrator skill by Alexa.
     * @return
     *  returns a cleaned and parsed story.
     */
    public String getStory(){
        return cleanStory(storyString);
    }

    /**
     * Method to return all lines of a story as a List of Strings.
     * @return
     *  returns all lines of a story as a List of Strings.
     */
    public List<String> getStoryLines(){
        return storyLines;
    }


    public int getStoryIdentifier(){
        return parseInt(storyString.split(":")[0]);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Setter Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Method prepares a Story List for the parser to transform
     * the Story lines for further usage.
     * @param storyParts
     *  A list of singular story lines, that together build one story.
     * @param story
     *  A list with pre-cleaned lines making it easier to be transformed later on.
     */
    private void setStoryLines(List<String> storyParts, List<String> story) {
        String s1;

        for(int i = 0; i < storyParts.size(); i++){
            if(i == 0){
                storyParts.get(i).split(",");
            }
            s1 = storyParts.get(i).substring(storyParts.get(i).lastIndexOf("|") + 1);
            story.add(s1);
        }
        story.remove(0);
    }

    /**
     * Method to set the class variable storyLines.
     * @param story
     *  The story, that will be set as the storyLines.
     *  This needs to be parsed beforehand.
     */
    private void setStoryLines(String story){
        Vector<String> storyList = new Vector<String>(Arrays.asList(story.split("  ")));
        storyLines = storyList;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                        Parser Methods                                         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Function that cleans a story from all redundant elements
     * such as as emojis for the Narrator Skill to pronounce.
     * @param pStory
     *  receives a story string on which it will call
     *  multiple regular expressions.
     * @return
     *  returns a cleaned story string.
     */
    private String cleanStory(String pStory){
        List<String> storyParts = new LinkedList<>(Arrays.asList(pStory.split("\t")));
        List<String> story = new ArrayList<>();
        storyParts.remove(storyParts.size()-1);
        String storyOut = "";

        //Regex to filter the final result for specific parts
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";

        int counter = 0;
        while (counter < 3) {
            storyParts.remove(counter);
            counter++;
        }

        setStoryLines(storyParts, story);

        String firstSentence = spliceEmoji(story.get(0).replaceAll(",", ""))
                .replaceAll("who thought it was ", "");

        story.set(0, firstSentence);

        for(String sentence : story){
            storyLines.add(spliceEmoji(sentence
                            .replaceAll(" \\?", "\\?")
                            .replaceAll("the " + regex, "")
                            .replaceAll(" \\.", "\\.")
                            .replaceAll(" '", "'")
                            .replaceAll(" , ", " ")
                            .replaceAll("\\.\\.", ".")
                            .replaceAll("\\?\\.", "?")
                            .replaceAll("  ", " ")
                            .replaceAll(" \\. ", "")
                    ) + " "
            );
        }

        for(String s : storyLines){
            //System.out.println("\t .. stitch stitch.. " +  s);
            if(s.contains("End")) {
                storyOut += s.replaceAll(" \\. ", " ")
                        .replace("End", "end.");
            }
            else if(s.contains("Wedding")) {
                storyOut += s.replaceAll(" \\. ", " ")
                        .replace("Wedding", "wedding");
            }
            else {
                storyOut += s.replaceAll(" \\. ", " ");
            }
        }

        setStoryLines(storyOut);

        return varifyStory(storyOut);
    }

    /**
     * Function returns a story containing descriptive emojis
     * for the character that else would've been filtered out.
     * @param pStory
     *  Unfitered story list
     * @return
     *  filtered story string still containing emojis.
     */
    private String uncleanStory(String pStory){
        List<String> storyParts = new LinkedList<>(Arrays.asList(pStory.split("\t")));
        List<String> story = new ArrayList<>();
        storyParts.remove(storyParts.size()-1);
        String storyOut = "";

        int counter = 0;

        while(counter < 3){
            storyParts.remove(counter);
            counter++;
        }

        setStoryLines(storyParts, story);

        for(String s : story){
            storyOut += s + " ";
        }

        //Adding proper ending through a full stop to the story.
        storyOut = storyOut.substring(0, storyOut.length()-1) + ".";

        return storyOut;
    }


    /**
     * Helper Method as a hotfix to resolve a bug that leaves emojis
     * in the story under certain circumstances.
     * @param story
     *  the already preparsed story will be parsed one more time.
     * @return
     *  hopefully a fully cleaned story.
     */
    private String varifyStory(String story){
        String storyOut = "";

        for (String s : story.split(" ")){
            if(isEmoji(s) || isExtendedEmoji(s)){
                System.out.println("Unfortunately the Story is invalid and needs some fixing..");
                storyOut = spliceEmoji(s);
            }
            else {
                storyOut += s + " ";
            }
        }
        return storyOut;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                      Find and replace Emoji without hurting the grammar.                      //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Function to splice emojis from strings including
     * their (indefinite) articles and adjectives.
     * @param pStory
     *  reveives a story string.
     * @return
     *  returns a cleaned story string.
     */
    private String spliceEmoji(String pStory){
        List<String> story      = new LinkedList<>(Arrays.asList(pStory.split(" ")));
        List<Integer> indexes   = new ArrayList<>();
        List<String> blackList  = Arrays.asList("the", "a");

        boolean hasEmoji = false;

        for(String word : story){
            replaceEmojiWithSC(word);
        }

        for(int i = story.size() - 1; i >= 0; --i){
            if (isEmoji(story.get(i))) {
                try {
                    while (!blackList.contains(story.get(i))) {
                        indexes.add(i);
                        --i;
                        hasEmoji = true;
                    }
                    if (hasEmoji && blackList.contains(story.get(i))) {
                        indexes.add(i);
                        hasEmoji = false;
                    }
                }
                catch (IndexOutOfBoundsException ioobe){
                    System.out.println("Something went wront trying to splice an emoji");
                    System.err.println(ioobe.getMessage());
                }

            } else if (isExtendedEmoji(story.get(i))) {
                try{
                    while (!blackList.contains(story.get(i))) {
                        indexes.add(i);
                        --i;
                        hasEmoji = true;
                    }
                    if (hasEmoji && blackList.contains(story.get(i))) {
                        indexes.add(i);
                        hasEmoji = false;
                    }
                }
                catch(IndexOutOfBoundsException ie){
                    System.err.println("There was a problem with the parsing of the story");
                    System.err.println(ie.getMessage());
                    return "Sorry I spilled ink over my notebook, could you ask for a different story?";
                }
            }
        }

        if(indexes.size() > 0){
            for (Integer index : indexes) {
                story.remove((int) index);
            }
        }

        String storyOut = "";
        int i = 0;
        for (String word : story){
            storyOut += word + " ";
        }
        return storyOut;
    }

    /**
     * Function to replace emojis with special characters such as
     * apostrophes and immediate commas and punctuation.
     * @param word
     */
    private void replaceEmojiWithSC(String word){
        char [] cWord = word.toCharArray();
        for(char c : cWord){
            String s = "\\u" + Character.toString(c);
            if(isEmoji(s)){
                word = word.replace( s, " ");
            }
        }

    }

    /**
     * Function using a regular expression to find emojis inside a sentence.
     * @param pSentence
     *  receives a sentence from a story.
     * @return
     *  returns true if a character in that sentence is an emoji, else false.
     */
    private boolean isEmoji(String pSentence){
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        for(String word : pSentence.split(" ")) {
            if(word.matches(regex)){
                return true;
            }
        }
        return false;
    }

    /**
     * Function using a regular expression to find emojis at the end of a sentence.
     * @param pSentence
     *  receives a sentence from a story.
     * @return
     *  returns true if a character in that sentence is an emoji, else false.
     */
    private boolean isExtendedEmoji(String pSentence){
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        for(String word : pSentence.split("")) {
            if(word.matches(regex)){
                return true;
            }
        }
        return false;
    }


    /////////////////////////////////////////
    public static void main(String[] args) {
        ItemGenerator na = new ItemGenerator();
        KnowledgeData knowledgeData = new KnowledgeData();

        StoryParser sp = new StoryParser("Donald Duck");
        System.out.println("\n\n" + sp.getStoryString());

        List<String> storyList = sp.getStoryLines();
        for(String line : storyList){
            System.out.println(line);
        }
        //List<List<String>> tuples   = na.generateStoryItems(sp.storyString, sp.storyLines)

        /*
        String[] keyWords = {"simpsons", "manhatten", "middle earth", "judgement", "fun", "crisis", "happiness", "science fiction", "history", "batman"};
        int k = 1;
        for (int i = 0; i < 10; i++){
            String keyWord = keyWords[i];
            System.out.println("Searched keyword is: " + keyWord + "\n\n");
            for (int j = 0; j < 10; j++) {
                String characterName = knowledgeData.getCharacterFromAttribute(keyWord);
                System.out.println(k + ". \tCharactername is " + characterName);
                sp = new StoryParser(characterName);
                System.out.println("The story is: " + sp.getStoryLines());
                k++;
            }
        }*/
    }
}
