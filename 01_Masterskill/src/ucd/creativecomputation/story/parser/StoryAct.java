package ucd.creativecomputation.story.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Story Act. A Story Act consists of an action and a {@see Dialogue} List.
 */
public class StoryAct {

    // Class variables.
    private String action               = "";
    private List<Dialogue> dialogues    = new ArrayList<>();

    /**
     * Constructor setting up a StoryAct based on an action and a List of Dialogues that happen alongside.
     * @param action
     *  the action happening during the Dialogue's content.
     * @param dialogues
     *  the list of Dialogues.
     */
    public StoryAct(String action, List<Dialogue> dialogues) {
        this.action     = action;
        this.dialogues  = dialogues;
    }

    /**
     * Function to return the action.
     * @return
     *  returns the action.
     */
    public String getAction() {
        return action;
    }

    /**
     * Funtion to return the List of Dialogues.
     * @return
     *  returns a list of Dialogues.
     */
    public List<Dialogue> getDialogueList() {
        return dialogues;
    }

    /**
     * Function to return a Dialogue by index.
     * @param index
     *  receives an index of the Dialogue.
     * @return
     *  returns a Dialogue based on the received index.
     */
    public Dialogue getDialogueByIndex(int index) {
        return dialogues.get(index);
    }

    @Override
    public String toString() {
        String act = action;
        for(Dialogue d : dialogues) {
            act += "\n" + d.toString();
        }
        return act;
    }
}
