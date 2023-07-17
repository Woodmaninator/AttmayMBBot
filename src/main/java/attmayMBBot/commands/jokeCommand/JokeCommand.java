package attmayMBBot.commands.jokeCommand;

import attmayMBBot.APIHandling.jokesAPIHandling.JokesAPIHandler;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public class JokeCommand implements ICommand {
    private AttmayMBBotConfig config;

    public JokeCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        //One-liner. Fuck yeah.
        channel.createMessage(new JokesAPIHandler().getRandomJokeAsString()).block();
    }
}
