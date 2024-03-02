package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;
import attmayMBBot.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AllQuotesCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    public AllQuotesCommand(AttmayMBBotConfig config, QuoteManager quoteManager){
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        List<Pair<String,Quote>> quoteList = null;
        if(!args.containsKey("author")){
            //Default case: print all the quotes
            quoteList = quoteManager.getAllQuotesSortedByIssuedDate();
        } else {
            //special case: print quotes by a specific author
            String authorName = args.get("author");
            if(quoteManager.checkIfQuoteAuthorNameExists(authorName)){
                quoteList = quoteManager.getAllQuotesFromAuthorSortedByIssuedDate(authorName);
            } else {
                channel.createMessage("Author not found!").block();
                return;
            }
        }
        if(quoteList != null) {
            if(quoteList.size() > 0) {
                //quoteList has the proper quotes to print here!
                List<String> embedDescriptions = new ArrayList<>();
                String embedTitle = "List of all Quotes";
                StringBuilder sb = new StringBuilder();
                for (Pair<String, Quote> quotePair : quoteList) {
                    String nextQuote = quotePair.getValue().toFormattedString(quotePair.getKey());
                    if (sb.toString().length() + (nextQuote + "\n\n").length() < 4096)
                        sb.append(nextQuote + "\n\n");
                    else {
                        //In case the first embed Descriptions is full (limit of 4096), start another one and create a new StringBuilder instance
                        //The description gets stored in the embedDescription list
                        embedDescriptions.add(sb.toString());
                        sb = new StringBuilder(nextQuote + "\n\n");
                    }
                }
                //After every quote is done, add the last string in the StringBuilder to the list
                //But only if the length of the string is greater than one
                if (sb.toString().length() > 0)
                    embedDescriptions.add(sb.toString());
                //Just yeet them out there
                for (String embedDescription : embedDescriptions) {
                    channel.createMessage(y -> y.setEmbed(x -> x.setTitle(embedTitle).setDescription(embedDescription).setColor(Color.of(0, 102, 102)))).block();
                }
            } else {
                channel.createMessage("There are no quotes in the system yet (That match your requirement).").block();
            }
        }
    }
}
