package ucd.creativecomputation.knowledgebase.nocobjects.character;

/**
 * Interface representing a character.
 * A character must have a name, a gender, and a voice.
 */
public interface Character {

    String getName();

    String getGender();

    String getVoice();

    void setVoice(String voice);
}