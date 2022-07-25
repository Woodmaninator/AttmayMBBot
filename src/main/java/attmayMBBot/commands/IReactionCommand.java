package attmayMBBot.commands;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;

public interface IReactionCommand {
    public void execute(Member member, Message message, ReactionEmoji emoji);
}
