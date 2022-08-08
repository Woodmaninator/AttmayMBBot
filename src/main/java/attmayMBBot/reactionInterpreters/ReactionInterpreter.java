package attmayMBBot.reactionInterpreters;

import attmayMBBot.commands.IReactionCommand;
import attmayMBBot.commands.arcadeCommand.alineCommand.AlineReactionCommand;
import attmayMBBot.commands.quoteCommand.AddQuoteQuizReactionCommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;

import java.util.ArrayList;
import java.util.List;

public class ReactionInterpreter {
    private AttmayMBBotConfig config;
    private List<IReactionCommand> addedReactionCommands;

    public ReactionInterpreter(AttmayMBBotConfig config, QuoteQuizManager quoteQuizManager, ArcadeGameManager arcadeGameManager) {
        this.addedReactionCommands = new ArrayList<>();
        this.addedReactionCommands.add(new AddQuoteQuizReactionCommand(config, quoteQuizManager));
        this.addedReactionCommands.add(new AlineReactionCommand(config, arcadeGameManager));
    }

    public void interpretAddedReaction(User user, Message message, ReactionEmoji emoji){
        //This executes the execute method of ALL added reaction commands, because it is not possible to know which command should be executed just by looking at the emoji.
        try {
            for (IReactionCommand command : this.addedReactionCommands)
                command.execute(user, message, emoji);
        } catch(Exception ex){
            ex.printStackTrace();
            message.getChannel().block().createMessage("Whoops, something went wrong. Sorry about that.\n" + ex.getMessage()).block();
        }
    }

    //Same principle for interpretRemovedReaction, but none of these exist yet
}
