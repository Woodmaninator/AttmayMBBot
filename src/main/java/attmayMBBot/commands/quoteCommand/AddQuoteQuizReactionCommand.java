package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.IReactionCommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizInstance;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;

public class AddQuoteQuizReactionCommand implements IReactionCommand {
    private AttmayMBBotConfig config;
    private QuoteQuizManager quoteQuizManager;

    public AddQuoteQuizReactionCommand(AttmayMBBotConfig config, QuoteQuizManager quoteQuizManager) {
        this.config = config;
        this.quoteQuizManager = quoteQuizManager;
    }

    @Override
    public void execute(User user, Message message, ReactionEmoji emoji) {
        QuoteQuizInstance instance = this.quoteQuizManager.getQuoteQuizInstanceByMessageId(message.getId().asLong());
        if(instance != null){
            //This means that the reaction was added to a quote quiz message.
            instance.submitAnswer(user, emoji);
        }
    }
}
