package attmayMBBot.commands.quoteCommand;

import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import discord4j.core.object.entity.Message;

public class AddQuoteCommand implements ICommand {
    private AttmayMBBotConfig config;

    public AddQuoteCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Message message, String[] args) {
        //This is a command that you need to be authorized for in order to perform it. Luckily Past-Woodmaninantor built a class for this very thing
        if(new AdvancedBotUserAuthorization(this.config).checkIfUserIsAuthorized(message.getAuthor().get())){
            //This code gets executed if the user is authorized
            if(args.length > 1){
                //This code only gets executed if they actually pass a quote as an argument
                //This code looks way to simple and clean. There is no fucking way this is going to work
                StringBuilder sb = new StringBuilder();
                for(int i = 1; i < args.length; i++)
                    sb.append(args[i]).append(" ");
                this.config.getQuoteConfig().getQuotes().add(sb.toString());
                this.config.saveConfigToFile();
            } else
                message.getChannel().block().createMessage("This command feels incomplete.\nUse !addquote [Quote] instead").block();
        } else
            message.getChannel().block().createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
    }
}
