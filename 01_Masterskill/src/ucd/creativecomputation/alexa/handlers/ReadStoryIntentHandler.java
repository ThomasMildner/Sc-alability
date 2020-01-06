package ucd.creativecomputation.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.amazon.ask.request.Predicates.intentName;
import static ucd.creativecomputation.alexa.NarratorStreamHandler.BLACKBOARD_RETRIEVER;

/**
 * Skill class to let Alexa read the story sentence by sentence and
 * update the Blackboard doing so.
 *
 * @author
 *  Thomas Mildner
 */
public class ReadStoryIntentHandler implements RequestHandler {

    // Array of various possible reprompts for Alexa to say to remind
    // the audience to continue the story.
    private String[] reprompts  = {
            "Should I continue?",
            "Do you follow?",
            "Are you still there?",
            "I didn't get that",
            "Nao, listen up"
    };

    // Class variables
    private String speechPause      = "<break time=\"1.0s\" />";
    private String lastSentence     = null;
    private boolean isEndOfStory;


    @Override
    public boolean canHandle(HandlerInput input){
        return input.matches(intentName("ReadStoryIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request             = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent               = intentRequest.getIntent();
        Map<String, Slot> slots     = intent.getSlots();

        String speechText       = "";
        String repromptText     = "";

        boolean isAskResponse   = false;

        // Check if Blackboard is empty. If so, prompt the audience to create the story first.
        if(!BLACKBOARD_RETRIEVER.isBlackboardEmpty()){
            speechText = fetchSentenceFromBlackBoard();
        }

        else{
            // Render a reprompt since no story has been created yet.
            repromptText = "Unfortunately we did not create a story together yet. If you would like to do so, please" +
                    "ask me to create a story and give me a subject, it should be about.";
            isAskResponse = true;
        }

        // If the Blackboard has already been filled, a sentence can be said.
        ResponseBuilder responseBuilder = input.getResponseBuilder();
        responseBuilder.withSimpleCard("Read A Story", speechText)
                .withSpeech(speechText)
                .withReprompt(getRandomReprompt())
                .withShouldEndSession(false);

        // Setup end of story scenario to end the story reading.
        if (isEndOfStory) {
            responseBuilder.withShouldEndSession(true)
                    .withSpeech(speechText + ". This is what I wrote. " +
                            "I hope you liked it, Nao, you are quite the listener.")
                    .withSimpleCard("Read A Story", "The End.")
                    .withShouldEndSession(true);
        }

        if (isAskResponse) {
            responseBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText);
        }

        return responseBuilder.build();
    }

    /**
     * Function to retrieve the next sentence from the Blackboard.
     * In case that sentence is the last one on the Blackboard,
     * this function will set the class variable isEndOfStory to true.
     * @return
     *  returns the next sentence on the Blackboard.
     */
    private String fetchSentenceFromBlackBoard(){
        isEndOfStory        = false;
        lastSentence        = BLACKBOARD_RETRIEVER.getLastSentence();
        String nextSentence = BLACKBOARD_RETRIEVER.getSentence();

        System.out.println("The next sentence is: " + nextSentence);
        System.out.println("The last sentence is: " + lastSentence);

        if(nextSentence.equals(lastSentence)) {
            isEndOfStory = true;
            return nextSentence + speechPause;
        }

        return nextSentence + speechPause;
    }

    /**
     * Function to chose a random additional reprompt to remind the audience,
     * to keep saying something like 'continue'.
     * @return
     *  returns one reprompt by chance. Note, that not every sentence will have
     *  a reprompt attached to it.
     */
    private String getRandomReprompt(){
        int rnd = new Random().nextInt(reprompts.length);

        return reprompts[rnd];
    }


    //////////////////////////////////
    public static void main(String[] args) {
        ReadStoryIntentHandler rs = new ReadStoryIntentHandler();
        System.out.println(rs.fetchSentenceFromBlackBoard());
        /*for(Map<String, AttributeValue> item : BLACKBOARD_RETRIEVER.getItemMaps()){
            if(!BLACKBOARD_RETRIEVER.isLastSentence(item)){
            }
            else {
                System.out.println("false");
            }
        }*/
    }
}
