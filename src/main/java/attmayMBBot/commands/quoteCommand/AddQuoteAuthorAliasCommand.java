package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteAuthor;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public class AddQuoteAuthorAliasCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;

    public AddQuoteAuthorAliasCommand(AttmayMBBotConfig config, QuoteManager quoteManager) {
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        //This is a command that you need to be authorized for in order to perform it. Luckily Past-Woodmaninator built a class for this very thing
        if(new AdvancedBotUserAuthorization(this.config).checkIfUserIsAuthorized(sender)){
            if(args.containsKey("author") && args.containsKey("alias")){
                String quoteAuthorName = args.get("author");
                String newAlias = args.get("alias");

                if(this.quoteManager.checkIfQuoteAuthorNameExists(quoteAuthorName)){
                    //user exists, try to add the alias
                    QuoteAuthor quoteAuthor = this.quoteManager.getAuthorFromName(quoteAuthorName);
                    if(!this.quoteManager.checkIfQuoteAuthorNameExists(newAlias)){
                        //alias does not exist, add it to the quoteAuthor
                        quoteAuthor.getAliases().add(newAlias);
                        this.quoteManager.saveQuotesToFile();
                        channel.createMessage("Alias successfully added!").block();
                    } else {
                        //alias already exists, print an error
                        channel.createMessage("Alias can not be added because that alias is already registered!").block();
                    }
                } else {
                    //user does not exist
                    channel.createMessage("The user you are trying to add an alias to does not exist!").block();
                }
            } else {
                //Wrong number of arguments
                channel.createMessage("This command feels incomplete.\nUse /addAlias [Username] [new Alias] instead!").block();
            }
        } else //No access to the command
            channel.createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
    }
}
