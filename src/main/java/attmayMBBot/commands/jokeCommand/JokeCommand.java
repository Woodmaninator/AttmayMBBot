package attmayMBBot.commands.jokeCommand;

import attmayMBBot.APIHandling.jokesAPIHandling.JokesAPIHandler;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import discord4j.core.object.entity.Message;

public class JokeCommand implements ICommand {
    private AttmayMBBotConfig config;

    public JokeCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Message message, String[] args) {
        //One liner. Fuck yeah.
        message.getChannel().block().createMessage(new JokesAPIHandler().getRandomJokeAsString()).block();
    }
}
