package attmayMBBot.commands;

import discord4j.core.object.entity.Message;

public interface ICommand {
    public void execute(Message message, String[] args);
}
