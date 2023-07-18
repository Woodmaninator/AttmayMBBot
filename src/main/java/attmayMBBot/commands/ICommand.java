package attmayMBBot.commands;

import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public interface ICommand {
    public void execute(Map<String, String> args, User sender, MessageChannel channel);
}
