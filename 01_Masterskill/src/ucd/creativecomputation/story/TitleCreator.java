package ucd.creativecomputation.story;

import ucd.creativecomputation.knowledgebase.nocobjects.character.NocCharacter;

/**
 * Class representing a title for a story. This class is supposed to be expandable
 * for different alternatives for title suggestions.
 *
 * Featured Styles:
 *  The Good, the Bad and the Ugly
 *
 * @author
 * Thomas Mildner
 */
public class TitleCreator extends ItemGenerator {

    /**
     * Method setting an array with three character.
     * @return
     */
    private String[] featureCharacter () {

        for (int i = 0; i < 2; i++) {
            featureCharacter()[i] = super.getNocCharacter().get(i);
        }
        return featureCharacter();
    }

    /**
     * Method returning a story title in the style of 'The Good, the Bad and the Ugly'.
     * @param charX
     *  First character name, to retrieve a positive from -> as in 'the Good'.
     * @param charY
     *  Second character name, to retrieve a negative from -> as in 'the Bad'.
     * @param charZ
     *  Third character name, to retrieve a negative from -> as in 'the Ugly'.
     * @return
     *  returns a story title in the style of 'The Good, the Bad and the Ugly'.
     */
    public String getGBUTitle(String charX, String charY, String charZ) {

        String good = new NocCharacter(charX).getPositiveTalkingPoints().firstElement();
        String bad  = new NocCharacter(charY).getNegativeTalkingPoints().firstElement();
        String ugly = new NocCharacter(charZ).getNegativeTalkingPoints().firstElement();

        return "The " + good + ", the " + bad + " and the " + ugly + ".";
    }

    /**
     * Method returning a story title in the style of 'The Good, the Bad and the Ugly'.
     * @param charX
     *  First NocCharacter, to retrieve a positive from -> as in 'the Good'.
     * @param charY
     *  Second NocCharacter, to retrieve a negative from -> as in 'the Bad'.
     * @param charZ
     *  Third NocCharacter, to retrieve a negative from -> as in 'the Ugly'.
     * @return
     *  returns a story title in the style of 'The Good, the Bad and the Ugly'.
     */
    public String getGBUTitle(NocCharacter charX, NocCharacter charY, NocCharacter charZ) {
        return getGBUTitle(charX.getName(), charY.getName(), charZ.getName());
    }

    public static void main(String[] args) {
        TitleCreator tc = new TitleCreator();
        String[] chars= new String[]{"The Joker", "Albert Einstein", "Abraham Lincoln"};
        System.out.println(tc.getGBUTitle(chars[0], chars[1], chars[2]));
    }
}
