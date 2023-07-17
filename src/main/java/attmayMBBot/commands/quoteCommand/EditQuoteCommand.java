package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class EditQuoteCommand implements ICommand {

    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    private QuoteIDManager quoteIDManager;

    public EditQuoteCommand(AttmayMBBotConfig config, QuoteManager quoteManager, QuoteIDManager quoteIDManager) {
        this.config = config;
        this.quoteManager = quoteManager;
        this.quoteIDManager = quoteIDManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
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

        // check for correct amount of arguments
        if (!args.containsKey("text")) {
            channel.createMessage("This command feels incomplete.\nUse /editquote [QuoteID] [NewQuoteText]\nor /editquote [NewQuoteText] instead. (The second one will edit the latest quote.)").block();
            return;
        }

        Optional<Pair<Long, String>> opt_quoteData = parseCommandArguments(args);
        if (!opt_quoteData.isPresent()) {
            channel.createMessage("Failed to edit quote: could not infer command variant (no valid QuoteID found)").block();
            return;
        }

        Pair<Long, String> quoteData = opt_quoteData.get();

        // check if the quote exists
        if (quoteData.getKey() > quoteIDManager.getLastQuoteId()) {
            channel.createMessage("Woah! That quote's from the future! :open_mouth:").block();
            return;
        } else if (quoteData.getKey() < 0) {
            channel.createMessage("Are you serious?? :face_with_raised_eyebrow:").block();
            return;
        } else if (quoteData.getKey().equals(0L)) {
            channel.createMessage("Quote IDs start from 1. Are you trying to modify history??").block();
            return;
        }

        if (!quoteManager.modifyQuoteText(quoteData.getKey(), quoteData.getValue())) {
            channel.createMessage("Failed to edit quote: quote not find.").block();
            return;
        }

        quoteManager.saveQuotesToFile();
        channel.createMessage(String.format("Quote #%s successfully fixed to %s!\n", quoteData.getKey(), quoteData.getValue())).block();
    }

    private Optional<Pair<Long, String>> parseCommandArguments(Map<String, String> args) {
        Long quoteID = 0L;
        String newQuoteString = "";

        if(args.containsKey("id")){
            try{
                quoteID = Long.parseLong(args.get("id"));
            } catch (Exception e){
                return Optional.empty();
            }
        } else {
            quoteID = quoteIDManager.getLastQuoteId();
        }

        if(args.containsKey("text")){
            newQuoteString = args.get("text");
        } else {
            newQuoteString = "";
        }

        return Optional.of(new Pair(quoteID, newQuoteString));
    }
}
