package attmayMBBot.commands;

import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class NotFoundCommand implements ICommand{
    @Override
    public void execute(String[] args, User sender, MessageChannel channel) {
        channel.createMessage("I have no idea what you want me to do...").block();
    }
}
