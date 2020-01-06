package ucd.creativecomputation.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static ucd.creativecomputation.toolkit.StoryFiles.getRandomNameFromFile;

/**
 * Skill class to give the audience a random character to setup a story for.
 * !! This skill is currently not setup in the current front end.
 * @author
 * Thomas Mildner
 */
public class SuggestCharacterIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input){
        return input.matches(intentName("SuggestCharacterIntentHandler"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Alright! I could tell you a story about ";
        String suggestCharacter = getRandomNameFromFile();

        return input.getResponseBuilder()
                .withSpeech(speechText + suggestCharacter)
                .withSimpleCard("Narrator Help", speechText)
                .withReprompt(speechText)
                .build();
    }
}
