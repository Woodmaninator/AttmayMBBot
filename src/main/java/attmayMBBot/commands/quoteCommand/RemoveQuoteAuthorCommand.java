package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

// intended command usage: !removeauthor [author name]

public class RemoveQuoteAuthorCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;

    public RemoveQuoteAuthorCommand(AttmayMBBotConfig config, QuoteManager quoteManager) {
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        AdvancedBotUserAuthorization authorization = new AdvancedBotUserAuthorization(config);
        String authorName;

        // authorized user check
        if (!authorization.checkIfUserIsAuthorized(sender)) {
            channel.createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
            return;
        }

        // check argument count
        if (!args.containsKey("authorname")) {
            channel.createMessage("This command feels incomplete.\nUse /removeauthor [Author username] instead.").block();
            return;
        }

        // get author name
        authorName = args.get("authorname");

        // attempt remove author
        if (!quoteManager.getQuoteAuthors().removeIf(author -> author.getName().equalsIgnoreCase(authorName))) {
            channel.createMessage("Who? :face_with_raised_eyebrow:").block();
            return;
        }

        // save changes
        quoteManager.saveQuotesToFile();

        // report success
        channel.createMessage(String.format("Author \"%s\" and their quotes have successfully been removed!", authorName)).block();
    }
}
