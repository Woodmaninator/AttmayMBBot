package attmayMBBot.commands.emojiKitchenCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.emojiKitchen.EmojiKitchenHandler;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;

public class EmojiKitchenCommand implements ICommand {
    private AttmayMBBotConfig config;

    public EmojiKitchenCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        if (!args.containsKey("emoji1") || !args.containsKey("emoji2")) {
            channel.createMessage("This command feels wrong!\n /combine <emoji1> <emoji2>").block();
            return;
        }

        String emojiURL = EmojiKitchenHandler.getURLFromEmojis(args.get("emoji1"), args.get("emoji2"));
        if (emojiURL == null) {
            channel.createMessage("No emoji found for the given combination").block();
            return;
        }
        channel.createMessage(emojiURL).block();
    }
}
