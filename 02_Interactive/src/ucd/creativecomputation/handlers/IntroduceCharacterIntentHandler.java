package ucd.creativecomputation.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.request.RequestHelper;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;

import ucd.creativecomputation.alexa.NarratorStreamHandler;
import ucd.creativecomputation.server.BlackboardRetriever;
import ucd.creativecomputation.server.CharacterboardRetriever;
import ucd.creativecomputation.server.InteractiveSection;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class IntroduceCharacterIntentHandler implements RequestHandler {


    private static final String ACTOR_NAME_SLOT             = "actornameslot";
    private static final String FIRST_NAME_SLOT             = "firstnameslot";
    private static final String FICTIONAL_CHARACTER_SLOT    = "fictionalcharacterslot";

    private InteractiveSection interactiveSection           = null;
    private Optional<String> characterName                  = null;
    private String characterA                               = "CHAR_A";
    private String characterB                               = "CHAR_B";
    public IntroduceCharacterIntentHandler(){
        //interactiveSection = new InteractiveSection();
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("IntroduceCharacterIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        interactiveSection = new InteractiveSection();
        String speechText;

        if (isNamingPossible()) {
            setCharacterName(input);
            System.out.println("SH:Name " + characterName.get() + " is now set.");
            speechText = getRandomIntroduction(characterName.get()) + " " + interactiveSection.buildInteractiveResponse();
        }
        else{
            System.out.println("SH:All names are set. Current decisionmode: " + NarratorStreamHandler.BLACKBOARD_RETRIEVER.getDecisionMode());
            speechText = "The story does not require another character at the moment. Would you like to continue?";
        }

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .withReprompt(getRandomReprompt())
                .build();
    }

    private String getRandomReprompt() {
        String[] reprompts = {"Sorry, I didn't get that, could you say your desired name again?"
                ,"Sorry I did could not understand you properly. Please repeat the name you wish to be introduced."};
        return reprompts[NarratorStreamHandler.RANDOM.nextInt(reprompts.length)];
    }

    private String getRandomIntroduction(String name){
        String[] introductions = {"Alright, I think " + name + " will make a great addition to our story."
                ,"Thank you. " + name + " will be a perfect fit for our little story."
                , "Very well. Let's see how " + name + " will impact the story."
                , "Great! Let's add " + name + " to our story and see what happens."};
        return introductions[NarratorStreamHandler.RANDOM.nextInt(introductions.length)];
    }


    /**
     * Function to find correct slot type and set characterName accordingly.
     * @param input
     *  current HandlerInput.
     */
    private void setCharacterName(HandlerInput input){

        RequestHelper requestHelper         = RequestHelper.forHandlerInput(input);


        if (requestHelper.getSlotValue(ACTOR_NAME_SLOT).isPresent()) {
            characterName = requestHelper.getSlotValue(ACTOR_NAME_SLOT);                     // add attribute map to current session.
            interactiveSection.isNamingPossible(characterName.get());
            //addCharacter(attributesManager, characterName.get());                            // add character to interactive session.
            System.out.println("SH:Current attribute for characternameslot is: " + characterName.get());
        }
        if (requestHelper.getSlotValue(FIRST_NAME_SLOT).isPresent()) {
            characterName = requestHelper.getSlotValue(FIRST_NAME_SLOT);                    // set the charactername according to the provided user input.
            interactiveSection.isNamingPossible(characterName.get());
            //addCharacter(attributesManager, characterName.get());                           // add character to interactive session.
            System.out.println("SH:Current attribute for firstnameslot is: " + characterName.get());
        }
        if (requestHelper.getSlotValue(FICTIONAL_CHARACTER_SLOT).isPresent()) {
            characterName = requestHelper.getSlotValue(FICTIONAL_CHARACTER_SLOT);           // set the charactername according to the provided user input.
            interactiveSection.isNamingPossible(characterName.get());
            //addCharacter(attributesManager, characterName.get());                           // add character to interactive session.
            System.out.println("SH:Current attribute for fictionalnameslot is: " + characterName.get());
        }
    }

    private boolean isNamingPossible() {
        System.out.println("DOES DECISION MODE " + NarratorStreamHandler.BLACKBOARD_RETRIEVER.getDecisionMode() + " = " + BlackboardRetriever.DecisionMode.INTRODUCTION + "?");
        //return NarratorStreamHandler.BLACKBOARD_RETRIEVER.getDecisionMode() == BlackboardRetriever.DecisionMode.INTRODUCTION;
        if (NarratorStreamHandler.BLACKBOARD_RETRIEVER.getDecisionMode() == BlackboardRetriever.DecisionMode.INTRODUCTION) {
            System.out.println("TRUE, CURRENT DECISION MODE " + NarratorStreamHandler.BLACKBOARD_RETRIEVER.getDecisionMode());
            return true;
        }
        System.out.println("FALSE, CURRENT DECISION MODE " + NarratorStreamHandler.BLACKBOARD_RETRIEVER.getDecisionMode());
        return false;
    }

    public static void main(String[] args) {
        IntroduceCharacterIntentHandler i = new IntroduceCharacterIntentHandler();
        //System.out.println(i.getRandomReprompt());
    }
}

/*
        if (actorNameSlot.getValue != null) {
            //characterName = actorNameSlot.getValue();               // set the charactername according to the provided user input.
            characterName = requestHelper.getSlotValue(actorNameSlot.getName());
            speechText = "You chose the actor name slot, your input was " + characterName;
            LOGGER.trace("Current attribute for characternameslot is: " + characterName);
        }
        if (firstNameSlot.getValue() != null) {
            characterName = firstNameSlot.getValue();                  // set the charactername according to the provided user input.
            LOGGER.trace("Current attribute for firstnameslot is: " + characterName);
            speechText = "You chose the first name slot, your input was " + characterName;
        }
        if (fictionalCharacterSlot.getValue() != null) {
            characterName = fictionalCharacterSlot.getValue();
            LOGGER.trace("Current attribute for fictionalnameslot is: " + characterName);

 */




    /*private void addCharacter(AttributesManager attributesManager, String name) {
        Map<String, Object> attributes = attributesManager.getSessionAttributes();

        if (!attributes.containsKey(characterA)) {                    //attributes.get(characterA).equals("")
            System.out.println("SH:CHAR_A Putting " + name);
            attributes.put(characterA, name);
            attributesManager.setSessionAttributes(attributes);
            interactiveSection.isNamingPossible(name);
        }
        else if (!attributes.containsKey(characterB)) {
            System.out.println("SH:CHAR_B Putting " + name + ". CHAR_A is " + attributes.get(characterA));
            attributes.put(characterB, name);
            attributesManager.setSessionAttributes(attributes);
            interactiveSection.isNamingPossible(name);
        }
        else {
            System.out.println("Setting allCharacterSet TRUE");
        }

    }*/


    /*private void setCharacterName(HandlerInput input){
        //Request request             = input.getRequestEnvelope().getRequest();
        //IntentRequest intentRequest = (com.amazon.ask.model.IntentRequest) request;
        //Intent intent               = intentRequest.getIntent();
        //Map<String, Slot> slots     = intent.getSlots();

        //Slot actorNameSlot          = slots.get(ACTOR_NAME_SLOT);    //retrieve the user-given character name
        //Slot firstNameSlot          = slots.get(FIRST_NAME_SLOT);
        //Slot fictionalCharacterSlot = slots.get(FICTIONAL_CHARACTER_SLOT);

        RequestHelper requestHelper         = RequestHelper.forHandlerInput(input);
        AttributesManager attributesManager = input.getAttributesManager();
        Map<String, Object> attributes      = attributesManager.getSessionAttributes();

        if(attributes.containsKey(characterA)){
            System.out.println("SH:CHAR_A is " + attributes.get(characterA));
        }
        else {
            System.out.println("SH:CHAR_A has not been set.");
        }

        if(attributes.containsKey(characterB)){
            System.out.println("SH:CHAR_B is " + attributes.get(characterB));
        }
        else {
            System.out.println("SH:CHAR_B has not been set.");
        }

        if (requestHelper.getSlotValue(ACTOR_NAME_SLOT).isPresent()) {
            characterName = requestHelper.getSlotValue(ACTOR_NAME_SLOT);                     // add attribute map to current session.
            interactiveSection.isNamingPossible(characterName.get());
            //addCharacter(attributesManager, characterName.get());                            // add character to interactive session.
            System.out.println("SH:Current attribute for characternameslot is: " + characterName.get());
        }
        if (requestHelper.getSlotValue(FIRST_NAME_SLOT).isPresent()) {
            characterName = requestHelper.getSlotValue(FIRST_NAME_SLOT);                    // set the charactername according to the provided user input.
            interactiveSection.isNamingPossible(characterName.get());
            //addCharacter(attributesManager, characterName.get());                           // add character to interactive session.
            System.out.println("SH:Current attribute for firstnameslot is: " + characterName.get());
        }
        if (requestHelper.getSlotValue(FICTIONAL_CHARACTER_SLOT).isPresent()) {
            characterName = requestHelper.getSlotValue(FICTIONAL_CHARACTER_SLOT);           // set the charactername according to the provided user input.
            interactiveSection.isNamingPossible(characterName.get());
            //addCharacter(attributesManager, characterName.get());                           // add character to interactive session.
            System.out.println("SH:Current attribute for fictionalnameslot is: " + characterName.get());
        }
    }*/