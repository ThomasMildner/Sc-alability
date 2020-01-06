package ucd.creativecomputation.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import org.apache.log4j.BasicConfigurator;
import ucd.creativecomputation.alexa.NarratorStreamHandler;
import ucd.creativecomputation.server.NonInteractiveSection;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class YesIntentHandler implements RequestHandler {

    NonInteractiveSection nonInteractiveSection = null;

    public YesIntentHandler(){
        nonInteractiveSection = new NonInteractiveSection();
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.YesIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        NonInteractiveSection nonInteractiveSection = new NonInteractiveSection();

        Request request             = input.getRequestEnvelope().getRequest();

        String currentSection       = nonInteractiveSection.buildNonInteractiveResponse(); // Receive current story section from blackboard.

        boolean isAskResponse   = false;

        ResponseBuilder responseBuilder = input.getResponseBuilder();
        responseBuilder.withSpeech(currentSection)
                .withReprompt(getRandomReprompt())
                .withShouldEndSession(false);

        if (isAskResponse) {
            responseBuilder.withShouldEndSession(false)
                    .withReprompt(getRandomReprompt());
        }

        // setting up a logger
        BasicConfigurator.configure();
        NarratorStreamHandler.LOGGER.trace("Something happened in NonInteractiveIntentHandler.class. \n"
                + input.getRequestEnvelope().getRequest().getRequestId());

        NarratorStreamHandler.LOGGER.trace("Trace Intent Request for NonInteractiveIntentHandler.class \n"
                + ((IntentRequest) request).getIntent());

        return responseBuilder.build();
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
