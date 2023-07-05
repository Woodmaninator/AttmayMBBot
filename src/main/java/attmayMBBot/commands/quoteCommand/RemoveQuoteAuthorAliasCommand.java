package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteAuthor;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;

import java.util.Optional;

// intended command usage: !removealias [author alias]

public class RemoveQuoteAuthorAliasCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;

    public RemoveQuoteAuthorAliasCommand(AttmayMBBotConfig config, QuoteManager quoteManager) {
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        AdvancedBotUserAuthorization authorization = new AdvancedBotUserAuthorization(config);
        String alias;

        // authorized user check
        if (!authorization.checkIfUserIsAuthorized(message.getAuthor().get())) {
            message.getChannel()
                    .block()
                    .createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.")
                    .block();
            return;
        }

        // check argument count
        if (args.length < 2) {
            message.getChannel()
                    .block()
                    .createMessage("This command feels incomplete.\nUse !removealias [Alias] instead.")
                    .block();
            return;
        }

        // get alias
        alias = args[1];

        Optional<QuoteAuthor> opt_aliasOwner = removeAlias(alias);
        if (!opt_aliasOwner.isPresent()) {
            message.getChannel()
                    .block()
                    .createMessage("Who? :face_with_raised_eyebrow:")
                    .block();
            return;
        }

        // save changes
        quoteManager.saveQuotesToFile();

        // report success
        message.getChannel()
                .block()
                .createMessage(String.format("Successfully removed %s's alias \"%s\"", opt_aliasOwner.get().getName(), alias))
                .block();
    }

    private Optional<QuoteAuthor> removeAlias(String alias) {
        for (QuoteAuthor author : quoteManager.getQuoteAuthors()) {
            if (author.getAliases().removeIf(authorAlias -> authorAlias.equalsIgnoreCase(alias))) {
                return Optional.of(author);
            }
        }

        return Optional.empty();
    }
}
