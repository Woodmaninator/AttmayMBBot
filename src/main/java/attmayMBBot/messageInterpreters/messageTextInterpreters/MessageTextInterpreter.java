package attmayMBBot.messageInterpreters.messageTextInterpreters;

import attmayMBBot.config.AttmayMBBotConfig;
import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageTextInterpreter implements IMessageTextInterpreter{
    private AttmayMBBotConfig config;
    private List<IMessageTextInterpreter> messageTextInterpreters;
    public MessageTextInterpreter(AttmayMBBotConfig config){
        this.config = config;
        this.messageTextInterpreters = new ArrayList<>();
        this.messageTextInterpreters.add(new GMMessageTextInterpreter());
    }
    @Override
    public void interpretMessageText(Message message) {
        for(IMessageTextInterpreter messageTextInterpreter : this.messageTextInterpreters)
            messageTextInterpreter.interpretMessageText(message);
    }
}
