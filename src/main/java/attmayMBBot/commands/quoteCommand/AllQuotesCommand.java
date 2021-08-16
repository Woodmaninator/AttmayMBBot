package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;

import java.util.ArrayList;
import java.util.List;

public class AllQuotesCommand implements ICommand {
    private AttmayMBBotConfig config;
    public AllQuotesCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Message message, String[] args) {
        List<String> quotes = this.config.getQuoteConfig().getQuotes();
        List<String> embedDescriptions = new ArrayList<>();
        String embedTitle = "List of all Quotes";
        StringBuilder sb = new StringBuilder();
        for(String quote : quotes){
            if(sb.toString().length() + (quote + "\n\n").length() < 4096)
                sb.append((quote + "\n\n"));
            else{
                //In case the first embed Descriptions is full (limit of 4096), start another one and create a new StringBuilder instance
                //The description gets stored in the embedDescription list
                embedDescriptions.add(sb.toString());
                sb = new StringBuilder(quote + "\n\n");
            }
        }
        //After every quote is done, add the last string in the stringbuilder to the list
        //But only if the length of the string is greater than one
        if(sb.toString().length() > 0)
            embedDescriptions.add(sb.toString());
        //Just yeet them out there
        for(String embedDescription : embedDescriptions){
            message.getChannel().block().createMessage(y -> y.setEmbed(x -> x.setTitle(embedTitle).setDescription(embedDescription).setColor(Color.of(0, 102, 102)))).block();
        }
    }
}
