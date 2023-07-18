package attmayMBBot.commands.arcadeCommand.triviaCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.arcade.aline.EAlineDifficulty;
import attmayMBBot.functionalities.arcade.trivia.ETriviaDifficulty;
import attmayMBBot.functionalities.popupMessageHandling.PopupMessageHandler;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public class TriviaCommand implements ICommand {
    private AttmayMBBotConfig config;
    private ArcadeGameManager arcadeGameManager;

    public TriviaCommand(AttmayMBBotConfig config, ArcadeGameManager arcadeGameManager) {
        this.config = config;
        this.arcadeGameManager = arcadeGameManager;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        if(channel.getId().asLong() == this.config.getArcadeConfig().getTriviaChannelId()) {
            ETriviaDifficulty difficulty = ETriviaDifficulty.EASY;
            if (args.containsKey("difficulty")) {
                switch (args.get("difficulty").toLowerCase()) {
                    case "easy":
                        difficulty = ETriviaDifficulty.EASY;
                        break;
                    case "medium":
                        difficulty = ETriviaDifficulty.MEDIUM;
                        break;
                    case "hard":
                        difficulty = ETriviaDifficulty.HARD;
                        break;
                }
            }
            this.arcadeGameManager.addTriviaArcadeGameInstance(channel, sender, difficulty);
        } else {
            PopupMessageHandler.sendTemporaryMessageAndDeleteInvoker(channel,  sender.getMention() +
                    " You can only use this command in the <#" + this.config.getArcadeConfig().getTriviaChannelId()+  "> channel!", 30000);
        }
    }
}
