package attmayMBBot.reactionInterpreters;

import attmayMBBot.commands.IReactionCommand;
import attmayMBBot.commands.quoteCommand.AddQuoteQuizReactionCommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;

import java.util.ArrayList;
import java.util.List;

public class ReactionInterpreter {
    private AttmayMBBotConfig config;
    private QuoteQuizManager quoteQuizManager;
    private List<IReactionCommand> addedReactionCommands;

    public ReactionInterpreter(AttmayMBBotConfig config, QuoteQuizManager quoteQuizManager) {
        this.quoteQuizManager = quoteQuizManager;
        this.addedReactionCommands = new ArrayList<>();
        this.addedReactionCommands.add(new AddQuoteQuizReactionCommand(config, quoteQuizManager));
    }

    public void interpretAddedReaction(Member member, Message message, ReactionEmoji emoji){
        //This executes the execute method of ALL added reaction commands, because it is not possible to know which command should be executed just by looking at the emoji.
        for(IReactionCommand command : this.addedReactionCommands)
            command.execute(member, message, emoji);
    }

    //Same principle for interpretRemovedReaction, but none of these exist yet
}
