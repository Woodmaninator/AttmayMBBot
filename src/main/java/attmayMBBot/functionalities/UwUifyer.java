package attmayMBBot.functionalities;

import java.util.List;
import java.util.Random;

public class UwUifyer {
    //In advance to writing this class, I just want to clarify that I am extremely sorry.
    //What I am doing may be wrong, but it has to be done.
    //Who the fuck am I kidding? It does not have to be done. No one asked me to do this. It was my own decision and yet i am pretending to care about the feelings
    //of myself or the feelings of other people. This is horrible. Not a single soul deserves this destiny. No matter how clean/efficient this code is going to end up
    //(Let's be real for a moment - it probably sucks ass) this will end up being the worst file in the whole project.

    private static final String[] emojiArray = new String[]{
            " rawr x3",
            " rawr xD",
            " OwO",
            " UwU",
            " uwu",
            " o.O",
            " >w<",
            " (⑅˘꒳˘)",
            " (ꈍᴗꈍ)",
            " (˘ω˘)",
            " (U ᵕ U❁)",
            " σωσ",
            " òωó",
            " (U ﹏ U)",
            " ( ͡o ω ͡o )",
            " ʘwʘ",
            " :3",
            " xD",
            " >_<",
            " \uD83D\uDE33",
            " \uD83E\uDD7A",
            " \uD83D\uDE33\uD83D\uDE33\uD83D\uDE33",
            " (✿oωo)",
            " /(^•ω•^)",
            "^•ﻌ•^ ",
            "(ˆ ﻌ ˆ)♡ ",
            " rawrrr",};

    public String uwuify(String message){
        //Here are the rules and their implementations for a proper UwUFication
        //S-S-Stuttering should appear s-sometimes at the beginning of a n-n-new word.
        //stupid emoji after . or ! or ? but also only sometimes
        int currentIndex = 0;
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        while(currentIndex < message.length()){
            char currentChar = message.charAt(currentIndex);
            switch(currentChar){
                case ' ':
                    //Space means that a new word is beginning. (Probably). Create stuttering for the next word.
                    //There is a 1 in 20 chance that a word will start with stuttering.
                    stringBuilder.append(' ');
                    if(random.nextFloat() <= 0.05f)
                        if(currentIndex + 1 < message.length())
                            if(thisCharIsPartOfTheAlphabet(message.charAt(currentIndex + 1))){
                                stringBuilder.append(changeCharIfNecessary(message.charAt(currentIndex + 1)));
                                stringBuilder.append('-');
                                stringBuilder.append(changeCharIfNecessary(message.charAt(currentIndex + 1)));
                                if(random.nextFloat() >= 0.5f){
                                    stringBuilder.append('-');
                                    stringBuilder.append(changeCharIfNecessary(message.charAt(currentIndex + 1)));
                                }
                                currentIndex++;
                            }
                    currentIndex++;
                    break;
                case '.':
                case '!':
                case '?':
                    //You know what punctuation means: Time for bad emojis.
                    stringBuilder.append(changeCharIfNecessary(currentChar));
                    //80% chance to add an emoji after punctuation.
                    if(random.nextFloat()<=0.8f)
                        stringBuilder.append(emojiArray[random.nextInt(emojiArray.length)]);
                    currentIndex++;
                    break;
                default:
                    stringBuilder.append(changeCharIfNecessary(currentChar));
                    currentIndex++;
                    break;
            }
        }
        return stringBuilder.toString();
    }
    private char changeCharIfNecessary(char c){
        //Shit method but i don't give a fuck tbh
        if(c == 'l' || c == 'r')
            return 'w';
        if(c == 'L' || c == 'R')
            return 'W';
        return c;
    }
    //The name for this method is actually not that good because it doesn't match the naming-conventions for a method that returns a boolean
    //However, I chose this name so i can just write:
    //if(thisCharIsPartOfTheAlphabet('x'))
    //Which makes sense from a grammatical point of view and also is very funny
    private boolean thisCharIsPartOfTheAlphabet(char c){
        //Checks if the ascii code of the char is within the range of the alphabet characters
        return ((int) c >= 65 && (int) c <= 90) || ((int) c >= 97 && (int) c <= 122);
    }
}
