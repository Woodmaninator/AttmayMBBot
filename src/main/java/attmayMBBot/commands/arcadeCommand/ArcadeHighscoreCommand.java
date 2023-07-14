package attmayMBBot.commands.arcadeCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeManager;
import attmayMBBot.functionalities.arcade.ArcadeUser;
import attmayMBBot.functionalities.popupMessageHandling.PopupMessageHandler;
import attmayMBBot.functionalities.quoteManagement.Quote;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ArcadeHighscoreCommand implements ICommand {
    private AttmayMBBotConfig config;
    private ArcadeManager arcadeManager;
    private GatewayDiscordClient gateway;

    public ArcadeHighscoreCommand(GatewayDiscordClient gateway, AttmayMBBotConfig config, ArcadeManager arcadeManager) {
        this.config = config;
        this.arcadeManager = arcadeManager;
        this.gateway = gateway;
    }

    @Override
    public void execute(String[] args, User sender, MessageChannel channel) {
        //CHeck if the message was sent in the right channel
        if(channel.getId().asLong() == this.config.getArcadeConfig().getGeneralArcadeChannelId()) {
            List<ArcadeUser> users = this.arcadeManager.getUsers();
            users.sort((x,y) -> y.getXp().compareTo(x.getXp()));

            List<String> embedDescriptions = new ArrayList<>();
            String embedTitle = "Highscore List";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < users.size(); i++) {
                String username = this.gateway.getUserById(Snowflake.of(users.get(i).getId())).block().getMention();
                String level = "" + this.arcadeManager.getUserLevel(users.get(i).getId());
                String nextEntry = (i + 1) + ". " + username + " Level " + level + " - " + users.get(i).getXp() + " XP";
                if (sb.toString().length() + (nextEntry + "\n\n").length() < 4096)
                    sb.append(nextEntry + "\n\n");
                else {
                    //In case the first embed Descriptions is full (limit of 4096), start another one and create a new StringBuilder instance
                    //The description gets stored in the embedDescription list
                    embedDescriptions.add(sb.toString());
                    sb = new StringBuilder(nextEntry + "\n\n");
                }
            }
            //After every user is done, add the last string in the StringBuilder to the list
            //But only if the length of the string is greater than zero
            if (sb.toString().length() > 0)
                embedDescriptions.add(sb.toString());
            //Just yeet them out there
            for (String embedDescription : embedDescriptions) {
                channel.createMessage(y -> y.setEmbed(x -> x.setTitle(embedTitle).setDescription(embedDescription).setColor(Color.of(0, 102, 102)))).block();
            }
        } else {
            PopupMessageHandler.sendTemporaryMessageAndDeleteInvoker(channel,  sender.getMention() +
                    " You can only use this command in the <#" + this.config.getArcadeConfig().getGeneralArcadeChannelId()+  "> channel!", 30000);
        }
    }
}
