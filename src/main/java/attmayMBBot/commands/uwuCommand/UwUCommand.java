package attmayMBBot.commands.uwuCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.UwUifyer;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class UwUCommand implements ICommand {
    private AttmayMBBotConfig config;

    public UwUCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(String[] args, User sender, MessageChannel channel) {
        String normalMessage = "";
        if(args.length > 1) {
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            normalMessage = sb.toString();
        }
        else {
            //Get the last message from that channel (excluding the one that is the command)
            //I am not too sure that this will work, but we can hope
            Message lastMessage = channel.getMessagesBefore(channel.getLastMessage().block().getId()).blockFirst();
            if (lastMessage != null)
                normalMessage = lastMessage.getContent();
            else {
                channel.createMessage("Whoops! something went wrong.").block();
                return;
            }
        }
        channel.createMessage(new UwUifyer().uwuify(normalMessage)).block();
    }
}
