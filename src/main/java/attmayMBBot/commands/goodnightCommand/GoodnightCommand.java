package attmayMBBot.commands.goodnightCommand;

import attmayMBBot.APIHandling.datamuseAPIHandling.DatamuseAPIHandler;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.util.EWordQueryMode;
import attmayMBBot.util.EWordType;
import attmayMBBot.util.WordObject;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.*;
import java.util.stream.Collectors;

public class GoodnightCommand implements ICommand {
    private AttmayMBBotConfig config;
    public GoodnightCommand(AttmayMBBotConfig config){
        this.config = config;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        EWordQueryMode wordQueryMode = EWordQueryMode.ADJECTIVE;
        String searchTerm = "";
        Random random = new Random();
        //Get the arguments and other shit
        if(args.containsKey("compound-word")){
            if(args.get("compound-word").equalsIgnoreCase("true"))
                wordQueryMode = EWordQueryMode.NOUN_BASED_ADJECTIVE;
        }

        if(args.containsKey("keyword"))
            searchTerm = args.get("keyword");

        //Get the current weekday and the weekday tomorrow
        String weekday = getWeekdayForAttmayTimezone(0);
        String weekdayTomorrow = getWeekdayForAttmayTimezone(86400000);
        String weekdayPattern = getDatamusePatternStringForWeekday(weekday);

        DatamuseAPIHandler datamuseAPIHandler = new DatamuseAPIHandler();
        List<WordObject> currentWordList = datamuseAPIHandler.getRandomWord(weekdayPattern, searchTerm, wordQueryMode);

        //Get the desired handling based on the WordQueryMode
        List<WordObject> finalWordObjectList = null;
        switch (wordQueryMode) {
            case ADJECTIVE:
                finalWordObjectList = currentWordList.stream().filter(x -> x.getWordType() == EWordType.ADJECTIVE).collect(Collectors.toList());
                break;
            case NOUN_BASED_ADJECTIVE:
                finalWordObjectList = currentWordList.stream().filter(x -> x.getWordType() == EWordType.NOUN).collect(Collectors.toList());
                break;
        }

        if (finalWordObjectList.size() == 0)
           channel.createMessage("There are no words. Sorry. Goodnight!").block();
        else {
            String finalWord = finalWordObjectList.get(random.nextInt(finalWordObjectList.size())).getTerm();
            if (wordQueryMode == EWordQueryMode.NOUN_BASED_ADJECTIVE)
                finalWord = finalWord + "-" + this.config.getGoodnightConfig().getCompoundModifierList().get(random.nextInt(this.config.getGoodnightConfig().getCompoundModifierList().size()));
            channel.createMessage("I'm afraid it's that time. I hope you all have a " + finalWord + " " + weekday + " night and an even better " + weekdayTomorrow + ". Goodnight!").block();
        }
    }
    private String getWeekdayForAttmayTimezone(long offset){
        //Current Date
        Date date = new Date();
        //Date 6 hours ago
        date.setTime(date.getTime() - 21600000);
        //Add offset
        date.setTime(date.getTime() + offset);
        //Get weekday
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch(cal.get(Calendar.DAY_OF_WEEK)){
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return "Saturday";
    }
    private String getDatamusePatternStringForWeekday(String weekday){
        switch(weekday){
            case "Monday":
                return "m*";
            case "Tuesday":
                return "t*";
            case "Wednesday":
                return "w*";
            case "Thursday":
                return "th*";
            case "Friday":
                return "f*";
            case "Saturday":
            case "Sunday":
                return "s*";
        }
        return "*";
    }
}