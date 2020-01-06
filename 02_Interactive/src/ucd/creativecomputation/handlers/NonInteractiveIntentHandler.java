package ucd.creativecomputation.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import org.apache.log4j.BasicConfigurator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class NonInteractiveIntentHandler implements RequestHandler {

    // Logger for this class
    //static final Logger logger = LogManager.getLogger(NonInteractiveIntentHandler.class);

    // Array of various possible reprompts for Alexa to say to remind
    // the audience to continue the story.
    private String[] reprompts  = {
            "Should I continue?",
            "Do you follow?",
            "Are you still there?",
            "I didn't get that."
    };

    // Class variables
    private String speechPause      = "<break time=\"1.0s\" />";
    private String lastSentence     = null;
    private boolean isEndOfStory;


    @Override
    public boolean canHandle(HandlerInput input){
        return input.matches(intentName("nonInteractiveIntent"));
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


        // If the Blackboard has already been filled, a sentence can be said.
        ResponseBuilder responseBuilder = input.getResponseBuilder();
        responseBuilder.withSimpleCard("Read A Story", speechText)
                .withSpeech(speechText)
                .withReprompt(getRandomReprompt())
                .withShouldEndSession(false);

        // Setup end of story scenario to end the story reading.
        if (isEndOfStory) {
            responseBuilder.withShouldEndSession(true)
                    .withSpeech(speechText + speechPause + ". This was the story. " +
                            "I hope you liked it! You are quite the listener.")
                    .withSimpleCard("Read A Story", "The End.")
                    .withShouldEndSession(true);
        }

        if (isAskResponse) {
            responseBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText);
            //logger.error("Alexa could not understand NAO: " + intentRequest.getIntent().getName() + " " + intent);
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
            BLACKBOARD_RETRIEVER.resetReadProcess();
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
}
