package attmayMBBot.commands;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public interface ICommand {
    public void execute(String[] args, User sender, MessageChannel channel);
}
