package attmayMBBot.commands.arcadeCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeManager;
import attmayMBBot.functionalities.arcade.ArcadeUser;
import attmayMBBot.functionalities.popupMessageHandling.PopupMessageHandler;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

public class ArcadeXpCommand implements ICommand {
    private AttmayMBBotConfig config;
    private ArcadeManager arcadeManager;
    public ArcadeXpCommand(AttmayMBBotConfig config, ArcadeManager arcadeManager) {
        this.config = config;
        this.arcadeManager = arcadeManager;
    }
    @Override
    public void execute(String[] args, User sender, MessageChannel channel) {
        //Check if the message was sent in the right channel
        if(channel.getId().asLong() == this.config.getArcadeConfig().getGeneralArcadeChannelId()) {
            ArcadeUser user = null;
            if (this.arcadeManager.userExists(sender.getId().asLong())) {
                user = this.arcadeManager.getUser(sender.getId().asLong());
            } else {
                //add a new user to the arcade manager
                user = this.arcadeManager.addNewUser(sender.getId().asLong());
                this.arcadeManager.saveArcadeInfoToFile();
            }

            if (user != null) {
                EmbedCreateSpec embed = EmbedCreateSpec.builder()
                        .color(Color.of(0x006666))
                        .title("XP Information for " + sender.getMention())
                        .url("https://github.com/Woodmaninator/AttmayMBBot")
                        .description(this.arcadeManager.getLevelInfoStringFromUser(user.getId()))
                        .build();

                channel.createMessage(embed).block();
            }
        } else {
            PopupMessageHandler.sendTemporaryMessageAndDeleteInvoker(channel,  sender.getMention() +
                    " You can only use this command in the <#" + this.config.getArcadeConfig().getGeneralArcadeChannelId()+  "> channel!", 30000);
        }
    }
}
