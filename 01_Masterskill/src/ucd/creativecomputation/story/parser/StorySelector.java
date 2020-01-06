package ucd.creativecomputation.story.parser;

public class StorySelector {
    private String story;

    public StorySelector (String attribute) {
            story = getStoryByAttribute(attribute);
    }

    public String getStory() {
        return story;
    }

    private String getStoryByAttribute(String attribte) {
        System.err.println("Not yet implemented.");
        return "No Story Selected";
    }
}
