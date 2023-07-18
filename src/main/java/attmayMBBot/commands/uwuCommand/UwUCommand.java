package attmayMBBot.commands.uwuCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.UwUifyer;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public class UwUCommand implements ICommand {
    private AttmayMBBotConfig config;

    public UwUCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        String normalMessage = "";
        if(args.containsKey("text")) {
            normalMessage = args.get("text");
        }
        else {
            //Get the last message from that channel (excluding the one that is the command)
            //I am not too sure that this will work, but we can hope
            Message lastMessage = channel.getLastMessage().block();
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
