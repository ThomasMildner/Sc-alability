package ucd.creativecomputation.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class SessionEndedRequestHandler implements RequestHandler {

    static final Logger logger = LogManager.getLogger(NonInteractiveIntentHandler.class);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(SessionEndedRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        // Reset all read processes for the story setup for a new session.
        // !!! This has disturbed the interaction - when the Echo lost connection is would start over.
        // BLACKBOARD_RETRIEVER.resetReadProcess();

        // any cleanup logic goes here

        // setting up a logger
        BasicConfigurator.configure();
        logger.trace("Something happened in SessionEndedRequestHandler.class.\n"
                + input.getRequestEnvelope().getRequest().getRequestId());

        return input.getResponseBuilder().build();
    }
}