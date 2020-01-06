package ucd.creativecomputation.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class LaunchRequestHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Alright, let us write a story.";

        String repromptText = "Please tell me what to do.";
        return input.getResponseBuilder()
                .withSimpleCard("The Writer", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .build();
    }
}