package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.IReactionCommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingInstance;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingManager;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;

public class AddQuoteRankingReactionCommand implements IReactionCommand {
    private AttmayMBBotConfig config;
    private QuoteRankingManager quoteRankingManager;

    public AddQuoteRankingReactionCommand(AttmayMBBotConfig config, QuoteRankingManager quoteRankingManager) {
        this.config = config;
        this.quoteRankingManager = quoteRankingManager;
    }

    @Override
    public void execute(User user, Message message, ReactionEmoji emoji) {
        QuoteRankingInstance instance = this.quoteRankingManager.getQuoteRankingInstanceByMessageId(message.getId().asLong());
        if(instance != null){
            //This means that the reaction was added to a quote quiz message.
            instance.submitAnswer(user, emoji);
        }
    }
}
