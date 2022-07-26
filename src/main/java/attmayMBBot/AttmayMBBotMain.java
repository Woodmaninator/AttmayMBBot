package attmayMBBot;

import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import attmayMBBot.messageInterpreters.MessageInterpreter;
import attmayMBBot.reactionInterpreters.ReactionInterpreter;
import com.google.gson.Gson;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;

import java.io.FileReader;
import java.io.Reader;

public class AttmayMBBotMain {
    public static void main(String[] args) {
        try{
            Reader reader = new FileReader("AMBBConfig.json");
            AttmayMBBotConfig config = new Gson().fromJson(reader, AttmayMBBotConfig.class);
            reader.close();
            reader = new FileReader("AMBBQuotes.json");
            QuoteManager quoteManager = new Gson().fromJson(reader, QuoteManager.class);
            reader.close();

            startBot(config, quoteManager);
        } catch(Exception ex){
            System.out.println("Something went terribly wrong when trying to read the config/start up the bot. You should get that fixed asap.");
            ex.printStackTrace();
        }
    }
    public static void startBot(AttmayMBBotConfig config, QuoteManager quoteManager){
        final DiscordClient client = DiscordClient.create(config.getToken());
        final GatewayDiscordClient gateway = client.login().block();

        QuoteQuizManager quoteQuizManager = new QuoteQuizManager(quoteManager);

        MessageInterpreter messageInterpreter = new MessageInterpreter(config, quoteManager, quoteQuizManager );
        ReactionInterpreter reactionInterpreter = new ReactionInterpreter(config, quoteQuizManager);

        //Event gets fired when the bot receives a message (private or in a text channel)
        gateway.on(MessageCreateEvent.class).subscribe(event -> {
           messageInterpreter.interpretMessage(event.getMessage());
        });

        //Event gets fired when the bot receives a reaction (in a text channel)
        gateway.on(ReactionAddEvent.class).subscribe(event -> {
            reactionInterpreter.interpretAddedReaction(event.getUser().block(), event.getMessage().block(), event.getEmoji());
        });

        //Start the update loop for the quote quiz manager
        quoteQuizManager.startUpdateLoop();

        gateway.onDisconnect().block();
    }
}
