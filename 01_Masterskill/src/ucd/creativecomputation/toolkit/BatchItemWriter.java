package ucd.creativecomputation.toolkit;

import ucd.creativecomputation.story.ItemGenerator;
import ucd.creativecomputation.story.StoryParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Class to generate a batch file of itemized stories to test.
 */
public class BatchItemWriter {

    private List<Integer> identifier        = new ArrayList<Integer>();
    private List<String> characterNames     = new ArrayList<String>();
    private List<List<List<String>>> items  = new LinkedList<>();
    private List<String> storyList          = new ArrayList<String>();
    private ItemGenerator itemGenerator     = new ItemGenerator();
    private StoryParser storyParser;

    private String inputPath = "/Users/thomasmildner/Documents/03_Studium/03_UCD/02_Research/" +
            "01_Computational-Creativity/generation/multicharacter_stories.txt";

    private Scanner scanner;
    private PrintWriter printWriter;

    public BatchItemWriter() {
    }

    private void setStoryList() throws FileNotFoundException {
         scanner = new Scanner(new File(inputPath));
         int counter = 100;
         while (scanner.hasNext()) {
             storyList.add(scanner.nextLine());
             //counter--;
         }
    }

    private void setItems() throws FileNotFoundException {

        setStoryList();
        for(String story : storyList) {
            storyParser                     = new StoryParser(story, true);
            String currentStory             = storyParser.getStoryString();
            List<String> currentStoryLines  = storyParser.getStoryLines();
            items.add(itemGenerator.generateStoryItems(currentStory, currentStoryLines));
        }
    }

    private void setCharacterNames(){
        for (String c : StoryFiles.STORY_FILE_NAMES){
            characterNames.add(c.split("_")[1]
                    .replaceAll("-", " "));
        }
        System.out.println(characterNames);
    }

    private void parseStories(){
    }

    private boolean isDistinctID(int id){
        if (!identifier.contains(id)){
            identifier.add(id);
            return true;
        }
        return false;
    }

    private void batchWriteItems() throws FileNotFoundException, UnsupportedEncodingException {
        int counter = 1;
        printWriter = new PrintWriter("/Users/thomasmildner/Documents/03_Studium/03_UCD/02_Research/01_Computational-Creativity/sampleStories.txt", "UTF-8");


        for (List<List<String>> item : items){
            printWriter.write("#" + counter + "\n");
            for(List<String> sentence : item){
                for(int i = 0; i < sentence.size(); i++) {
                    printWriter.write(sentence.get(i) + "%");
                }
                printWriter.write("˜\n");
            }
            counter++;
        }
        printWriter.close();
    }


    public static void main(String[] args) {
        BatchItemWriter biw = new BatchItemWriter();
        try {
            biw.setItems();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            biw.batchWriteItems();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
