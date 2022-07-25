package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import discord4j.core.object.entity.Message;

public class QuoteQuizCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteQuizManager quoteQuizManager;

    public QuoteQuizCommand(AttmayMBBotConfig config, QuoteQuizManager quoteQuizManager) {
        this.config = config;
        this.quoteQuizManager = quoteQuizManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        //ez
        this.quoteQuizManager.addQuoteQuizInstance(message, message.getAuthor().get().getMention(), message.getAuthor().get().getId().asLong());
    }
}
