package ucd.creativecomputation.server;

import ucd.creativecomputation.alexa.NarratorStreamHandler;;

public class InteractiveSection {

    private BlackboardRetriever br      = null;

    //private String tmpCharacterName = "";
    private String characterNameA   = "";
    private String characterNameB   = "";

    private boolean allNamesSet     = false;
    private boolean isCharASet      = false;
    private boolean isCharBSet      = false;
    private BlackboardRetriever.DecisionMode decisionMode = BlackboardRetriever.DecisionMode.EMPTY;

    public InteractiveSection(){
        br = NarratorStreamHandler.BLACKBOARD_RETRIEVER;
        decisionMode = br.getDecisionMode();
    }


    private void checkAvailableNames(String characterName) {
        CharacterboardRetriever cr = new CharacterboardRetriever();
        characterNameA = cr.getCharacterA();
        characterNameB = cr.getCharacterB();

        if (isCharASet && isCharBSet) {
            System.out.println("IS:Both names have been set to: \n"
                + "Char A: "  + characterNameA + " "
                + "Char B: "  + characterNameB);
            allNamesSet = true;
        }
        else if(characterNameA.equals("")) {
            System.out.println("IS:CHAR_A set to: " + characterName);
            //characterNameA = characterName;
            cr.setCharacter(characterName);
            isCharASet = true;
        }
        else if(characterNameB.equals("")) {
            System.out.println("IS:CHAR_B set to: " + characterName);
            //characterNameB = characterName;
            cr.setCharacter(characterName);
            isCharBSet  = true;
        }
    }

    public boolean isNamingPossible(String characterName) {
        if (allNamesSet || (isCharASet && isCharBSet)) {
            System.out.println("IS:All names have already been set.");
            return false;
        }
        else {
            System.out.println("IS:Naming is possible.");
            checkAvailableNames(characterName);
            return true;
        }
    }

    private String getParsedString(){
        CharacterboardRetriever cr = new CharacterboardRetriever();
        String currentSection   = br.getSentence();
        String parsedString     = "";
        String[] tokens         = currentSection.split(" ");
        String characterA = cr.getCharacterA(); //characterNameA
        String characterB = cr.getCharacterB(); //characterNameB

        //TODO: Add more cases or better regex.
        for (int i = 0; i < tokens.length; i++) {

            switch (tokens[i].trim()) {
                case "{X}":
                    //System.out.println("Need character for X");
                    tokens[i] = characterA;
                    break;
                case "{X}.":
                    //System.out.println("Need character vor X.");
                    tokens[i] = characterA + ".";
                    break;
                case "{X}'s":
                    //System.out.println("Need character vor X's");
                    tokens[i] = characterA + "'s";
                    break;
                case "{Y}":
                    //System.out.println("Need character for Y");
                    tokens[i] = characterB;
                    break;
                case "{Y}.":
                    //System.out.println("Need character for Y.");
                    tokens[i] = characterB + ".";
                    break;
                case "{Y}'s":
                    //System.out.println("Need character vor Y's");
                    tokens[i] = characterB + "'s";
                    break;
            }

            //System.out.println("Current item: " + tokens[i]);
            parsedString += tokens[i] + " ";
        }

        return parsedString.trim();
    }

    private String getChangePrompt(){
        String[] prompts = {"I think this story could use some change, do you agree?"
                ,"Let's mix it up a little, what do you think?"
                ,"Do you think we could spice things up a notch?"
                ,"Let's introduce some change, should we?"};
        return prompts[NarratorStreamHandler.RANDOM.nextInt(prompts.length)];
    }

    private String getIntroductionPrompt(){
        String [] intros = {" I think we should introduce a new character to the story. Please give me the name of someone you would like to add."
                ,"Let's spice things up by introducing a new character. Be so kind and tell me a good name and I will add them to our story?"
                ,"This story could use another character. Please give me a name you would like to hear in our story?"};
        return intros[NarratorStreamHandler.RANDOM.nextInt(intros.length)];
    }

    private String getEnding(){
        return "good bye.";
    }

    public String buildInteractiveResponse(){
        String section = getParsedString();
        String tail;
        decisionMode = br.getDecisionMode();

        if(decisionMode.equals(BlackboardRetriever.DecisionMode.CHANGE)) {
            tail = getChangePrompt();
        }
        else if(decisionMode.equals(BlackboardRetriever.DecisionMode.INTRODUCTION)) {
            tail = getIntroductionPrompt();
        }
        else if(decisionMode.equals(BlackboardRetriever.DecisionMode.ENDING)) {
            tail = getEnding();
        }
        else {
            tail = "";
        }

        return section + " " + tail;
    }

    public static void main(String[] args) {
        InteractiveSection is = new InteractiveSection();
        is.isNamingPossible("MIKE");
        //String sampleSentence = "Hello, I am {X} and hate {Y}. {Y} likes {X}.";

        System.out.println(is.buildInteractiveResponse());


        /*int sections = 5;

        while(sections > 0) {
            is.isNamingPossible("Hans");
            is.isNamingPossible("Anne");
            is.isNamingPossible("Thomas");
            System.out.println(is.buildInteractiveResponse());
            sections -= 1;
        }*/
        //System.out.println("Char A: " + is.getCharacterNameA() + " Char B: " + is.getCharacterNameB());
    }
}

