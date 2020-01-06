package ucd.creativecomputation.story;

import ucd.creativecomputation.toolkit.StoryFiles;

import java.util.List;

/**
 * Class to slice a story into individual sentences. This has been in use
 * for the ItemGenerator but is currently not necessary.
 *
 * @author
 * Thomas Mildner
 */
public class StorySlicer {
    private List<String> storyList;
    private StoryParser parsedStory;
    private int storyIndex = 0;


    public StorySlicer() {
        parsedStory = new StoryParser(StoryFiles.getRandomNameFromFile());
        parsedStory.getStory();
        storyList = parsedStory.getStoryLines();
    }

    public String getCurrentSentence(){
        if(storyIndex == 0){
            increaseStoryIndex();
            return storyList.get(0);
        }
        increaseStoryIndex();
        return storyList.get(storyIndex);
    }

    public void increaseStoryIndex(){
        storyIndex += 1;
    }

    public List<String> getStoryList(){
        return storyList;
    }

    /////////////////////////////////
    public static void main(String[] args) {
        StorySlicer sl = new StorySlicer();
        for (int i = 0; i < sl.storyList.size() - 1; i++){
            System.out.println(i + ": " + sl.getCurrentSentence());
        }
    }
}
