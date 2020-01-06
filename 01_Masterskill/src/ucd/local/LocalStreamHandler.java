package ucd.local;

import ucd.creativecomputation.knowledgebase.KnowledgeBaseModul;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class LocalStreamHandler {

    // Various local file paths
    private static final String LOCAL_NOC                = "src/ucd/local/localfiles/Veale's The NOC List.txt";
    private static final String LOCAL_TYPICAL_CAUSALITY  = "src/ucd/local/localfiles/Veale's typical causality.txt";
    private static final String LOCAL_AFFORDANCES        = "src/ucd/local/localfiles/Veale's affordances.txt";
    private static final String LOCAL_WEAPON_ARSENAL     = "src/ucd/local/localfiles/Veale's weapon arsenal.txt";
    private static final String LOCAL_COUNTERPARTS       = "src/ucd/local/localfiles/Mildner_CharacterCounterParts.txt";

    // Various local files
    private static final File LOCAL_NOC_FILE                 = new File(LOCAL_NOC);
    private static final File LOCAL_TYPICAL_CAUSALITY_FILE   = new File(LOCAL_TYPICAL_CAUSALITY);
    private static final File LOCAL_AFFORDANCES_FILE         = new File(LOCAL_AFFORDANCES);
    private static final File LOCAL_WEAPON_ARSENAL_FILE      = new File(LOCAL_WEAPON_ARSENAL);
    private static final File LOCAL_COUNTERPARTS_FILE        = new File(LOCAL_COUNTERPARTS);

    // Various local file URLs
    private static URL LOCAL_NOC_LIST_URL            = null;
    private static URL LOCAL_TYPICAL_CAUSALITY_URL   = null;
    private static URL LOCAL_AFFORDANCES_URL         = null;
    private static URL LOCAL_WEAPON_ARSENAL_URL      = null;
    private static URL LOCAL_COUNTERPARTS_URL        = null;

    static {
        try {
            LOCAL_NOC_LIST_URL          = LOCAL_NOC_FILE.toURI().toURL();
            LOCAL_TYPICAL_CAUSALITY_URL = LOCAL_TYPICAL_CAUSALITY_FILE.toURI().toURL();
            LOCAL_AFFORDANCES_URL       = LOCAL_AFFORDANCES_FILE.toURI().toURL();
            LOCAL_WEAPON_ARSENAL_URL    = LOCAL_WEAPON_ARSENAL_FILE.toURI().toURL();
            LOCAL_COUNTERPARTS_URL      = LOCAL_COUNTERPARTS_FILE.toURI().toURL();

        } catch (MalformedURLException e) {
            System.err.println("Some files could not be found");
            e.printStackTrace();
        }
    }

    // Various Knowledge Base Modules
    public static final KnowledgeBaseModul LOCAL_NOC_LIST               = new KnowledgeBaseModul(LOCAL_NOC_LIST_URL, 0);
    public static final KnowledgeBaseModul LOCAL_CAUSALITY_LIST         = new KnowledgeBaseModul(LOCAL_TYPICAL_CAUSALITY_URL, 0);
    public static final KnowledgeBaseModul LOCAL_AFFORDANCES_LIST       = new KnowledgeBaseModul(LOCAL_AFFORDANCES_URL, 0);
    public static final KnowledgeBaseModul LOCAL_WEAPON_ARSENAL_LIST    = new KnowledgeBaseModul(LOCAL_WEAPON_ARSENAL_URL, 1);
    public static final KnowledgeBaseModul LOCAL_COUNTERPARTS_LIST      = new KnowledgeBaseModul(LOCAL_COUNTERPARTS_URL,0);
}
