package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Date;
import java.util.Map;

public class AddQuoteCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    private QuoteIDManager quoteIDManager;

    public AddQuoteCommand(AttmayMBBotConfig config, QuoteManager quoteManager, QuoteIDManager quoteIDManager) {
        this.config = config;
        this.quoteManager = quoteManager;
        this.quoteIDManager = quoteIDManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        //This is a command that you need to be authorized for in order to perform it. Luckily Past-Woodmaninator built a class for this very thing
        if(new AdvancedBotUserAuthorization(this.config).checkIfUserIsAuthorized(sender)){
            //This code gets executed if the user is authorized
            if(!args.containsKey("author") || !args.containsKey("year") || !args.containsKey("quote")) {
                //get the quote properties
                Date nowDate = new Date();
                String quoteAuthorName = args.get("author");

                //I'm too lazy to look for an equivalent method of TryParse in Java, so I'm just going to use a try/catch block
                int quoteYear;
                try {
                    quoteYear = Integer.parseInt(args.get("year"));
                } catch(Exception ex){
                    channel.createMessage("The year is not a valid number!").block();
                    return;
                }

                Long quoteId = this.quoteIDManager.getNextQuoteId();
                String quoteText = args.get("quote");
                //make the new Quote object
                Quote quote = new Quote(quoteId, quoteYear, quoteText, sender.getId().asLong(), nowDate);
                //check if the quoteAuthorName exists
                if(this.quoteManager.checkIfQuoteAuthorNameExists(quoteAuthorName)){
                    //if it does, add the quote to the author's list of quotes
                    this.quoteManager.addQuote(quoteAuthorName, quote);
                    //save the quote to the file
                    this.quoteManager.saveQuotesToFile();
                    channel.createMessage("Quote successfully added").block();
                } else {
                    //if it doesn't exist, print an error message
                    channel.createMessage("Quote can not be added because that user is not known to attmayMBBot!").block();
                }
            } else
                channel.createMessage("This command feels incomplete.\nUse /addquote [Username/Alias] [Year] [QuoteText] instead").block();
        } else
            channel.createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
    }
}
