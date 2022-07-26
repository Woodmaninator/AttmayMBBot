package attmayMBBot.commands;

import discord4j.core.object.entity.Message;

public class CommandsCommand implements ICommand{
    @Override
    public void execute(Message message, String[] args) {
        message.getChannel().block().createMessage("You can find a list of all the commands on the github page for this project:\nhttps://github.com/Woodmaninator/AttmayMBBot").block();
    }
}
