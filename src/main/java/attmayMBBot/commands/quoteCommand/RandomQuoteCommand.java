package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import discord4j.core.object.entity.Message;

import java.util.Random;

public class RandomQuoteCommand implements ICommand {
    private AttmayMBBotConfig config;

    public RandomQuoteCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Message message, String[] args) {
        //This is way too short
        //No way this is gonna work
        Random random = new Random();
        message.getChannel().block().createMessage(this.config.getQuoteConfig().getQuotes().get(random.nextInt(this.config.getQuoteConfig().getQuotes().size()))).block();
    }
}
