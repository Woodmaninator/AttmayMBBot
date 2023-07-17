package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteAuthor;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

// intended command usage: !renameauthor [current name] [new name]

public class RenameQuoteAuthorCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;

    public RenameQuoteAuthorCommand(AttmayMBBotConfig config, QuoteManager quoteManager) {
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        AdvancedBotUserAuthorization authorization = new AdvancedBotUserAuthorization(config);
        String currentAuthorName;
        String newAuthorName;

        // authorized user check
        if (!authorization.checkIfUserIsAuthorized(sender)) {
            channel.createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
            return;
        }

        // check argument existence
        if (!args.containsKey("old-name") || !args.containsKey("new-name")) {
            channel.createMessage("This command feels incomplete.\nUse /renameauthor [Author username] [New author name] instead.").block();
            return;
        }

        currentAuthorName = args.get("old-name");
        newAuthorName = args.get("new-name");

        // find author by name
        QuoteAuthor author = quoteManager.getAuthorFromName(currentAuthorName);
        if (author == null) {
            channel.createMessage("Who? :face_with_raised_eyebrow:").block();
            return;
        }

        //Check whether the new name is already taken
        if(quoteManager.checkIfQuoteAuthorNameExists(newAuthorName)){
            channel.createMessage("This name is already taken!").block();
            return;
        }

        // rename author
        author.setName(newAuthorName);

        // save changes
        quoteManager.saveQuotesToFile();

        // report success
        channel.createMessage(String.format("Author \"%s\" successfully renamed to \"%s\"!", currentAuthorName, newAuthorName)).block();
    }
}
