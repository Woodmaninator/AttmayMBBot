package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public class RemoveQuoteCommand implements ICommand {

    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    private QuoteIDManager quoteIDManager;

    public RemoveQuoteCommand(AttmayMBBotConfig config, QuoteManager quoteManager, QuoteIDManager quoteIDManager) {
        this.config = config;
        this.quoteManager = quoteManager;
        this.quoteIDManager = quoteIDManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        Long quoteID = quoteIDManager.getLastQuoteId(); // id of quote to remove (defaults to last quote ID)
        AdvancedBotUserAuthorization authorizer = new AdvancedBotUserAuthorization(this.config); // authorized user check

        // check for user permissions
        if (!authorizer.checkIfUserIsAuthorized(sender)) {
            channel.createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
            return;
        }

        // check for existing quotes
        if (quoteManager.getAllQuotesSortedByIssuedDate().size() <= 0) {
            channel.createMessage("There are no quotes in the system yet.").block();
            return;
        }

        // specific case: remove specific quote
        if (args.containsKey("id")) {
            try {
                quoteID = Long.parseLong(args.get("id"));
            } catch (Exception e) {
                channel.createMessage("That's not a valid Quote ID! :rolling_eyes:").block();
                return;
            }

            // check if the quote exists
            if (quoteID > quoteIDManager.getLastQuoteId()) {
                channel.createMessage("Woah! That quote's from the future! :open_mouth:").block();
                return;
            } else if (quoteID < 0) {
                channel.createMessage("Are you serious?? :face_with_raised_eyebrow:").block();
                return;
            } else if (quoteID == 0) {
                channel.createMessage("Quote IDs start at 1. Are you trying to erase history??").block();
                return;
            }
        }

        if (!quoteManager.removeQuote(quoteID)) {
            channel.createMessage("Failed to remove quote because it was not found.").block();
            return;
        }
        quoteManager.saveQuotesToFile();
        channel.createMessage(String.format("Quote #%s successfully removed!", quoteID)).block();
    }
}
