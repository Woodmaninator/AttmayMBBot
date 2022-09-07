package attmayMBBot.commands;

import discord4j.core.object.entity.Message;

public class NotFoundCommand implements ICommand{
    @Override
    public void execute(Message message, String[] args) {
        message.getChannel().block().createMessage("I have no idea what you want me to do...").block();
    }
}
