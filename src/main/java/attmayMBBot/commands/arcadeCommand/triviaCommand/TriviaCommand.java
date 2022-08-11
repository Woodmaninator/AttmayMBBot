package attmayMBBot.commands.arcadeCommand.triviaCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.arcade.aline.EAlineDifficulty;
import attmayMBBot.functionalities.arcade.trivia.ETriviaDifficulty;
import attmayMBBot.functionalities.popupMessageHandling.PopupMessageHandler;
import discord4j.core.object.entity.Message;

public class TriviaCommand implements ICommand {
    private AttmayMBBotConfig config;
    private ArcadeGameManager arcadeGameManager;

    public TriviaCommand(AttmayMBBotConfig config, ArcadeGameManager arcadeGameManager) {
        this.config = config;
        this.arcadeGameManager = arcadeGameManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        if(message.getChannel().block().getId().asLong() == this.config.getArcadeConfig().getTriviaChannelId()) {
            ETriviaDifficulty difficulty = ETriviaDifficulty.EASY;
            if (args.length > 1) {
                switch (args[1].toLowerCase()) {
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
            this.arcadeGameManager.addTriviaArcadeGameInstance(message, message.getAuthor().get().getId().asLong(), difficulty);
        } else {
            PopupMessageHandler.sendTemporaryMessageAndDeleteInvoker(message,  message.getAuthor().get().getMention() +
                    " You can only use this command in the <#" + this.config.getArcadeConfig().getTriviaChannelId()+  "> channel!", 30000);
        }
    }
}
