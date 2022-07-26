package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;
import javafx.util.Pair;

import java.util.List;
import java.util.Random;

public class RandomQuoteCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;

    public RandomQuoteCommand(AttmayMBBotConfig config, QuoteManager quoteManager) {
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        //Smooth operator
        List<Pair<String, Quote>> quotePairs = this.quoteManager.getAllQuotesSortedByIssuedDate();
        if(quotePairs.size() > 0) {
            Random random = new Random();
            Pair<String, Quote> randomPair = quotePairs.get(random.nextInt(quotePairs.size()));
            message.getChannel().block().createMessage(randomPair.getValue().getQuoteText() + " - " + randomPair.getKey() + ", " + randomPair.getValue().getQuoteYear()).block();
        } else
            message.getChannel().block().createMessage("There are no quotes in the system yet.").block();
    }
}
