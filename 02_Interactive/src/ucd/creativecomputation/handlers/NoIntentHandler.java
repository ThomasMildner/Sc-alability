package ucd.creativecomputation.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;

import ucd.creativecomputation.alexa.NarratorStreamHandler;
import ucd.creativecomputation.server.InteractiveSection;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class NoIntentHandler implements RequestHandler {

    private InteractiveSection interactiveSection = null;

    public NoIntentHandler(){
        interactiveSection = new InteractiveSection();
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.NoIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        String currentSection   = interactiveSection.buildInteractiveResponse(); // Receive current story section from blackboard.

        boolean isAskResponse   = false;

        ResponseBuilder responseBuilder = input.getResponseBuilder();
        responseBuilder.withSpeech(getNoReaction() + " " + currentSection)
                .withReprompt(getRandomReprompt())
                .withShouldEndSession(false);

        if (isAskResponse) {
            responseBuilder.withShouldEndSession(false)
                    .withReprompt(getRandomReprompt());
        }

        return responseBuilder.build();
    }

    private String getNoReaction() {
        String[] reaction = {"Well, I am not finished yet so be quiet.", "Sorry, I disagree."
            , "I hear you, but disagree.", "I am going to continue anyways.", "Be a little patient, we are almost done."
            , "Come on, give me a chance to win your interest.", "Hm. Maybe this next section will gain your interest."};
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


    public static void main(String[] args) {
        YesIntentHandler yih = new YesIntentHandler();
    }
}
