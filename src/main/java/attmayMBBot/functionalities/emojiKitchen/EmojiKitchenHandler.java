package attmayMBBot.functionalities.emojiKitchen;

public class EmojiKitchenHandler {
    private static EmojiCombinationHandler emojiCombinationHandler;
    public static void setEmojiCombinationHandler(EmojiCombinationHandler handler) {
        emojiCombinationHandler = handler;
    }
    public static String getURLFromEmojis(String emoji1, String emoji2) {
        if(emojiCombinationHandler != null) {
            String e1 = getCodePointString(emoji1);
            String e2 = getCodePointString(emoji2);

            return emojiCombinationHandler.getURLFromEmojis(e1, e2);
        }
        return null;
    }
    private static String getCodePointString(String emoji){
        String result = "";
        if(!emoji.contains("\u200D")) {
            //This is the case in which there is no zero width joiner in the emoji (WHICH MEANS THAT IT'S A ONE PIECE EMOJI)
            result = emoji.codePoints().mapToObj(Integer::toHexString).findFirst().get();
            result = appendVariantSelector(emoji, result);
        } else {
            //Otherwise: It has to be a multiple-piece emoji. Emojis with more pieces are not supported yet.
            String substring = "";
            int separatorIndex = emoji.indexOf("\u200D");
            int startIndex = 0;
            while(separatorIndex != -1 && startIndex < emoji.length()){
                substring = emoji.substring(startIndex, separatorIndex);
                //Add codepoint of current emoji
                if(startIndex != 0)
                    result += "-";
                result = result + substring.codePoints().mapToObj(Integer::toHexString).findFirst().get();
                //Add the variant selector for the current emoji
                result = appendVariantSelector(substring, result);
                //Add the zero width joiner
                result = result + "-200d";

                startIndex = separatorIndex + 1;
                separatorIndex = emoji.indexOf("\u200D", startIndex);
            }
            //Add the last emoji after the zero width joiner
            System.out.println(emoji.length());
            substring = emoji.substring(startIndex, emoji.length());
            if(!substring.equals("")) {
                result += "-";
                result = result + substring.codePoints().mapToObj(Integer::toHexString).findFirst().get();
                result = appendVariantSelector(substring, result);
            }
        }
        return result;
    }
    private static String appendVariantSelector(String emoji, String result){
        if(emoji.toCharArray()[emoji.length() - 1] == 0xfe0f){
            return result + "-fe0f";
        }
        return result;
    }
}
