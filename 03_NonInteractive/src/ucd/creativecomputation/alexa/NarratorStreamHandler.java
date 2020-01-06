package ucd.creativecomputation.alexa;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.exception.UnhandledSkillException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ucd.creativecomputation.handlers.*;
import ucd.creativecomputation.server.BlackboardRetriever;

import java.util.Random;

/**
 * Stream Handler Class that is needed for Amazon's Lambda to retrieve all necessary information from.
 * @author
 * Thomas Mildner
 */
public class NarratorStreamHandler extends SkillStreamHandler{
    public static final String IAM_ACCESSKEY    = "AKIAXKP5OSFKGNM3RY6E";                       // creativecomputation key: "AKIAJNPHYPQHQ6OFPECQ";
    public static final String IAM_SECRETKEY    = "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";   // creativecomputation key: "Rfh+2yXx5+u4EtJQ5MdT2in2vVKdAirqEgTa6qk9";

    public static BlackboardRetriever BLACKBOARD_RETRIEVER;//   = new BlackboardRetriever();

    public static Random RANDOM;
    public static Logger LOGGER;

    private static Skill getSkill() throws UnhandledSkillException {
        BLACKBOARD_RETRIEVER    = new BlackboardRetriever();
        RANDOM                  = new Random();
        LOGGER = LogManager.getLogger(NonInteractiveIntentHandler.class);

        return Skills.standard().addRequestHandlers(
                new NoIntentHandler(),
                new YesIntentHandler(),
                new ResetStoryIntentHandler(),
                new LaunchRequestHandler(),
                new HelpIntentHandler(),
                new StopIntentHandler(),
                new SessionEndedRequestHandler())
                .build();
    }

    public NarratorStreamHandler() throws UnhandledSkillException {
        super(getSkill());
    }
}
