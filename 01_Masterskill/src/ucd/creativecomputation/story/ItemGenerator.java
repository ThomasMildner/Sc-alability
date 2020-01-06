package ucd.creativecomputation.story;

import ucd.creativecomputation.knowledgebase.nocobjects.character.NocCharacter;

import java.util.*;

/**
 * Class to export stories for a NAO Robot to say. Each line consists of an action and the actual
 * sentence separated with a "|" as a delimiter.
 * @author
 * Thomas Mildner
 */
public class ItemGenerator {

    public static List<NocCharacter> storyCharacterList = new ArrayList<NocCharacter>();

    private IntroductionGenerator introductionGenerator;
    private List<String> storyLines         = new ArrayList<String>(); //TODO: Link to StoryParser storyLines
    private List<String> nocCharacter       = new ArrayList<String>();

    private List<String> introductionLines = new LinkedList<>(Arrays.asList(
            "What do you think about ",
            "This story should feature ",
            "Every good story contains ",
            "And here comes ",
            "Here we have an oddball ",
            "When was the last time you heard about ",
            "There's the one and only ",
            "Open the curtain for ",
            "What's your opinion on ",
            "Everyone held their breath for ",
            "Let's see what happens if we add ",
            "Let's bring in ",
            "This story would get quite the turn with the appearance of "
    ));

    /**
     * Constructor for feature improvement to design one item-object class, that allows to set
     * up the Blackboard more easily.
     * @param storyString
     *  The unparsed story text.
     * @param storyLines
     *  The parsed story sentences, as a list of sentences.
     */
    public ItemGenerator(String storyString, List<String> storyLines) {
        //generateStoryItems(storyString, storyLines);
    }

    /**
     * Empty Constructor for basic functionality.
     */
    public ItemGenerator(){
        //Empty constructor
    }

    /**
     * Function to generate items to load onto to the Blackboard structure. These items
     * are constructed as List of List of strings. Each List of String consists of a
     * sentence from the sentence, a string with all actions from that sentence in them,
     * and a string with all characters from that sentence. Thus each List of strings
     * represents one sentence of the story with all the necessary data for the enactment.
     * @param storyString
     *  The story as one long string. Due to the layout of the story files and the previous
     *  parsing of the story-elements, the method {@see getActions} receives the entire
     *  storystring and returns a list of actions for each sentence.
     * @param storyLines
     *  A list of all sentences as individual strings in a list. These are the same
     *  sentences that are in the {@param storyString} but already parsed. The sentences
     *  are used to put them in the output list and to retrieve the characters from.
     * @return
     *  A list of lists of strings that constist of a sentence from the sentence,
     *  a string with all actions from that sentence in them, and a string with all
     *  characters from that sentence.
     */
    public List<List<String>> generateStoryItems(String storyString, List<String> storyLines){

        List<String> lines          = null;
        List<String> actions        = null;
        String tmpCharacter         = "";
        nocCharacter                = new ArrayList<>();
        List<List<String>> items    = new ArrayList<>();
        List<Integer> introIndex    = new ArrayList<>();

        // Check if the story is not null, throws NullPointerException if it is unexpectedly.
        if(storyString != null){
            actions = getActionListV2(storyString);
            actions.remove(0);
            actions.remove(0);
        }
        else{
            System.err.println("The storyString was empty.");
            throw new NullPointerException();
        }

        // Check if the story lines list is filled, otherwise throws a NullPointerException.
        if(storyLines != null){
            lines = storyLines;
        }
        else {
            System.err.println("The storyLines where empty.");
            throw new NullPointerException();
        }

        // For every Line in the story, we create a new item. New characters get an introduction item.
        for(int i = 0; i < lines.size(); i++){
            List<String> item;
            List<String> tmpChar = new ArrayList<>();

            // The beginning of the story should always just be narrated. No extras here.
            // All characters introduced here are being stored in teh nocCharacter list.
            if(i == 0){
                tmpCharacter = getCharacterFromSentence(lines.get(i));
                tmpChar.addAll(Arrays.asList(tmpCharacter.split(" \\| ")));
                nocCharacter.addAll(tmpChar);

                item = itemize(lines.get(i), getCharacterFromSentence(lines.get(i)), " ");

                items.add(item);
            }
            // The last sentence will also just be said. In most cases it just spells 'The end.'
            else if(i == lines.size() - 1) {

                item = itemize(lines.get(i));

                items.add(item);
            }
            // For every other sentence in the story we take a look at the characters in that sentence
            // and check, weather it is a new one. If so we add an additional item for their introduction
            // if not, we simply add the story sentence, the actions and all characters within that sentence.
            else {
                tmpCharacter = getCharacterFromSentence(lines.get(i));
                tmpChar.addAll(Arrays.asList(tmpCharacter.split(" \\| ")));
                String characterOne = tmpChar.get(0).trim();
                String characterTwo = "";
                String introduction = "";

                if(tmpChar.size() > 1){
                    characterTwo = tmpChar.get(1).trim();
                }

                // Since we usually have more than one nocCharacter in a sentence, this statement checks
                // weather the first nocCharacter was already mentioned. Else, will be stored in a list.
                if (!nocCharacter.contains(characterOne) && characterOne.contains(" ")){

                    //introduction = getRandomIntroduction(characterOne);
                    introduction = getAnyIntroduction(characterOne);

                    addCharacter(characterOne);

                    item = itemize(introduction, characterOne);

                    items.add(item);
                }
                // Check weather a second nocCharacter exists. Only do so, if the first was already introduced.
                // If it was not introduced yet, add them to the nocCharacter list and add an item to introduce them.
                else if(tmpChar.size() > 1 && !nocCharacter.contains(characterTwo) && characterTwo.contains(" ")){

                    //introduction = getRandomIntroduction(characterTwo);
                    introduction = getAnyIntroduction(characterTwo);

                    addCharacter(characterTwo);

                    item = itemize(introduction, characterTwo);

                    items.add(item);
                }

                // For every sentence, where the actions are not empty, fill a regular item with a
                // sentence, an action and the characters mentioned devided with a '|'.
                if (!actions.get(i - 1).isEmpty()) {
                    item = itemize(lines.get(i), getCharacterFromSentence(lines.get(i)), actions.get(i - 1));
                }
                // If there's no action in the sentence found, instead add a space ' '.
                else {
                    item = itemize(lines.get(i), getCharacterFromSentence(lines.get(i)));
                }

                items.add(item);
            }
        }
        return items;
    }

    /**
     * Helper method to add characters to the character lists to reduce code duplication.
     * @param character
     */
    private void addCharacter(String character) {
         nocCharacter.add(character);
         storyCharacterList.add(new NocCharacter(character));
    }

    private String getAnyIntroduction(String character) {
        introductionGenerator = new IntroductionGenerator(character);
        return "{ " + introductionGenerator.getIntroduction();
    }

    /**
     * Helper method to create a weaponized introduction for a nocCharacter.
     * For example: 'Prepare to be beat black-and-blue with a Quack Fu!' - for Donald Duck.
     * These create an alternative to the general introduction of a name.
     * @return
     *  returns a weaponized introduction for a nocCharacter with a prepended '{' as a
     *  flag for Nao.
     */
    private String getWeaponizedIntroduction(String character) {
        introductionGenerator = new IntroductionGenerator(character);
        return "{ " + introductionGenerator.getBoomIntroduction();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //        Prepare raw story text and actions for the individual items of the Blackboard.         //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Helper method to retrieve a random introduction for a nocCharacter.
     * @return
     *  returns a random introduction from {@see introductionLines}
     *  to be used to introduce a new characater in the story. A '-' is prepended
     *  as a flag for Nao.
     */
    private String getRandomIntroduction(String character){
        Random rnd = new Random();
        String introduction = "";

        if(introductionLines.size() > 1){
            int rndIntro = rnd.nextInt(introductionLines.size() - 1);
            introduction = introductionLines.get(rndIntro);
            introductionLines.remove(rndIntro);
        }
        else {
            introduction = introductionLines.get(0);
        }

        return "- " + introduction + " " + character;
    }

    /**
     * Helping method to construct an item for the function {@see generateStoryItems}.
     * @param sentence
     *  A story sentence
     * @param character
     *  Characters popping up in that sentence.
     * @param action
     *  Actions occuring between the nocCharacter. Can be left out, then an empty string
     *  will be placed instead.
     * @return
     *  Returns an Item consisting of a sentence, one or more characters, and an action.
     */
    private List<String> itemize (String sentence, String character, String action) {
        List<String> item = new ArrayList<>();

        item.add(sentence);
        item.add(character);
        item.add(action);

        return item;
    }

    private List<String> itemize (String sentence, String character) {
        String action = " ";

        return itemize(sentence, character, action);
    }

    private List<String> itemize (String sentence) {
        String character = " ";

        return itemize(sentence, character);
    }

    /**
     * Filter all Characters from a sentence to add them to the Blackboard later on.
     * The characters are written into a String and separated with '|'s for later use
     * of the robot to act on.
     * @param sSentence
     *  A sentence from the story, which contains the characters.
     * @return
     *  A String consisting of all characters from a sentence. They are separated
     *  through '|'s.
     */
    private String getCharacterFromSentence(String sSentence){
        List<String> sentence   = Arrays.asList(sSentence.split(" "));
        String tmpChar = "";
        List<String> character  = new ArrayList<>();

        for (int i = 1; i < sentence.size(); i++){
            String word = sentence.get(i);

            if (Character.isUpperCase(word.charAt(0))){
                tmpChar +=  word.replaceAll("'s", "")
                                .replaceAll("[^\\s\\w]*", "")+" ";

                if(i == sentence.size()-1){
                    character.add(tmpChar);
                }
            }
            else {
                character.add(tmpChar);
                tmpChar = "";
            }
        }
        character.removeAll(Arrays.asList("", null));

        String result = character.toString();
        result = result.replaceAll("\\[", "")
                .replaceAll("\\]", "")
                .replaceAll(",", "|");

        return result;
    }

    /**
     * Improved Method to retrieve a complete list of the actions from a sentence.
     * Insetad of leaving blanks, this method fills in '0' for sentences, that do
     * not come with an action. This enables easier combining of the two lists
     * later on.
     * @param storyString
     *  Receives a story string, to retrieve the actions from.
     * @return
     *  Returns a list of actions.
     */
    public List<String> getActionListV2(String storyString){
        List<String> actions    = new ArrayList<>();
        List<String> storyParts = new LinkedList<>(Arrays.asList(storyString.split("\t")));
        storyParts.remove(storyParts.size() - 1);

        String action;
        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";

        int counter = 0;
        while (counter < 3) {
            storyParts.remove(counter);
            counter++;
        }

        for (String s : storyParts){
            if( s.contains("|")){
                action = Arrays.asList(s.split("\\|")).get(0);
                if(action.contains("(")){
                    action = action.substring(action
                            .indexOf("(") + 1, action.indexOf(")"))
                            .replace("or: ", "")
                            .replaceAll(" ", "_")
                            .replaceFirst(regex, "A")
                            .replaceFirst(regex, "B");
                }
                actions.add(action);
            }
            else {
                actions.add("0");
            }
        }

        actions.remove(actions.size() - 1);
        List<String> preActions = getPreActionList(storyString);

        Collections.reverse(actions);
        Collections.reverse(preActions);

        List<String> out = new ArrayList<>();

        int difference = actions.size() - preActions.size();

        for (int i = 0; i < difference; i++){
            preActions.add("0");
        }

        int current = 0;

        for(String a : actions){
            if (a.equals("0")){
                out.add(a);
            }
            else {
                out.add(preActions.get(current));
            }
            current ++;
        }

        Collections.reverse(out);
        return out;
    }

    /**
     * Method to generate a preprocessed list of actions.
     * This list helps {@see getActionListV2} to get the actual
     * list of actions matched to the sentences they appear in.
     * @param storyString
     *  Receives a story string, to retrieve the actions from.
     * @return
     *  Returns a list of pre-actions.
     */
    private List<String> getPreActionList(String storyString){
        List<String> actions    = new ArrayList<>();
        List<String> storyParts = new LinkedList<>(Arrays.asList(storyString.split("\t")));
        storyParts.remove(storyParts.size() - 1);

        String actionString     = storyParts.get(0);
        actionString            = actionString.substring(actionString.indexOf("[") + 1, actionString.indexOf("]"));
        actions                 = Arrays.asList(actionString.split(":"));

        List<String> tmp;
        List<String> out = new ArrayList<>();

        for (String action : actions){
            tmp =  Arrays.asList(action.split(" "));
            if(tmp.size() > 1){
                action = tmp.get(1);
                out.add(action);
            }
            else {
                action = tmp.get(0);
                out.add(action);
            }
        }

        return out;
    }

    /**
     * Method to return a list of all NOC characters from a story.
     * @return
     *  returns a list of all NOC characters.
     */
    public List<String> getNocCharacter(){
        return nocCharacter;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //           Old Code that is working but replaced with alternative, better versions.            //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Function to generate a prepared story consisting of an action and the actual
     * sentence separated with a "|" as a delimiter.
     * @param pStory
     *  Receives a story string, which will be regenerated to match a storytelling parser of the NAO Robot
     *  by {@author Philipp Wicke}.
     * @return
     *  Returns a regenerated story string,  which will be regenerated to match a storytelling parser of the NAO Robot
     *  by {@author Philipp Wicke}.
     */
    public String generateActionStoryTupels(String pStory){
        List<String> lines      = storyLines;
        List<String> actions    = getActionList(pStory);
        List<String> tuples     = new ArrayList<>();
        String actionStoryOut   = "";

        for(int i = 0; i < lines.size(); i++){
            if(i == 0){
                tuples.add(lines.get(i));
            }
            else if(i == lines.size() - 1) {
                tuples.add(lines.get(i) + "..");
            }
            else {
                tuples.add(actions.get(i - 1) + "|" + lines.get(i));
            }
        }

        for(String s : tuples){
            actionStoryOut += s.replaceAll(" \\.", "") + "\n";
        }
        actionStoryOut += "#";
        return actionStoryOut;
    }

    /**
     * Function to get the actions from a story string containing the entire story.
     * Due to the construction of the previously implemented parser and the way
     * the sentences lie inside a storyfile, with the actions to each sentence
     * before the actual sentence, this function parses over the same content again,
     * to filter for only the actions before every sentence.
     * @param pStory
     *  Receives a story string, to retrieve the actions from.
     * @return
     *  Returns a list of actions.
     */
    private List<String> getActionList(String pStory){
        List<String> story  = new LinkedList<>(Arrays.asList(pStory.split("\t")));
        String actionString = story.get(0);
        List<String> actionList;

        actionString = actionString
                .replaceAll(".*\\b\\:\\[", "")
                .replaceAll("]", "");

        actionList = new ArrayList<>(Arrays.asList(actionString.split(":")));

        for(int i = 0; i < actionList.size(); i++){
            if(actionList.get(i).split(" ").length > 1){
                actionList.set(i, actionList.get(i).split(" ")[1]);
            }
        }
        return actionList;
    }


    ///////////////////////////
    /**
     * Main Function that executes the {@see ItemGenerator} class in order do generate a set of 100 prepared stories
     * and write them into an output file.
     * @param args
     */

    public static void main(String[] args) {
        ItemGenerator na = new ItemGenerator();
        StoryParser sp = new StoryParser("Sauron");

        List<List<String>> items = na.generateStoryItems(sp.getStoryString(),sp.getStoryLines());
        for(List<String> item : items){
            System.out.println(item);
        }

    }
}
