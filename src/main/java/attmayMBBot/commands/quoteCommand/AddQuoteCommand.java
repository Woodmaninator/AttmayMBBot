package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;

import java.util.Date;

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
    public void execute(Message message, String[] args) {
        //This is a command that you need to be authorized for in order to perform it. Luckily Past-Woodmaninantor built a class for this very thing
        if(new AdvancedBotUserAuthorization(this.config).checkIfUserIsAuthorized(message.getAuthor().get())){
            //This code gets executed if the user is authorized
            if(args.length > 3){
                //This code only gets executed if they actually pass a name/alias, a year and a quote
                StringBuilder sb = new StringBuilder();
                //argument number 4 is the first part of the actual quote
                for(int i = 3; i < args.length; i++)
                    sb.append(args[i]).append(" ");
                //remove the last space
                if(sb.length() > 0)
                    sb.deleteCharAt(sb.length() - 1);

                //get the quote properties
                Date nowDate = new Date();
                String quoteAuthorName = args[1];

                //I'm too lazy to look for an equivalent method of TryParse in Java, so I'm just going to use a try/catch block
                int quoteYear;
                try {
                    quoteYear = Integer.parseInt(args[2]);
                } catch(Exception ex){
                    message.getChannel().block().createMessage("The year is not a valid number!").block();
                    return;
                }

                Long quoteId = this.quoteIDManager.getNextQuoteId();
                String quoteText = sb.toString();
                //make the new Quote object
                Quote quote = new Quote(quoteId, quoteYear, quoteText, message.getAuthor().get().getId().asLong(), nowDate);
                //check if the quoteAuthorName exists
                if(this.quoteManager.checkIfQuoteAuthorNameExists(quoteAuthorName)){
                    //if it does, add the quote to the author's list of quotes
                    this.quoteManager.addQuote(quoteAuthorName, quote);
                    //save the quote to the file
                    this.quoteManager.saveQuotesToFile();
                    message.getChannel().block().createMessage("Quote successfully added").block();
                } else {
                    //if it doesn't exist, print an error message
                    message.getChannel().block().createMessage("Quote can not be added because that user is not known to attmayMBBot!").block();
                }
            } else
                message.getChannel().block().createMessage("This command feels incomplete.\nUse !addquote [Username/Alias] [Year] [QuoteText] instead").block();
        } else
            message.getChannel().block().createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
    }
}
