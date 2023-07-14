package attmayMBBot.commands;

import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class CommandsCommand implements ICommand{
    @Override
    public void execute(String[] args, User user, MessageChannel channel) {
        channel.createMessage("You can find a list of all the commands on the github page for this project:\nhttps://github.com/Woodmaninator/AttmayMBBot#commands").block();
    }
}
