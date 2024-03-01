package attmayMBBot;

import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.arcade.ArcadeManager;
import attmayMBBot.functionalities.emojiKitchen.EmojiCombinationHandler;
import attmayMBBot.functionalities.emojiKitchen.EmojiKitchenHandler;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingManager;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingResults;
import attmayMBBot.functionalities.xkcdUpdater.XKCDUpdater;
import attmayMBBot.messageInterpreters.CommandInterpreter;
import attmayMBBot.messageInterpreters.MessageInterpreter;
import attmayMBBot.reactionInterpreters.ReactionInterpreter;
import com.google.gson.Gson;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.InteractionCallbackSpec;

import java.io.FileReader;
import java.io.Reader;

public class AttmayMBBotMain {
    public static void main(String[] args) {
        try{
            Reader reader = new FileReader("AMBBConfig.json");
            AttmayMBBotConfig config = new Gson().fromJson(reader, AttmayMBBotConfig.class);
            reader.close();
            config.setToken(System.getenv("AMBB_TOKEN"));
            config.setSpoonacularApiKey(System.getenv("AMBB_SPOONACULAR_KEY"));

            reader = new FileReader("AMBBQuotes.json");
            QuoteManager quoteManager = new Gson().fromJson(reader, QuoteManager.class);
            reader.close();

            reader = new FileReader("AMBBQuoteRanking.json");
            QuoteRankingResults quoteRankingResults = new Gson().fromJson(reader, QuoteRankingResults.class);
            reader.close();

            reader = new FileReader("AMBBArcade.json");
            ArcadeManager arcadeManager = new Gson().fromJson(reader, ArcadeManager.class);
            reader.close();

            reader = new FileReader("EmojiKitchen.json");
            EmojiCombinationHandler emojiCombinationHandler = new Gson().fromJson(reader, EmojiCombinationHandler.class);
            EmojiKitchenHandler.setEmojiCombinationHandler(emojiCombinationHandler);

            reader = new FileReader("AMBBXKCDConfig.json");
            XKCDUpdater xkcdUpdater = new Gson().fromJson(reader, XKCDUpdater.class);

            startBot(config, quoteManager, quoteRankingResults, arcadeManager, xkcdUpdater);
        } catch(Exception ex){
            System.out.println("Something went terribly wrong when trying to read the config/start up the bot. You should get that fixed asap.");
            ex.printStackTrace();
        }
    }
    public static void startBot(AttmayMBBotConfig config, QuoteManager quoteManager, QuoteRankingResults quoteRankingResults, ArcadeManager arcadeManager, XKCDUpdater xkcdUpdater){
        final DiscordClient client = DiscordClient.create(config.getToken());
        final GatewayDiscordClient gateway = client.login().block();

        QuoteQuizManager quoteQuizManager = new QuoteQuizManager(quoteManager);
        ArcadeGameManager arcadeGameManager = new ArcadeGameManager(arcadeManager);
        QuoteRankingManager quoteRankingManager = new QuoteRankingManager(quoteManager, quoteRankingResults);

        MessageInterpreter messageInterpreter = new MessageInterpreter(gateway, config, quoteManager, quoteRankingManager, quoteRankingResults, arcadeManager, quoteQuizManager, arcadeGameManager);
        CommandInterpreter commandInterpreter = new CommandInterpreter(gateway, config, quoteManager, quoteRankingManager, quoteRankingResults, arcadeManager, new QuoteIDManager(quoteManager.getQuoteAuthors()), quoteQuizManager, arcadeGameManager);
        ReactionInterpreter reactionInterpreter = new ReactionInterpreter(config, quoteQuizManager, quoteRankingManager, arcadeGameManager);

        //Event gets fired when the bot receives a message (private or in a text channel)
        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            messageInterpreter.interpretMessage(event.getMessage());
        });

        //Event gets fired when the bot receives a reaction (in a text channel)
        gateway.on(ReactionAddEvent.class).subscribe(event -> {
            reactionInterpreter.interpretAddedReaction(event.getUser().block(), event.getMessage().block(), event.getEmoji());
        });

        //Handle slash commands
        gateway.on(ChatInputInteractionEvent.class).subscribe(event -> {
            //Delegate to the command interpreter to keep main clean

            //Get the channel and the user
            MessageChannel channel = event.getInteraction().getChannel().block();
            User sender = event.getInteraction().getUser();

            StringBuilder replyBuilder = new StringBuilder();
            replyBuilder.append("Executing the following command: /" + event.getCommandName() + "\n");
            for(ApplicationCommandInteractionOption option : event.getOptions()){
                if(option.getValue().isPresent())
                    replyBuilder.append("-" + option.getName() + ": " + option.getValue().get().getRaw() + "\n");
            }

            //Forward the command, the channel and the sender to the command interpreter
            try {
                event.reply(replyBuilder.toString()).block();
            } catch(Throwable t) {
                t.printStackTrace();
            }

            commandInterpreter.interpretCommand(event.getCommandName(), event.getOptions(), sender, channel);
        });

        //Start the update loop for the quote quiz manager
        quoteQuizManager.startUpdateLoop();

        //Start the update loop for the quote ranking manager
        quoteRankingManager.startUpdateLoop();

        //Start the update loop for the arcade manager
        arcadeGameManager.startUpdateLoop();

        //Start the update loop for the xkcd updater
        Long xkcdChannelId = xkcdUpdater.getXkcdChannelId();
        MessageChannel xkcdChannel = gateway.getChannelById(Snowflake.of(xkcdChannelId)).ofType(MessageChannel.class).block();
        xkcdUpdater.startUpdateLoop(xkcdChannel);

        gateway.onDisconnect().block();
    }
}
