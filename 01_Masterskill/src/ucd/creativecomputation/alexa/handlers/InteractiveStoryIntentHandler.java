package ucd.creativecomputation.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.response.ResponseBuilder;
import com.amazon.ask.model.*;


import ucd.creativecomputation.toolkit.retriever.BlackboardRetriever;
import ucd.creativecomputation.knowledgebase.nocobjects.KnowledgeData;
import ucd.creativecomputation.story.ItemGenerator;
import ucd.creativecomputation.story.StoryParser;

import java.util.*;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Skill-class representing a story recveived by an audience given attribute.
 * @author
 * Thomas Mildner
 */
public class InteractiveStoryIntentHandler implements RequestHandler {

    // Setup variables.
    public static final String ATTRIBUTE_SLOT           = "attributeSlot";      // Select Slot from Alexa Skill 'The Narrator'
    public static final KnowledgeData KNOWLEDGE_DATA    = new KnowledgeData();

    // Variables to send progressive responses
    //private DirectiveServiceClient directiveServiceClient   = null;
    //private DirectiveService directiveService           = null;

    // private BlackboardRetriever blackboardRetriever = new BlackboardRetriever();
    private ItemGenerator naoActions                = null;
    private StoryParser storyParser                 = null;
    private KnowledgeData knowledgeData             = null;
    private BlackboardRetriever blackboardRetriever = null;


    private String character        = "";
    private String speechPause      = "<break time=\"3.0s\" />";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ChoseInteractiveStoryIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        System.out.println("Begin Intent: " + System.currentTimeMillis());

        Request request             = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (com.amazon.ask.model.IntentRequest) request;
        Intent intent               = intentRequest.getIntent();
        Map<String, Slot> slots     = intent.getSlots();

        Slot attributeSlot          = slots.get(ATTRIBUTE_SLOT);

        //RequestEnvelope requestEnvelope = input.getRequestEnvelope();
        //Context context             = requestEnvelope.getContext();
        //SystemState systemState     = getSystemState(context); //TODO: Test
        //String apiEndpoint          = systemState.getApiEndpoint();
        //directiveService      = new DirectiveServiceClient();

        String speechText           = "";   // Alexa's response to start the story telling.
        String repromptText         = "";   // In case the audience did not say anything, this will remind them what to say.
        String characterName        = "";   // The name of a character the story will be about.
        String attribute            = "";   // The attribute will be used on the knowledge data to receive a fitting character.
        String storyText            = "";   // The story that will be out on the story board.

        boolean isAskResponse       = false;

        // Printed out in the Lambda Console Log when the skill is tested.
        System.out.println("The Attribute for the Story is: " + attributeSlot);

        // Set-up initial story by taking the Attribute_Slot value
        // and walking it through the Knowledge-Base.
        if(attributeSlot != null){
            // Store suggested attribute in the session and create response.
            attribute = attributeSlot.getValue();
            //input.getAttributesManager().setSessionAttributes(Collections.singletonMap(ATTRIBUTE_KEY, attribute));

            setupStoryBoard(attribute);

            speechText      = "I think I wrote a wonderful story about " + attribute + " for you. ";
        }
        else {
            // Render an error since we don't know what the audiences character.
            repromptText    = "I'm not sure who that is. Please tell me an attribute the story should involve.";
            isAskResponse   = true;
        }

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        responseBuilder.withSimpleCard("The Writer",
                "A story about " + attribute)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        if (isAskResponse) {
            responseBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText);
        }

        System.out.println("Returning Intent: " + System.currentTimeMillis());

        // Dispatch a progressive response for the audience while Alexa is setting up the Blackboard.
        // setupProgrssiveResponse(request, systemState, apiEndpoint);

        return responseBuilder.build();
    }

    /**
     * Function to set up the storyboard with a story retrieved from the
     * given attribute-slot by the audience.
     * @param pAttribute
     *  The attribute, the story to tell will be about.
     */
    private void setupStoryBoard(String pAttribute){
        naoActions          = new ItemGenerator();
        knowledgeData       = new KnowledgeData();
        blackboardRetriever = new BlackboardRetriever();
        storyParser         = new StoryParser(knowledgeData.getCharacterFromAttribute(pAttribute));

        blackboardRetriever.putItemToBlackboard(naoActions.generateStoryItems(storyParser.getStoryString(), storyParser.getStoryLines()));
    }


    // TODO: Progressive Response while Alexa is loading a story to the Blackboard.
    /*
    private void setupProgrssiveResponse(Request request, SystemState systemState, String apiEndpoint){
        String progressiveResponse          = "This is going to be good." + speechPause
                + "Even an emotionless robot will like this one." + speechPause
                + "Ha! I am brilliant." + speechPause;

        ApiConfiguration configuration      = DefaultApiConfiguration.builder().build();
        String requestId                    = request.getRequestId();
        DirectiveEnvelopeHeader header      = DirectiveEnvelopeHeader.builder().withRequestId(requestId).build();
        SpeakDirective directive            = SpeakDirective.builder().withSpeech(progressiveResponse).build();
        SendDirectiveRequest sendDirectiveRequest = SendDirectiveRequest.builder().withDirective(directive).build();
        DirectiveServiceClient client       = new DirectiveServiceClient(configuration);

        if(systemState.getApiAccessToken()!= null && !systemState.getApiAccessToken().isEmpty()){
            String apiAccessToken = systemState.getApiAccessToken();
            try {
                client.enqueue(sendDirectiveRequest);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }

    }

    private SystemState getSystemState(Context context) {

        try{
            return context.getSystem();
        }
        catch (NullPointerException npe) {
            System.err.println("No Context SystemState was found.");
            System.err.println(npe.getMessage());
            return context.getSystem();
        }
    }

*/
    ///////////////////////////////////////
    public static void main(String[] args) {
        InteractiveStoryIntentHandler is = new InteractiveStoryIntentHandler();
        //is.setupStoryBoard("lord of the rings");
    }
}
