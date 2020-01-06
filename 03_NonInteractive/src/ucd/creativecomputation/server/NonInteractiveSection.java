package ucd.creativecomputation.server;

import ucd.creativecomputation.alexa.NarratorStreamHandler;
import static ucd.creativecomputation.alexa.NarratorStreamHandler.BLACKBOARD_RETRIEVER;

public class NonInteractiveSection {

    private String currentSection   = "";
    private String lastSentence     = "";

    public NonInteractiveSection(){

        // Check if Blackboard is empty. If so, prompt the audience to create the story first.
        if(!BLACKBOARD_RETRIEVER.isBlackboardEmpty()){
            currentSection  = BLACKBOARD_RETRIEVER.getSentence();
            lastSentence    = BLACKBOARD_RETRIEVER.getLastSentence();
            addCharacters();
        }
        else {
            // Render a reprompt since no story has been created yet.
            System.err.println("There was an issue with receiving the story.");
        }
    }

    private void addCharacters(){
        String regexA = "\\{X\\}";
        String regexB = "\\{Y\\}";
        String characterA = "Marge Simpson";
        String characterB = "Barney Gumbles";
        currentSection = currentSection.replaceAll(regexA, characterA).replaceAll(regexB, characterB);
        lastSentence = lastSentence.replaceAll(regexA, characterA).replaceAll(regexB, characterB);
        //System.out.println("updated section: " + currentSection);
    }

    private String promptUtterance(){

        String[] prompts = {"Would you like to continue?",
                            "Shall I proceed?",
                            "Should I go on?",
                            "Would you like to hear more?",
                            "Do you want more?",
                            "Should I continue?",
                            "Do you want to proceed?"};

        return prompts[NarratorStreamHandler.RANDOM.nextInt(prompts.length)];
    }

    public String buildNonInteractiveResponse(){
        if(currentSection.equals(lastSentence)){
            BLACKBOARD_RETRIEVER.resetReadProcess();
            return currentSection + " Thank you, you have been a wonderful listener.";
        }
        return currentSection + " " + promptUtterance();
    }


    public static void main(String[] args) {
        NonInteractiveSection nis = new NonInteractiveSection();
        System.out.println(nis.buildNonInteractiveResponse());
    }
}
