package attmayMBBot.commands.emojiKitchenCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.emojiKitchen.EmojiKitchenHandler;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

public class EmojiKitchenCommand implements ICommand {
    private AttmayMBBotConfig config;

    public EmojiKitchenCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(String[] args, User sender, MessageChannel channel) {
        if (args.length != 3) {
            channel.createMessage("This command feels wrong!\n !combine <emoji1> <emoji2>\nBe sure to keep in mind that you need a spacebar between the emojis!").block();
            return;
        }

        String emojiURL = EmojiKitchenHandler.getURLFromEmojis(args[1], args[2]);
        if (emojiURL == null) {
            channel.createMessage("No emoji found for the given combination").block();
            return;
        }
        channel.createMessage(emojiURL).block();
    }
}
