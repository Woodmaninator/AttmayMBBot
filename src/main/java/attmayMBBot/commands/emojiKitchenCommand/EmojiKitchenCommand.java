package attmayMBBot.commands.emojiKitchenCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.emojiKitchen.EmojiKitchenHandler;
import discord4j.core.object.entity.Message;

public class EmojiKitchenCommand implements ICommand {
    private AttmayMBBotConfig config;

    public EmojiKitchenCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(Message message, String[] args) {
        if (args.length != 3) {
            message.getChannel().block().createMessage("This command feels wrong!\n !combine <emoji1> <emoji2>\nBe sure to keep in mind that you need a spacebar between the emojis!").block();
            return;
        }

        String emojiURL = EmojiKitchenHandler.getURLFromEmojis(args[1], args[2]);
        if (emojiURL == null) {
            message.getChannel().block().createMessage("No emoji found for the given combination").block();
            return;
        }
        message.getChannel().block().createMessage(emojiURL).block();
    }
}
