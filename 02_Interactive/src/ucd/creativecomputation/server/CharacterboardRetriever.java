package ucd.creativecomputation.server;

        import com.amazonaws.AmazonServiceException;
        import com.amazonaws.auth.AWSCredentials;
        import com.amazonaws.auth.AWSStaticCredentialsProvider;
        import com.amazonaws.auth.BasicAWSCredentials;
        import com.amazonaws.regions.Regions;
        import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
        import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
        import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
        import com.amazonaws.services.dynamodbv2.document.DynamoDB;
        import com.amazonaws.services.dynamodbv2.document.Item;
        import com.amazonaws.services.dynamodbv2.document.Table;
        import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
        import com.amazonaws.services.dynamodbv2.model.*;

        import java.util.List;
        import java.util.Map;

        import static ucd.creativecomputation.alexa.NarratorStreamHandler.IAM_ACCESSKEY;
        import static ucd.creativecomputation.alexa.NarratorStreamHandler.IAM_SECRETKEY;

public class CharacterboardRetriever {

    private boolean allCharacterSet                     = false;

    private String DYNAMODB_TABLE_NAME                  = "Evaluation_Character";
    private Regions region                              = Regions.US_EAST_1;
    private AmazonDynamoDB client                       = null;
    private DynamoDB dynamoDB                           = null;
    private Table characterboard                        = null;
    private ScanRequest scanRequest                     = null;
    private ScanResult scanResult                       = null;
    private List<Map<String, AttributeValue>> itemMaps  = null;

    public CharacterboardRetriever(){

        // Setting the credentials. Both keys are provided in the NarratorStreamHandler Class.
        AWSCredentials credentials = new BasicAWSCredentials(
                IAM_ACCESSKEY,
                IAM_SECRETKEY);

        client = AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();

        dynamoDB        = new DynamoDB(client);
        characterboard  = dynamoDB.getTable(DYNAMODB_TABLE_NAME);
        scanRequest     = new ScanRequest()
                .withTableName(DYNAMODB_TABLE_NAME);
        scanResult      = client.scan(scanRequest);
        itemMaps        = scanResult.getItems();
    }

    private boolean isCharASet(){
        for (Map<String, AttributeValue> item : itemMaps) {
            if(item.get("ID").getS().equals("CHAR_A")){
                //return !(item.get("name").getS().equals(" "));
                if(item.get("name").getS().equals(" ")) {
                    System.out.println("CR:Ready to set CHAR_A");
                    return false;
                }
                else {
                    System.out.println("CR:CHAR_A has already been set.");
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCharBSet() {
        for (Map<String, AttributeValue> item : itemMaps) {
            if (item.get("ID").getS().equals("CHAR_B")) {
                if (item.get("name").getS().equals(" ")) {
                    System.out.println("CR:Ready to set CHAR_B");
                    return false;
                } else {
                    System.out.println("CR:CHAR_B has already been set.");
                    return true;
                }
            }
        }
        System.out.println("CR:CHAR_B has already been set.");
        return false;
    }

    public void setCharacterA(String name) {
        AttributeUpdate attributeUpdate = new AttributeUpdate("name").put(name);
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("ID", "CHAR_A")
                .withAttributeUpdate(attributeUpdate);

        characterboard.updateItem(updateItemSpec);
        System.out.println("CR:CHAR_A has been set to " + name);
    }

    public void setCharacterB(String name) {
        AttributeUpdate attributeUpdate = new AttributeUpdate("name").put(name);
        UpdateItemSpec updateItemSpec   = new UpdateItemSpec()
                .withPrimaryKey("ID", "CHAR_B")
                .withAttributeUpdate(attributeUpdate);

        characterboard.updateItem(updateItemSpec);
        System.out.println("CR:CHAR_B has been set to " + name);
    }

    public void setCharacter(String name){
        System.out.println("CR:Trying to set character for name: " + name +  "...");
        if(!isCharASet()) {
            System.out.println("CR:CHAR_A is available...");
            setCharacterA(name);
        }
        else if(!isCharBSet()) {
            System.out.println("CR:CHAR_B is available...");
            setCharacterB(name);
        }
        else {
            allCharacterSet = true;
        }
    }

    public String getCharacterA(){
        if (isCharASet()) {
            for (Map<String, AttributeValue> item : itemMaps) {
                if (item.get("ID").getS().equals("CHAR_A")) {
                    return item.get("name").getS();
                }
            }
        }
        System.out.println("CR:CHAR:A is empty.");
        return "";
    }

    public String getCharacterB(){
        if (isCharBSet()) {
            for (Map<String, AttributeValue> item : itemMaps) {
                if (item.get("ID").getS().equals("CHAR_B")) {
                    return item.get("name").getS();
                }
            }
        }
        System.out.println("CR:CHAR:B is empty.");
        return "";
    }

    public void resetCharacterBoard() {
        AttributeUpdate attributeUpdate = new AttributeUpdate("name").put(" ");
        UpdateItemSpec updateItemSpecA  = new UpdateItemSpec()
                .withPrimaryKey("ID", "CHAR_A")
                .withAttributeUpdate(attributeUpdate);
        UpdateItemSpec updateItemSpecB  = new UpdateItemSpec()
                .withPrimaryKey("ID", "CHAR_B")
                .withAttributeUpdate(attributeUpdate);

        characterboard.updateItem(updateItemSpecA);
        characterboard.updateItem(updateItemSpecB);

        System.out.println("CR:All names have been reset.");
    }

    /**
     * Method to read all data from the Blackboard and plots them out into the console.
     */
    private void readBlackboard() {
        try {
            TableDescription table_info = client.describeTable(DYNAMODB_TABLE_NAME).getTable();
            System.out.format("Table name  : %s\n",
                    table_info.getTableName());
            System.out.format("Table ARN   : %s\n",
                    table_info.getTableArn());
            System.out.format("Status      : %s\n",
                    table_info.getTableStatus());
            System.out.format("Item count  : %d\n",
                    table_info.getItemCount().longValue());
            System.out.format("Size (bytes): %d\n",
                    table_info.getTableSizeBytes().longValue());

            ProvisionedThroughputDescription throughput_info =
                    table_info.getProvisionedThroughput();
            System.out.println("Throughput");
            System.out.format("  Read Capacity : %d\n",
                    throughput_info.getReadCapacityUnits().longValue());
            System.out.format("  Write Capacity: %d\n",
                    throughput_info.getWriteCapacityUnits().longValue());

            List<AttributeDefinition> attributes =
                    table_info.getAttributeDefinitions();
            System.out.println("Attributes");
            for (AttributeDefinition a : attributes) {
                System.out.format("  %s (%s)\n",
                        a.getAttributeName(), a.getAttributeType());
            }
        }
        catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        CharacterboardRetriever cr = new CharacterboardRetriever();
        //cr.readBlackboard();
        cr.resetCharacterBoard();
    }
}
