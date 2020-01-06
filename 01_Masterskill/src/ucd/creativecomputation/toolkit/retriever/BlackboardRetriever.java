package ucd.creativecomputation.toolkit.retriever;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static ucd.creativecomputation.alexa.NarratorStreamHandler.IAM_ACCESSKEY;
import static ucd.creativecomputation.alexa.NarratorStreamHandler.IAM_SECRETKEY;

public class BlackboardRetriever {

    // Various class variables
    private String DYNAMODB_TABLE_NAME                  = "Blackboard";
    //private String DYNAMODB_TABLE_NAME                  = "Test-Blackboard";
    private Regions region                              = Regions.US_EAST_1;
    private AmazonDynamoDB client                       = null;
    private DynamoDB dynamoDB                           = null;
    private Table blackboard                            = null;
    private ScanRequest scanRequest                     = null;
    private ScanResult scanResult                       = null;
    private List<Map<String, AttributeValue>> itemMaps  = null;
    private Map<String, AttributeValue> currentItem     = null;
    private int currentSentence;

    /**
     * Constructor of the Blackboard. It sets up all the credentials for the
     * DynamoDB table.
     */
    public BlackboardRetriever(){

        // Set Current Sentence to the Start;
        setCurrentSentenceToStart();

        // Setting the credentials. Both keys are provided in the NarratorStreamHandler Class.
        AWSCredentials credentials = new BasicAWSCredentials(
                IAM_ACCESSKEY,
                IAM_SECRETKEY);

        client = AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();

        dynamoDB    = new DynamoDB(client);
        blackboard  = dynamoDB.getTable(DYNAMODB_TABLE_NAME);
        scanRequest = new ScanRequest()
                .withTableName(DYNAMODB_TABLE_NAME);
        scanResult  = client.scan(scanRequest);
        itemMaps = scanResult.getItems();
    }

    /**
     * Method to reset the variable currentSentence to
     * the starting integer of the Blackboard.
     */
    public void setCurrentSentenceToStart(){
        currentSentence = 0;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                Write ITEMS ON THE BLACKBOARD                                  //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Method put a new item to the Blackboard. Instead of first deleting
     * the Blackboard and then recreating it, this method will assume,
     * that the table exists and only clear it before writing all
     * story itemMaps to it.
     * These itemMaps  then consist of the following attributes:
     * N: 'ID', S: 'Sentence, B: 'Narrator_Processed', B: 'Actor_Processed',
     * and S: 'Action'.
     * @param storyList
     *  A prepared list of story sentences.
     */
    public void putItemToBlackboard(List<List<String>> storyList){

        if(doesBlackboardExist()){
            clearBlackboard();
        }

        else {
            System.err.println("The Blackboard does not exist!");
            return;
        }

        Item item = new Item();
        for (int i = 0; i < storyList.size(); i++){
            //System.out.println("Putting Item: " + storyList.get(i).get(0)  + " | " + storyList.get(i).get(1));

            item.withPrimaryKey("ID", i)
                    .withString("Sentence", storyList.get(i).get(0))
                    .withString("Character", storyList.get(i).get(1))
                    .withString("Action", storyList.get(i).get(2))
                    .withString("Actor_Metamodel", " ")
                    .withString("Narrator_Metamodel", " ")
                    .withBoolean("Narrator_Processed", false)
                    .withBoolean("Actor_Processed", false);

            blackboard.putItem(item);
        }
        System.out.println("Done!");
    }

    /**
     * Function to set the Blackboard boolean attribute 'Narrator_Processed' to true.
     * This is being used after the next story sentence has been retrieved.
     * @param id
     *  id is the 'ID' attribute of the item on the Blackboard.
     *  It is used, to update the correct item.
     */
    private void setNarratorProcessedTrue(int id){
        AttributeUpdate attributeUpdate = new AttributeUpdate("Narrator_Processed").put(true);
        UpdateItemSpec updateItemSpec   = new UpdateItemSpec()
                .withPrimaryKey("ID", id)
                .withAttributeUpdate(attributeUpdate);

        blackboard.updateItem(updateItemSpec);
    }

    /**
     * Function waits for the actor to finish its acting. Only then to get the next sentence.
     * The function tries 5 times and waits of 0.5 seconds each time.
     * @return
     *  returns true only if the actor finished the actor. After 5 failed waiting attempts it returns false.
     */
    private boolean waitForActorProcessed() {

        boolean actor_processed = blackboard.getItem("ID", currentSentence).getBOOL("Actor_Processed");
        int tries               = 0;
        while(!actor_processed){
            long timeout = 500;
            try {
                TimeUnit.MILLISECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tries++;
            if(tries == 5){
                return false;
            }
            waitForActorProcessed();
        }
        return true;
    }

    /**
     * Function to reset all read processes to false. Thus every sentence can be accessed again.
     */
    public void resetReadProcess(){
        UpdateItemSpec updateItemSpec = new UpdateItemSpec();

        for (int i = 0; i < itemMaps.size(); i++){

            // Setting update itemspec with update expressions.
            updateItemSpec.withPrimaryKey("ID", i)
                    .withUpdateExpression("set Actor_Processed = :val1")
                    .withValueMap(new ValueMap()
                            .withBoolean(":val1", false))
                    .withUpdateExpression("set Narrator_Processed = :val2")
                    .withValueMap(new ValueMap()
                            .withBoolean(":val2", false));

            try {
                UpdateItemOutcome outcome = blackboard.updateItem(updateItemSpec);
            }
            catch (Exception e) {
                System.err.println("An error occurred while trying to reset the 'read' properties for this table.");
                System.err.println(e.getMessage());
            }
        }
        System.out.println(String.format("All read attributes have been successfully reset."));
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                 READ ITEMS ON THE BLACKBOARD                                  //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Function to retrieve the next sentence. The function runs through all itemMaps
     * on the Blackboard until it finds the first which's 'Narrator_Processed' value
     * is false. It then returns the S: 'Sentence' attribute of that item.
     * @return
     *  Returns the S: 'Sentence' attribute of an unread item.
     */
    public String getSentence() {

        Item item = blackboard.getItem("ID", currentSentence);
        // Going through the itemMaps on the blackboard to retrieve the first
        // that has not yet been said.
        while(item.getBOOL("Narrator_Processed")){
            currentSentence += 1;
            item = blackboard.getItem("ID", currentSentence);
        }

        // Wait for the actor to finish its acting.
        /*if(!waitForActorProcessed()){
            return "You keep forever. Just tell me when to continue.";
        }*/

        setNarratorProcessedTrue(currentSentence);
        // Hot Fix that makes sure ALL items on the table will be looked at.
        currentSentence = 0;

        return item.getString("Sentence");
    }

    /**
     * Method to return true in case an Item is the last one of a table.
     * @param item
     *  The to be checked item.
     * @return
     *  Returns true, if the item is the last on the table, otherwise returns false.
     */
    public boolean isLastSentence(Map<String, AttributeValue> item){
        // Scan the table with its current content.
        scanRequest     = new ScanRequest()
                .withTableName(DYNAMODB_TABLE_NAME);
        scanResult      = client.scan(scanRequest);
        itemMaps        = scanResult.getItems();

        if(("" + (itemMaps.size() - 1)).equals(item.get("ID").getN())){
            return true;
        }
        return false;
    }

    /**
     * Returns whether a given item is the last on the blackboard.
     * @param item
     *  Given item to be checked.
     * @return
     *  returns true, if the given item is last on the blackboard or false if not.
     */
    public boolean isLastItem(Item item){

        return item.get("ID").equals(itemMaps.size());
    }

    /**
     * Returns a list of all itemMaps on the Blackboard as Map<String,AttributeValue>.
     * @return
     *  returns a list of all itemMaps on the Blackboard as Map<String,AttributeValue>.
     */
    public List<Map<String,AttributeValue>> getItemMaps(){
        return itemMaps;
    }

    public String getLastSentence(){
        for(int i = 0; i < itemMaps.size(); i++){

            if(isLastSentence(getItemMaps().get(i))){
                String sentence = String.valueOf(getItemMaps().get(i).get("Sentence").getS());
                return sentence;
            }
        }
        return "No sentence could be found.";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //                                  WORK WITH ON THE BLACKBOARD                                  //
    ///////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Function to check, whether the Blackboard is empty or contains any itemMaps.
     * @return
     *  returns true, if the Blackboard is empty. Otherwise returns false.
     */
    public boolean isBlackboardEmpty(){
        Table table = dynamoDB.getTable(DYNAMODB_TABLE_NAME);
        return table.describe().getItemCount() == 0;
    }


    /**
     * Function to check for the existence of the Blackboard.
     * @return
     *  Returns true, if the Blackboard exists. Otherwise returns false.
     */
    private boolean doesBlackboardExist() {
        try {
            TableDescription table_info = dynamoDB.getTable(DYNAMODB_TABLE_NAME).describe();
            return true;
        }
        catch (ResourceNotFoundException rnfe) {
            return false;
        }
    }

    /**
     * Function that deletes all itemMaps on the Blackboard. Usually deleting the entire
     * table is supposedly faster, though due to the small size of the table this method
     * much faster.
     */
    public void clearBlackboard(){
        PrimaryKey primaryKey;
        DeleteItemSpec deleteItemSpec;
        System.out.println("Cleaning stuff throwing out old items.");

        for (int i = 0; i < itemMaps.size(); i++) {
            itemMaps.get(i).get("ID");
            primaryKey = new PrimaryKey("ID", i);
            deleteItemSpec = new DeleteItemSpec().withPrimaryKey(primaryKey);
            blackboard.deleteItem(deleteItemSpec);
        }
        System.out.println("The blackboard looks new and shiny.");
    }

    /**
     * Function to create a new Blackboard.
     */
    public void createBlackboard(){
        try {

            List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
            attributeDefinitions.add(new AttributeDefinition().withAttributeName("ID").withAttributeType("N"));

            List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
            keySchema.add(new KeySchemaElement().withAttributeName("ID").withKeyType(KeyType.HASH)); // Partition
            // key

            CreateTableRequest request = new CreateTableRequest().withTableName(DYNAMODB_TABLE_NAME).withKeySchema(keySchema)
                    .withAttributeDefinitions(attributeDefinitions).withProvisionedThroughput(
                            new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));

            System.out.println("Issuing CreateTable request for " + DYNAMODB_TABLE_NAME);
            Table table = dynamoDB.createTable(request);

            System.out.println("Waiting for " + DYNAMODB_TABLE_NAME + " to be created...this may take a while...");
            table.waitForActive();
        }
        catch (Exception e) {
            System.err.println("CreateTable request failed for " + DYNAMODB_TABLE_NAME);
            System.err.println(e.getMessage());
        }
    }

    /**
     * Function to delete the Blackbaord.
     */
    private void deleteBlackboard(){
        Table table = dynamoDB.getTable(DYNAMODB_TABLE_NAME);
        try {
            System.out.println("Issuing DeleteTable request for " + DYNAMODB_TABLE_NAME);
            table.delete();

            System.out.println("Waiting for " + DYNAMODB_TABLE_NAME + " to be deleted...this may take a while...");

            table.waitForDelete();
        }
        catch (Exception e) {
            System.err.println("DeleteTable request failed for " + DYNAMODB_TABLE_NAME);
            System.err.println(e.getMessage());
        }
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


    /////////////////////////////
    public static void main(String[] args) {
        // BasicConfigurator.configure();
        //ItemGenerator na = new ItemGenerator();
        //KnowledgeData knowledgeData = new KnowledgeData();
        BlackboardRetriever br = new BlackboardRetriever();

        //StoryParser sp = new StoryParser(knowledgeData.getCharacterFromAttribute("dumbledore"));
        //sp.getStory();
        // find last item on Blackboard.
        //System.out.println(br.getLastSentence());

        //br.clearBlackboard();

        //br.putItemToBlackboard(na.generateStoryItems(sp.getStoryString(), sp.getStoryLines()));
        //System.out.println(br.getSentence());
    }
}
