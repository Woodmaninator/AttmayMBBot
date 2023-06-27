package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;

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
    public void execute(Message message, String[] args) {
        Long quoteID = quoteIDManager.getLastQuoteId(); // id of quote to remove (defaults to last quote ID)
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

        // specific case: remove specific quote
        if (args.length > 1) {
            try {
                quoteID = Long.parseLong(args[1]);
            } catch (Exception e) {
                message.getChannel().block().createMessage("That's not a valid Quote ID! :rolling_eyes:").block();
                return;
            }

            // check if the quote exists
            if (quoteID > quoteIDManager.getLastQuoteId()) {
                message.getChannel().block().createMessage("Woah! That quote's from the future! :open_mouth:").block();
                return;
            } else if (quoteID < 0) {
                message.getChannel().block().createMessage("Are you serious?? :face_with_raised_eyebrow:").block();
                return;
            } else if (quoteID == 0) {
                message.getChannel().block().createMessage("Quote IDs start from 1. Are you trying to erase history??").block();
                return;
            }
        }

        if (!quoteManager.removeQuote(quoteID)) {
            message.getChannel().block().createMessage("Failed to remove quote because it was not found.").block();
            return;
        }
        quoteManager.saveQuotesToFile();
        message.getChannel().block().createMessage(String.format("Quote #%s successfully removed!", quoteID)).block();
    }
}
