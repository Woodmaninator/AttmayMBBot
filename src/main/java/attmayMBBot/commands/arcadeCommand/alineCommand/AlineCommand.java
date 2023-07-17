package attmayMBBot.commands.arcadeCommand.alineCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.arcade.aline.EAlineDifficulty;
import attmayMBBot.functionalities.popupMessageHandling.PopupMessageHandler;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public class AlineCommand implements ICommand {
    private AttmayMBBotConfig config;
    private ArcadeGameManager arcadeGameManager;
    public AlineCommand(AttmayMBBotConfig config, ArcadeGameManager arcadeGameManager) {
        this.config = config;
        this.arcadeGameManager = arcadeGameManager;
    }
    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        //Check if command was sent in the right channel
        if(channel.getId().asLong() == this.config.getArcadeConfig().getAlineChannelId()) {
            EAlineDifficulty difficulty = EAlineDifficulty.EASY;
            if (args.containsKey("mode")) {
                //if the second argument is "tutorial"
                if (args.get("mode").toLowerCase().equals("tutorial")) {
                    channel.createMessage("Move the red line to the blue target using the arrow reactions.\n" +
                            "The line will move until it hits a wall or the end of the board. The line will also stop if it collides with itself.\n" +
                            "Be careful not to hit any :skull: as they may result in a minor case of instant death.").block();
                    return;
                }
                //if the second argument is not tutorial
                switch (args.get("mode").toLowerCase()) {
                    case "easy":
                        difficulty = EAlineDifficulty.EASY;
                        break;
                    case "medium":
                        difficulty = EAlineDifficulty.MEDIUM;
                        break;
                    case "hard":
                        difficulty = EAlineDifficulty.HARD;
                        break;
                }
            }
            this.arcadeGameManager.addAlineArcadeGameInstance(channel, sender, difficulty);
        } else {
            PopupMessageHandler.sendTemporaryMessageAndDeleteInvoker(channel,  sender.getMention() +
                    " You can only use this command in the <#" + this.config.getArcadeConfig().getAlineChannelId()+  "> channel!", 30000);
        }
    }
}
