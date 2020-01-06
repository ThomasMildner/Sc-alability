package ucd.creativecomputation.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import ucd.creativecomputation.alexa.NarratorStreamHandler;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ResetStoryIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ResetStoryIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "I'll reset the story to its beginning. Any progress will be lost. " +
                "Would you like to here the story again? Say stop if you like me to stop.";

        NarratorStreamHandler.BLACKBOARD_RETRIEVER.resetReadProcess();

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .build();
    }
}
