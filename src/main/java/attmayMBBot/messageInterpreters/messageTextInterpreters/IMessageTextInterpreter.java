package attmayMBBot.messageInterpreters.messageTextInterpreters;

import discord4j.core.object.entity.Message;

public interface IMessageTextInterpreter {
    public void interpretMessageText(Message message);
}
