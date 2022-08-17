package attmayMBBot.functionalities.emojiKitchen;

import java.util.List;

public class EmojiCombinationHandler {
    private List<EmojiCombination> data;

    public String getURLFromEmojis(String emoji1, String emoji2){
        for (EmojiCombination combination : data) {
            if ( //checks if the combination is found, the order of emojis does not matter
                    (combination.getEmoji1Key().equals(emoji1) && combination.getEmoji2Key().equals(emoji2)) ||
                    (combination.getEmoji1Key().equals(emoji2) && combination.getEmoji2Key().equals(emoji1))) {
                return combination.getUrl();
            }
        }
        return null;
    }
}
