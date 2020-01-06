package ucd.creativecomputation.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "As The Writer, you can ask me to create a story on " +
                "a topic that might be on your mind.";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("The Writer – Help", speechText)
                .withReprompt(speechText)
                .build();
    }
}