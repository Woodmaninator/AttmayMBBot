package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteAuthor;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;

public class AddQuoteAuthorAliasCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;

    public AddQuoteAuthorAliasCommand(AttmayMBBotConfig config, QuoteManager quoteManager) {
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        //This is a command that you need to be authorized for in order to perform it. Luckily Past-Woodmaninantor built a class for this very thing
        if(new AdvancedBotUserAuthorization(this.config).checkIfUserIsAuthorized(message.getAuthor().get())){
            if(args.length == 3){
                String quoteAuthorName = args[1];
                String newAlias = args[2];

                if(this.quoteManager.checkIfQuoteAuthorNameExists(quoteAuthorName)){
                    //user exists, try to add the alias
                    QuoteAuthor quoteAuthor = this.quoteManager.getAuthorFromName(quoteAuthorName);
                    if(!this.quoteManager.checkIfQuoteAuthorNameExists(newAlias)){
                        //alias does not exist, add it to the quoteAuthor
                        quoteAuthor.getAliases().add(newAlias);
                        this.quoteManager.saveQuotesToFile();
                        message.getChannel().block().createMessage("Alias successfully added!").block();
                    } else {
                        //alias already exists, print an error
                        message.getChannel().block().createMessage("Alias can not be added because that alias is already registered!").block();
                    }
                } else {
                    //user does not exist
                    message.getChannel().block().createMessage("The user you are trying to add an alias to does not exist!").block();
                }
            } else {
                //Wrong number of arguments
                message.getChannel().block().createMessage("This command feels incomplete.\nUse !addAlias [Username] [new Alias] instead!").block();
            }
        } else //No access to the command
            message.getChannel().block().createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
    }
}
