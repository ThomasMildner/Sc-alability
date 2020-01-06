package ucd.creativecomputation.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import ucd.creativecomputation.alexa.NarratorStreamHandler;
import ucd.creativecomputation.server.InteractiveSection;
import ucd.creativecomputation.server.NonInteractiveSection;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class YesIntentHandler implements RequestHandler {

    private final static Logger LOGGER              = Logger.getLogger(YesIntentHandler.class);
    private InteractiveSection interactiveSection   = null;

    public YesIntentHandler(){
        interactiveSection = new InteractiveSection();
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.YesIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        //Request request             = input.getRequestEnvelope().getRequest();
        String currentSection       = interactiveSection.buildInteractiveResponse(); // Receive current story section from blackboard.

        boolean isAskResponse   = false;

        ResponseBuilder responseBuilder = input.getResponseBuilder();
        responseBuilder.withSpeech(getYesReaction() + " "+ currentSection)
                .withReprompt(getRandomReprompt())
                .withShouldEndSession(false);

        if (isAskResponse) {
            responseBuilder.withShouldEndSession(false)
                    .withReprompt(getRandomReprompt());
        }


        return responseBuilder.build();
    }

    private String getYesReaction(){
        String[] reaction = {"Alright.", "I agree.", "Let's go on then."
                , "Okay.", "Very well.", "I like the way you think."};

        return reaction[NarratorStreamHandler.RANDOM.nextInt(reaction.length)];
    }

    private String getRandomReprompt(){
        String[] reprompts  = {
                "Should I continue?",
                "Do you follow?",
                "Are you still there?",
                "I didn't get that."
        };
        return reprompts[NarratorStreamHandler.RANDOM.nextInt(reprompts.length)];
    }

    /*private String reactionUtterance(){
        Random random = new Random();
        String[] reactions = {"I like the way you think!", "I agree, let's spice things up.", "Very well, let's do this"};
        return reactions[random.nextInt(reactions.length)];
    }*/

    /*private String interactiveOutputUtterance() {
        InteractiveSection is = new InteractiveSection();
        String section = is.buildReply();
        return reactionUtterance() + " " + section;
    }*/


    public static void main(String[] args) {
        YesIntentHandler yih = new YesIntentHandler();
    }
}
