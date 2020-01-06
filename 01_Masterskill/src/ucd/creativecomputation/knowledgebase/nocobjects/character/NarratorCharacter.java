package ucd.creativecomputation.knowledgebase.nocobjects.character;

/**
 * Class representing a Narrator Object. It is subscribed to the Interface {@see Character}
 * but unlike other character objects very light weight and only consists of a preset name,
 * preset neutral gender and a set voice.
 */
public class NarratorCharacter implements Character {
    private String voice = null;

    @Override
    public String getName() {
        return "Narrator";
    }

    @Override
    public String getGender() {
        return "neutral";
    }

    @Override
    public String getVoice() {
        return voice != null ? voice : "no-voice";
    }

    @Override
    public void setVoice(String voice) {
        this.voice = this.voice == null ? voice : this.voice;
    }
}
