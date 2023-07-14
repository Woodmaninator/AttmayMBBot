package attmayMBBot.messageInterpreters;

import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.arcade.ArcadeManager;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingManager;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingResults;
import attmayMBBot.messageInterpreters.messageTextInterpreters.IMessageTextInterpreter;
import attmayMBBot.messageInterpreters.messageTextInterpreters.MessageTextInterpreter;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;

public class MessageInterpreter {
    private AttmayMBBotConfig config;
    private IMessageTextInterpreter messageTextInterpreter;
    public MessageInterpreter(GatewayDiscordClient gateway, AttmayMBBotConfig config, QuoteManager quoteManager, QuoteRankingManager quoteRankingManager, QuoteRankingResults quoteRankingResults, ArcadeManager arcadeManager, QuoteQuizManager quoteQuizManager, ArcadeGameManager arcadeGameManager){
        this.config = config;
        this.messageTextInterpreter = new MessageTextInterpreter(config);
    }
    public void interpretMessage(Message message) {
        if (!message.getAuthor().get().isBot()) {
            String messageContent = message.getContent();
            //Execute the messageTextInterpreter if the message is not empty
            //If the message starts witch a '!', it's a command. Commands were moved to slash commands. Inform the user of this
            try {
                if(messageContent.length() > 0){
                    if(messageContent.charAt(0) == '!'){
                        //If this statement is true, the message is a command
                        message.getChannel().block().createMessage("Looks like you just attempted to write a command.\nCommands are now available via slash commands. Use a '/' instead of a '!'");
                    } else {
                        //If the statement is false, the message is not a command and will get put through the message text interpreters
                        this.messageTextInterpreter.interpretMessageText(message);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                try { //Try to send a message to the channel, but if there's something wrong with the channel, that might not work either and break the entire bot
                    message.getChannel().block().createMessage("Whoops, something went wrong. Sorry about that.\n" + ex.getMessage()).block();
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            }
        }
    }

}
