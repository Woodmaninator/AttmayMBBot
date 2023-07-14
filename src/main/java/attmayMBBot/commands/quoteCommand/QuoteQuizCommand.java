package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class QuoteQuizCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteQuizManager quoteQuizManager;

    public QuoteQuizCommand(AttmayMBBotConfig config, QuoteQuizManager quoteQuizManager) {
        this.config = config;
        this.quoteQuizManager = quoteQuizManager;
    }

    @Override
    public void execute(String[] args, User sender, MessageChannel channel) {
        //ez
        this.quoteQuizManager.addQuoteQuizInstance(channel, sender);
    }
}
