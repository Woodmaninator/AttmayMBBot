package attmayMBBot.commands.xkcdCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.xkcdUpdater.XKCDInfo;
import attmayMBBot.functionalities.xkcdUpdater.XKCDPostScraper;
import attmayMBBot.functionalities.xkcdUpdater.XKCDPoster;
import attmayMBBot.functionalities.xkcdUpdater.XKCDUpdater;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.Map;
import java.util.Random;

public class XKCDCommand implements ICommand {

    private AttmayMBBotConfig config;
    private XKCDUpdater xkcdUpdater;
    public XKCDCommand(AttmayMBBotConfig config, XKCDUpdater xkcdUpdater) {
        this.config = config;
        this.xkcdUpdater = xkcdUpdater;
    }
    @Override
    public void execute(Map<String, String> args, User sender, MessageChannel channel) {
        Integer number = new Random().nextInt(xkcdUpdater.getLatestXkcd()) + 1;

        if(args.containsKey("number")) {
            number = Integer.parseInt(args.get("number"));
        }

        XKCDInfo info = new XKCDPostScraper().getXkcd(number);

        if (args.containsKey("number") && number == 404) {
            channel.createMessage("Very funny. haha. 404. It doesn't exist.").block();
        } else if(info == null && args.containsKey("number")) {
            channel.createMessage("The XKCD with the number " + number + " does not exist.").block();
        } else if(number == 404) {
            channel.createMessage("Randomly decided to get XKCD number 404. Guess what - it doesn't exist.").block();
        } else {
            new XKCDPoster().postXKCD(info, channel);
        }
    }
}
