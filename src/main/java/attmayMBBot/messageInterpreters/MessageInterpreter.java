package attmayMBBot.messageInterpreters;

import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.arcade.ArcadeManager;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import attmayMBBot.messageInterpreters.messageTextInterpreters.IMessageTextInterpreter;
import attmayMBBot.messageInterpreters.messageTextInterpreters.MessageTextInterpreter;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;

public class MessageInterpreter {
    private AttmayMBBotConfig config;
    private CommandInterpreter commandInterpreter;
    private IMessageTextInterpreter messageTextInterpreter;
    public MessageInterpreter(GatewayDiscordClient gateway, AttmayMBBotConfig config, QuoteManager quoteManager, ArcadeManager arcadeManager, QuoteQuizManager quoteQuizManager, ArcadeGameManager arcadeGameManager){
        this.config = config;
        this.commandInterpreter = new CommandInterpreter(gateway, config, quoteManager, arcadeManager, new QuoteIDManager(quoteManager.getQuoteAuthors()), quoteQuizManager, arcadeGameManager);
        this.messageTextInterpreter = new MessageTextInterpreter(config);
    }
    public void interpretMessage(Message message) {
        if (!message.getAuthor().get().isBot()) {
            String messageContent = message.getContent();
            //Execute the command
            try {
                if(messageContent.length() > 0){
                    if(messageContent.charAt(0) == '!'){
                        //If this statement is true, the message is a command and gets passed ot the command-interpreter
                        this.commandInterpreter.interpretCommand(message);
                    } else {
                        //If the statement is false, the message is not a command and will get put through the message text interpreters
                        this.messageTextInterpreter.interpretMessageText(message);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                message.getChannel().block().createMessage("Whoops, something went wrong. Sorry about that.\n" + ex.getMessage()).block();
            }
        }
    }

}
