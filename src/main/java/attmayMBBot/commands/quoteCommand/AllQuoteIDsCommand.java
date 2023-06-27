package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class AllQuoteIDsCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    public AllQuoteIDsCommand(AttmayMBBotConfig config, QuoteManager quoteManager){
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        StringBuilder sb = new StringBuilder();
        List<Pair<String,Quote>> quoteList = null;
        String embedTitle = "List of all Quotes and their IDs";
        List<String> embedDescriptions = new ArrayList<>();

        if (args.length == 1) { // default case: print all the quotes
            quoteList = quoteManager.getAllQuotesSortedByIssuedDate();
        } else if (args.length > 1) { // special case: print quotes by a specific author
            String authorName = args[1];
            if (quoteManager.checkIfQuoteAuthorNameExists(authorName)) {
                quoteList = quoteManager.getAllQuotesFromAuthorSortedByIssuedDate(authorName);
            } else {
                message.getChannel().block().createMessage("Author not found!").block();
                return;
            }
        }

        if (quoteList == null) {
            return; // failed to obtain a valid quote list
                    // this is not good.
        }

        // no quotes D:
        if (quoteList.size() <= 0) {
            message.getChannel().block().createMessage("There are no quotes that match those requirements in the system yet.").block();
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
            sb = new StringBuilder(nextQuote + "\n\n");
        }

        // After every quote is done, add the last string in the StringBuilder to the list,
        // but only if the length of the string is greater than one.
        if (sb.toString().length() > 0)
            embedDescriptions.add(sb.toString());

        // just yeet the quote list out there
        for (String embedDescription : embedDescriptions) {
            message.getChannel().block().createMessage(y -> {
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
