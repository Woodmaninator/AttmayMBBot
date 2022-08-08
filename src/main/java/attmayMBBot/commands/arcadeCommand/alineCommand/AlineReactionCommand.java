package attmayMBBot.commands.arcadeCommand.alineCommand;

import attmayMBBot.commands.IReactionCommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.AArcadeGameInstance;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;

public class AlineReactionCommand implements IReactionCommand {
    private AttmayMBBotConfig config;
    private ArcadeGameManager arcadeGameManager;
    public AlineReactionCommand(AttmayMBBotConfig config, ArcadeGameManager arcadeGameManager) {
        this.config = config;
        this.arcadeGameManager = arcadeGameManager;
    }
    @Override
    public void execute(User user, Message message, ReactionEmoji emoji) {
        //Nothing to do yet
        AArcadeGameInstance gameInstance = this.arcadeGameManager.getAlineArcadeGameInstanceByMessageId(message.getId().asLong());
        if(gameInstance != null){
            gameInstance.submitReaction(user, emoji);
        }
    }
}
