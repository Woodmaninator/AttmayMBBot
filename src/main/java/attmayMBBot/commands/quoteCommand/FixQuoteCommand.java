package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Optional;

public class FixQuoteCommand implements ICommand {

    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    private QuoteIDManager quoteIDManager;

    public FixQuoteCommand(AttmayMBBotConfig config, QuoteManager quoteManager, QuoteIDManager quoteIDManager) {
        this.config = config;
        this.quoteManager = quoteManager;
        this.quoteIDManager = quoteIDManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        AdvancedBotUserAuthorization authorizer = new AdvancedBotUserAuthorization(this.config); // authorized user check

        // check for user permissions
        if (!authorizer.checkIfUserIsAuthorized(message.getAuthor().get())) {
            message.getChannel().block().createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
            return;
        }

        // check for existing quotes
        if (quoteManager.getAllQuotesSortedByIssuedDate().size() <= 0) {
            message.getChannel().block().createMessage("There are no quotes in the system yet.").block();
            return;
        }

        // check for correct amount of arguments
        if (args.length < 2) {
            message.getChannel()
                    .block().createMessage("This command feels incomplete.\nUse !editquote [QuoteID] [NewQuoteText]\nor !editquote [NewQuoteText] instead.")
                    .block();
            return;
        }

        Optional<Pair<Long, String>> opt_quoteData = parseCommandArguments(args);
        if (!opt_quoteData.isPresent()) {
            message.getChannel().block().createMessage("Failed to edit quote: could not infer command variant (no valid QuoteID found)").block();
            return;
        }

        Pair<Long, String> quoteData = opt_quoteData.get();

        // check if the quote exists
        if (quoteData.getKey() > quoteIDManager.getLastQuoteId()) {
            message.getChannel().block().createMessage("Woah! That quote's from the future! :open_mouth:").block();
            return;
        } else if (quoteData.getKey() < 0) {
            message.getChannel().block().createMessage("Are you serious?? :face_with_raised_eyebrow:").block();
            return;
        } else if (quoteData.getKey().equals(0L)) {
            message.getChannel().block().createMessage("Quote IDs start from 1. Are you trying to modify history??").block();
            return;
        }

        if (!quoteManager.modifyQuoteText(quoteData.getKey(), quoteData.getValue())) {
            message.getChannel().block().createMessage("Failed to edit quote: quote not find.").block();
            return;
        }

        quoteManager.saveQuotesToFile();
        message.getChannel().block().createMessage(String.format("Quote #%s successfully fixed to %s!\n", quoteData.getKey(), quoteData.getValue())).block();
    }

    private Optional<Pair<Long, String>> parseCommandArguments(String[] args) {
        Long quoteID = 0L;
        String newQuoteString = "";

        if (args[1].startsWith("\"")) { // is immediately a quote
            quoteID = quoteIDManager.getLastQuoteId();
            newQuoteString = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            return Optional.of(new Pair<>(quoteID, newQuoteString));
        }

        // if it doesn't start with a quote, it must contain an ID.
        try {
            quoteID = Long.parseLong(args[1]);
        } catch (Exception e) {
            return Optional.empty();
        }

        // string together the arguments
        newQuoteString = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        return Optional.of(new Pair(quoteID, newQuoteString));
    }
}
