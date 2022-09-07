package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.aline.EAlineDifficulty;
import attmayMBBot.functionalities.popupMessageHandling.PopupMessageHandler;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingManager;
import discord4j.core.object.entity.Message;

public class QuoteRankingCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteRankingManager quoteRankingManager;

    public QuoteRankingCommand(AttmayMBBotConfig config, QuoteRankingManager quoteRankingManager) {
        this.config = config;
        this.quoteRankingManager = quoteRankingManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        //Check if command was sent in the right channel
        if(message.getChannel().block().getId().asLong() == this.config.getQuoteRankingChannelID()) {
            this.quoteRankingManager.addQuoteRankingInstance(message, message.getAuthor().get().getMention(), message.getAuthor().get().getId().asLong());
        } else {
            PopupMessageHandler.sendTemporaryMessageAndDeleteInvoker(message,  message.getAuthor().get().getMention() +
                    " You can only use this command in the <#" + this.config.getQuoteRankingChannelID() +  "> channel!", 30000);
        }
    }
}
