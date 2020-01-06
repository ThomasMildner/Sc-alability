package ucd.creativecomputation.alexa;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.amazon.ask.exception.UnhandledSkillException;
import ucd.creativecomputation.alexa.handlers.*;
import ucd.creativecomputation.knowledgebase.KnowledgeBaseModul;
import ucd.creativecomputation.toolkit.retriever.BlackboardRetriever;
import ucd.creativecomputation.toolkit.retriever.S3FileRetriever;

import java.net.URL;

/**
 * Stream Handler Class that is needed for Amazon's Lambda to retrieve all necessary information from.
 *
 * @author
 * Thomas Mildner
 */
public class NarratorStreamHandler extends SkillStreamHandler{
    public static final String IAM_ACCESSKEY    = "put-your-accesskey-here";
    public static final String IAM_SECRETKEY    = "put-your-secretkey-here";

    // S3 Bucket and Filenames
    public static final String BUCKET_NAME              = "name-of-the-s3bucket-containing-necessary-NOC-Files";

    public static final String NOC_FILE                 = "Veale's The Noc List.txt";
    //public static final String TYPICAL_ACTIVITY_FILE    = "Veale's Typical Activities.txt";
    public static final String TYPICAL_CAUSALITY_FILE   = "Veale's typical causality.txt";
    public static final String AFFORDANCES              = "Veale's affordances.txt";
    public static final String WEAPON_ARSENAL           = "Veale's weapon arsenal.txt";
    public static final String COUNTERPARTS             = "Mildner_CharacterCounterParts.txt";

    // S3 File Receiver
    public static final S3FileRetriever S3_NOC_FILE_RETRIEVER   = new S3FileRetriever(BUCKET_NAME, NOC_FILE);
    //public static final S3FileRetriever S3_TA_FILE_RETRIEVER    = new S3FileRetriever(BUCKET_NAME, TYPICAL_ACTIVITY_FILE);
    public static final S3FileRetriever S3_TC_FILE_RETRIEVER    = new S3FileRetriever(BUCKET_NAME, TYPICAL_CAUSALITY_FILE);
    public static final S3FileRetriever S3_AFF_FILE_RETRIEVER   = new S3FileRetriever(BUCKET_NAME, AFFORDANCES);
    public static final S3FileRetriever S3_WA_FILE_RETRIEVER    = new S3FileRetriever(BUCKET_NAME, WEAPON_ARSENAL);
    public static final S3FileRetriever S3_CP_FILE_RETRIEVER    = new S3FileRetriever(BUCKET_NAME, COUNTERPARTS);

    // S3 Files
    public static final URL NOC_LIST_URL            = S3_NOC_FILE_RETRIEVER.getURLFromS3File();
    //public static final URL ACTIVITY_LIST_URL       = S3_TA_FILE_RETRIEVER.getURLFromS3File();
    public static final URL CAUSALITY_LIST_URL      = S3_TC_FILE_RETRIEVER.getURLFromS3File();
    public static final URL AFFORDANCES_LIST_URL    = S3_AFF_FILE_RETRIEVER.getURLFromS3File();
    public static final URL WEAPON_ARSENAL_LIST_URL = S3_WA_FILE_RETRIEVER.getURLFromS3File();
    public static final URL COUNTERPARTS_LIST_URL   = S3_CP_FILE_RETRIEVER.getURLFromS3File();

    // Various Knowledge Base Modules
    public static final KnowledgeBaseModul NOC_LIST             = new KnowledgeBaseModul(NOC_LIST_URL, 0);
    public static final KnowledgeBaseModul CAUSALITY_LIST       = new KnowledgeBaseModul(CAUSALITY_LIST_URL, 0);
    public static final KnowledgeBaseModul AFFORDANCES_LIST     = new KnowledgeBaseModul(AFFORDANCES_LIST_URL, 0);
    public static final KnowledgeBaseModul WEAPON_ARSENAL_LIST  = new KnowledgeBaseModul(WEAPON_ARSENAL_LIST_URL, 1);
    public static final KnowledgeBaseModul COUNTERPARTS_LIST    = new KnowledgeBaseModul(COUNTERPARTS_LIST_URL,0);

    public static BlackboardRetriever BLACKBOARD_RETRIEVER;//   = new BlackboardRetriever();

    private static Skill getSkill() throws UnhandledSkillException {
        BLACKBOARD_RETRIEVER = new BlackboardRetriever();

        return Skills.standard().addRequestHandlers(
                new InteractiveStoryIntentHandler(),
                new ReadStoryIntentHandler(),
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
