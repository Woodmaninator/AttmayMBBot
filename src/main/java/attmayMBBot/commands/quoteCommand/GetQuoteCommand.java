package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteAuthor;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import javafx.util.Pair;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class GetQuoteCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    private QuoteIDManager quoteIDManager;

    public GetQuoteCommand(AttmayMBBotConfig config, QuoteManager quoteManager, QuoteIDManager quoteIDManager) {
        this.config = config;
        this.quoteManager = quoteManager;
        this.quoteIDManager = quoteIDManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        if (args.length == 1) { // default, old behaviour
            printRandomQuote(message.getChannel().block());
            return;
        }

        printQuoteFromIDString(message.getChannel().block(), args[1]);
    }

    private void printRandomQuote(MessageChannel channel) {
        List<Pair<String, Quote>> quotePairs = this.quoteManager.getAllQuotesSortedByIssuedDate();

        if (quotePairs.size() <= 0) {
            channel.createMessage("There are no quotes in the system yet.").block();
            return;
        }

        Random random = new Random();
        Pair<String, Quote> randomPair = quotePairs.get(random.nextInt(quotePairs.size()));
        channel.createMessage(randomPair.getValue().toFormattedString(randomPair.getKey())).block();
    }

    private void printQuoteFromIDString(MessageChannel channel, String argument) {
        List<Pair<String, Quote>> quotePairs = this.quoteManager.getAllQuotesSortedByIssuedDate();
        Long quoteID;
        String messageText = "";

        // check if there are quotes
        if (quotePairs.size() <= 0) {
            channel.createMessage("There are no quotes in the system yet.").block();
            return;
        }

        // check if ID is a valid Long
        try {
            quoteID = Long.parseLong(argument);
        } catch (Exception e) {
            channel.createMessage("That's not a valid Quote ID! :rolling_eyes:").block();
            return;
        }

        // check if ID is valid
        if (quoteID < 0) {
            channel.createMessage("Are you serious?? :face_with_raised_eyebrow:").block();
            return;
        } else if (quoteID == 0) {
            channel.createMessage("Quote IDs start from 1.").block();
            return;
        }

        // check if ID is unoccupied (from future)
        if (quoteID > quoteIDManager.getLastQuoteId()) {
            channel.createMessage("Woah! That quote's from the future! :open_mouth:").block();
            return;
        }

        // attempt to retrieve quote
        try {
            for (QuoteAuthor author : quoteManager.getQuoteAuthors()) {
                Optional<Quote> opt_quote  = author.getQuotes().stream()
                        .filter(q -> q.getId().equals(quoteID))
                        .findFirst();
                if (!opt_quote.isPresent()) {
                    continue;
                }

                messageText = opt_quote.get().toFormattedString(author.getName());
                break;
            }
        } catch (Exception e) {
            channel.createMessage("Something went wrong trying to retrieve that quote.\n"+e.toString()).block();
        }

        // check if ID is a tombstone (belongs to deleted quote)
        if (messageText == "") {
            channel.createMessage("Whoops, that quote has been deleted (presumably by Interpol [they're onto us!]).\nI mean-\nWhat quote?").block();
            return;
        }

        channel.createMessage(messageText).block();
    }
}
