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

public class AllQuoteIDsCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    public AllQuoteIDsCommand(AttmayMBBotConfig config, QuoteManager quoteManager){
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        StringBuilder sb = new StringBuilder();
        List<Pair<String,Quote>> quoteList = null;
        String embedTitle = "List of all Quotes and their IDs";
        List<String> embedDescriptions = new ArrayList<>();

        if (!args.containsKey("author")) { // default case: print all the quotes
            quoteList = quoteManager.getAllQuotesSortedByIssuedDate();
        } else { // special case: print quotes by a specific author
            String authorName = args.get("author");
            if (quoteManager.checkIfQuoteAuthorNameExists(authorName)) {
                quoteList = quoteManager.getAllQuotesFromAuthorSortedByIssuedDate(authorName);
            } else {
                channel.createMessage("Author not found!").block();
                return;
            }
        }

        if (quoteList == null) {
            return; // failed to obtain a valid quote list
                    // this is not good.
        }

        // no quotes D:
        if (quoteList.size() <= 0) {
            channel.createMessage("There are no quotes that match those requirements in the system yet.").block();
        }

        for (Pair<String, Quote> quotePair : quoteList) {
            String nextQuote = new String(); // quote string

            nextQuote += quotePair.getValue().getId() + ": "; // append id
            nextQuote += quotePair.getValue().toFormattedString(quotePair.getKey()); // append formatted quote
            nextQuote += "\n\n"; // append new line & line padding

            if (sb.toString().length() + nextQuote.length() < 4096) {
                sb.append(nextQuote);
                continue;
            }

            // In case the first embedDescriptions is full (limit of 4096), start another one and create a new StringBuilder instance.
            // The description gets stored in the embedDescription list.
            embedDescriptions.add(sb.toString());
            sb = new StringBuilder(nextQuote);
        }

        // After every quote is done, add the last string in the StringBuilder to the list,
        // but only if the length of the string is greater than one.
        if (sb.toString().length() > 0)
            embedDescriptions.add(sb.toString());

        // just yeet the quote list out there
        for (String embedDescription : embedDescriptions) {
            channel.createMessage(y -> {
                y.setEmbed(x -> {
                    x.setTitle(embedTitle)
                     .setDescription(embedDescription)
                     .setColor(Color.of(0, 102, 102));
                });
            })
            .block();
        }
    }
}
