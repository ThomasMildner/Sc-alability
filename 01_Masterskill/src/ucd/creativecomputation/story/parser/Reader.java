package ucd.creativecomputation.story.parser;

import java.io.*;
import java.util.Objects;

/**
 * Class representing a reader that reads a story from a file or returns a sample story.
 */
public class Reader {

    private String story = null;

    /**
     * Constructor receiving a file from which it will extract a story.
     * @param storyFile
     *  receives a story file from which to extract a story from.
     */
    public Reader(File storyFile) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(storyFile));
        } catch (FileNotFoundException e) {
            System.err.println("File could not be read or did not exist.");
            System.err.println(e.getMessage());
        }

        try {
            story = Objects.requireNonNull(reader).readLine();
        } catch (IOException e) {
            System.err.println("A reader could not be created.");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Default constructor that sets a sample story.
     */
    public Reader() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("src/ucd/creativecomputation/story/parser/samplestories/sample-story.txt"));
        } catch (FileNotFoundException e) {
            System.err.println("File could not be read or did not exist.");
            System.err.println(e.getMessage());
        }

        try {
            story = Objects.requireNonNull(reader).readLine();
        } catch (IOException e) {
            System.err.println("A reader could not be created.");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Function to return the story as a String.
     * @return
     *  returns the story as a String.
     */
    public String getStory(){
        return story;
    }


}
