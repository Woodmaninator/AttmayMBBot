package attmayMBBot.messageInterpreters.messageTextInterpreters;

import discord4j.core.object.entity.Message;

public class GMMessageTextInterpreter implements IMessageTextInterpreter{
    @Override
    public void interpretMessageText(Message message) {
        String messageText = message.getContent();
        if(messageText.toLowerCase().startsWith("gm")){
            //Message is GM
            message.getChannel().block().createMessage("gm " + message.getAuthor().get().getMention()).block();
        }
    }
}
