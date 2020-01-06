package ucd.creativecomputation.toolkit.retriever;


import ucd.creativecomputation.knowledgebase.nocobjects.KnowledgeData;
import ucd.creativecomputation.toolkit.StoryFiles;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static ucd.creativecomputation.toolkit.StoryFiles.*;

/**
 * Class to retrieve a character story-file from a host website given a character name. The class uses the
 * {@see StoryParser} to check and recieve a valid file name to look up.
 * @author Thomas Mildner
 */
public  class URLStoryFileRetriever {
    private List<String> storyList = new ArrayList<>();
    private String urlString;

    /**
     * Method retrieves a storyfile from http://www.callmethomas.com/hidden/stories/ by name.
     * If it is unable to fina a story, it will throw an exception.
     * @param pName
     *  The name of the storyfiles character.
     * @return
     *  A random story around the character.
     */
    public String getStoryByName(String pName){
        String name = pName;

        if(name.contains(" ")){
            name = name.replaceAll(" ", "-");
        }
        if(name.contains(".")){
            name = name.replaceAll(".", "");
        }
        if(name.contains("\"")){
            name = name.replaceAll("\"", "");
        }

        try {
            retrieveStories(name);
        } catch (IOException e) {
            System.err.println("Could not find any stories for: " + name);
            System.err.println(e.getMessage());
        }
        System.out.println("StoryList created.");

        String currentStory = pickRandomStory();
        int counter = 0;
        while(!checkCharacter(currentStory, name)){
            counter++;
            currentStory = pickRandomStory();
            if(counter == 10){                       // after 10 tries, a random story containing
                return pickRandomStory();            // the character will be chosen and used instead.
            }
        }
        return currentStory;
    }

    /**
     * Function tries to connect to 'tintin.ucd.ie/thomas/stories' in oder
     * to access a storyfile using the getStoryFileByName method. The stories inside
     * that file is then being stored a Story List.
     * @see StoryFiles#getStoryFileByBName(String)
     * @param pName
     *  the name of the character, whose storyfile will be tried to download.
     */
    private void retrieveStories(String pName) throws IOException {
        String fileName = getStoryFileByBName(pName);

        // Setting website-URL hosting the character-story files.
        urlString = "http://www.callmethomas.com/hidden/stories/"; // TODO: Change to ../new_stories/

        URL fileURL = new URL(urlString);

        try {
            fileURL = new URL(fileURL + fileName);
            System.out.println("Story is retrieved from: " + fileURL);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader in = null;

        try {
            in = new BufferedReader(
                    new InputStreamReader(fileURL.openStream()));
        }
        catch (IOException e) {
            System.err.println("Could not find the storyfile for " + pName + ".");
            System.err.println(e.getMessage());
        }

        String inputLine;
        assert in != null;
        while ((inputLine = in.readLine()) != null){
            storyList.add(inputLine);
        }
        in.close();
    }

    /**
     * Function picks a random story from the story file and returns it.
     * @return
     *  returns a random story from the storyfile.
     */
    private String pickRandomStory(){
        Random rand = new Random();
        int max = storyList.size();
        int randomStory = 0;
        try{
            randomStory = Math.abs(rand.nextInt(max));
        } catch (NullPointerException e){
            System.err.println("The Story List could not be filled.");
            System.err.println(e.getMessage());
        }
        return storyList.get(randomStory);
    }

    /**
     * Function hot fixing issue to return only main character stories.
     * @param pStory
     *  receives a story string.
     * @param pName
     *  recieves a character name, the story should contain.
     * @return
     *  returns true if the character name is actually the main-character of a story, else false.
     */
    private boolean checkCharacter(String pStory, String pName){
        List<String> storyParts = new LinkedList<>(Arrays.asList(pStory.split("\t")));

        // Reconstruct compound Names.
        String characters = storyParts.get(1)
                .replaceAll("-", "");

        String name = "";

        // Make name characters camelcase to match names in the file
        if(pName.contains("-")){
            String [] tmpName = pName.split("-");
            for (String aTmpName : tmpName) {
                name += aTmpName.substring(0, 1).toUpperCase()
                        + aTmpName.substring(1);
            }
            return characters.contains(name);
        }
        System.out.println(characters.replaceAll("-", "")
                .contains(pName.substring(0,1)
                        .toUpperCase()
                        + pName.substring(1)));

        return characters.replaceAll("-", "")
                .contains(pName.substring(0,1)
                .toUpperCase()
                + pName.substring(1));
    }


    //////////////////////////////////////////////////////////////////////
    // larger test to check for any mistakes in retrieving a story-file //
    //////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        KnowledgeData knowledgeData = new KnowledgeData();
        URLStoryFileRetriever sr = new URLStoryFileRetriever();

        String[] keyWords = {"war", "drama", "betraying", "crime", "author", "wisdom", "strength", "romance", "steven fry", "comedy"};
        int k = 1;
        for (int i = 0; i < 10; i++){
            String keyWord = keyWords[i];
            System.out.println("Searched keyword is: " + keyWord + "\n\n");
            for (int j = 0; j < 10; j++) {
                String character = knowledgeData.getCharacterFromAttribute("magneto");

                System.out.println("\t" + k + ". \t" +"The character is: " + character);
                System.out.println("\t" + sr.getStoryByName(character) + "\n");
                k++;
            }
        }
    }
}