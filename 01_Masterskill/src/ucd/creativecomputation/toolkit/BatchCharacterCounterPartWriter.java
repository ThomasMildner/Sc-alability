package ucd.creativecomputation.toolkit;

import com.opencsv.CSVWriter;
import ucd.creativecomputation.knowledgebase.nocobjects.character.CharacterCounterParts;
import ucd.creativecomputation.knowledgebase.nocobjects.KnowledgeData;
import ucd.creativecomputation.knowledgebase.nocobjects.character.NocCharacter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

/**
 * Class to generate a file with all characters from the NOC List as key values and their positive and negative
 * talking points counter parts.
 *
 * @author
 * Thomas Mildner
 */
public class BatchCharacterCounterPartWriter {
    private KnowledgeData knowledgeData         = new KnowledgeData();
    private NocCharacter nocCharacter           = null;
    private CharacterCounterParts counterParts  = null;

    private Vector<String> allCharacters = knowledgeData.getAllCharacters();
    private HashMap<String, Vector> crossCharacters = new HashMap<>();


    public BatchCharacterCounterPartWriter(){
        //populateCharacterNames();
    }

    private void populateCharacterNames(){
        Vector<String> tmpCounterParts = new Vector<String>();
        for(String character : allCharacters){
            nocCharacter = new NocCharacter(character);
            counterParts = new CharacterCounterParts(nocCharacter);

            String positiveCounterPart = "###";
            String negativeCounterPart = "###";
            try{
                positiveCounterPart = counterParts.getPositiveCounterPart().getName();
                negativeCounterPart = counterParts.getNegativeCounterPart().getName();
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }

            tmpCounterParts.add(positiveCounterPart);
            tmpCounterParts.add(negativeCounterPart);

            crossCharacters.put(character, tmpCounterParts);

            System.out.println(character + " " + crossCharacters.get(character));

            tmpCounterParts.clear();
        }
    }

    private void writeCrossCharacterToCSV() throws Exception {
        String path     = "/Users/thomasmildner/Documents/03_Studium/03_UCD/02_Research/01_Computational-Creativity/AlexaKimStories/data/";
        String fileName = "CharacterCounterParts.csv";
        File file       = new File(path + fileName);
        try {
            FileWriter outputFile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputFile);

            String[] header = {"Character", "Positive Counterpart", "Negative Counterpart"};
            writer.writeNext(header);

            int counter = 1;

            for (String character : allCharacters) {
                nocCharacter = new NocCharacter(character);
                counterParts = new CharacterCounterParts(nocCharacter);

                String positiveCounterPart = " ";
                String negativeCounterPart = " ";

                try {
                    positiveCounterPart = counterParts.getPositiveCounterPart().getName();
                    negativeCounterPart = counterParts.getNegativeCounterPart().getName();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String[] line = {character, positiveCounterPart, negativeCounterPart};
                writer.writeNext(line);

                System.out.println(counter + ": " + character + " added to the file");
                counter++;

                /*if(counter > 5){
                    writer.close();
                    return;
                }*/
            }

            writer.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        BatchCharacterCounterPartWriter bw = new BatchCharacterCounterPartWriter();
        bw.writeCrossCharacterToCSV();
    }
}
