package attmayMBBot.commands.arcadeCommand.alineCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.arcade.aline.EAlineDifficulty;
import attmayMBBot.functionalities.popupMessageHandling.PopupMessageHandler;
import discord4j.core.object.entity.Message;

public class AlineCommand implements ICommand {
    private AttmayMBBotConfig config;
    private ArcadeGameManager arcadeGameManager;
    public AlineCommand(AttmayMBBotConfig config, ArcadeGameManager arcadeGameManager) {
        this.config = config;
        this.arcadeGameManager = arcadeGameManager;
    }
    @Override
    public void execute(Message message, String[] args) {
        //Check if command was sent in the right channel
        if(message.getChannel().block().getId().asLong() == this.config.getArcadeConfig().getAlineChannelId()) {
            EAlineDifficulty difficulty = EAlineDifficulty.EASY;
            if (args.length > 1) {
                //if the second argument is "tutorial"
                if (args[1].toLowerCase().equals("tutorial")) {
                    message.getChannel().block().createMessage("Move the red line to the blue target using the arrow reactions.\n" +
                            "The line will move until it hits a wall or the end of the board. The line will also stop if it collides with itself.\n" +
                            "Be careful not to hit any :skull: as they may result in a minor case of instant death.").block();
                    return;
                }
                //if the second argument is not tutorial
                switch (args[1].toLowerCase()) {
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
            this.arcadeGameManager.addAlineArcadeGameInstance(message, message.getAuthor().get().getId().asLong(), difficulty);
        } else {
            PopupMessageHandler.sendTemporaryMessageAndDeleteInvoker(message,  message.getAuthor().get().getMention() +
                    " You can only use this command in the <#" + this.config.getArcadeConfig().getAlineChannelId()+  "> channel!", 30000);
        }
    }
}
