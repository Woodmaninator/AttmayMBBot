package attmayMBBot.commands.uwuCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.UwUifyer;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class UwUCommand implements ICommand {
    private AttmayMBBotConfig config;

    public UwUCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Message message, String[] args) {
        String normalMessage = "";
        if(args.length > 1) {
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < args.length; i++)
                sb.append(args[i]).append(" ");
            normalMessage = sb.toString();
        }
        else {
            //Get the last message from that channel (excluding the one that is the command)
            Message lastMessage = message.getChannel().block().getMessagesBefore(message.getId()).blockFirst();
            if (lastMessage != null)
                normalMessage = lastMessage.getContent();
            else {
                message.getChannel().block().createMessage("Whoops! something went wrong.").block();
                return;
            }
        }
        message.getChannel().block().createMessage(new UwUifyer().uwuify(normalMessage)).block();
    }
}
