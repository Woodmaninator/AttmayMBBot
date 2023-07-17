package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.popupMessageHandling.PopupMessageHandler;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public class QuoteRankingCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteRankingManager quoteRankingManager;

    public QuoteRankingCommand(AttmayMBBotConfig config, QuoteRankingManager quoteRankingManager) {
        this.config = config;
        this.quoteRankingManager = quoteRankingManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        //Check if command was sent in the right channel
        if(channel.getId().asLong() == this.config.getQuoteRankingChannelID()) {
            this.quoteRankingManager.addQuoteRankingInstance(channel, sender);
        } else {
            PopupMessageHandler.sendTemporaryMessageAndDeleteInvoker(channel,  sender.getMention() +
                    " You can only use this command in the <#" + this.config.getQuoteRankingChannelID() +  "> channel!", 30000);
        }
    }
}
