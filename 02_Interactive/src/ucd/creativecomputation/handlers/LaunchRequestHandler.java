package ucd.creativecomputation.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import ucd.creativecomputation.alexa.NarratorStreamHandler;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "";

        if(NarratorStreamHandler.BLACKBOARD_RETRIEVER.isFirstItemRead()) {
            speechText = "The story has been started, would you like to continue from where it was left of?";
        } else if(NarratorStreamHandler.BLACKBOARD_RETRIEVER.isLastItemRead()) {
            speechText = "The story is all read. If you would like to hear it again, please ask me to reset the story.";
        } else {
            speechText = "Hello, would you like to hear a story?";
        }

        String repromptText = "would you like to hear a story?";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .build();
    }
}