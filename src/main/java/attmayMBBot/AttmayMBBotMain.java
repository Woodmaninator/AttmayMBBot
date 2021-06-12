package attmayMBBot;

import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.messageInterpreters.MessageInterpreter;
import com.google.gson.Gson;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.io.FileReader;
import java.io.Reader;

public class AttmayMBBotMain {
    public static void main(String[] args) {
        try{
            Reader reader = new FileReader("AMBBConfig.json");
            AttmayMBBotConfig config = new Gson().fromJson(reader, AttmayMBBotConfig.class);
            reader.close();
            startBot(config);
        } catch(Exception ex){
            System.out.println("Something went terribly wrong when trying to read the config/start up the bot. You should get that fixed asap.");
            ex.printStackTrace();
        }
    }
    public static void startBot(AttmayMBBotConfig config){
        final DiscordClient client = DiscordClient.create(config.getToken());
        final GatewayDiscordClient gateway = client.login().block();

        MessageInterpreter messageInterpreter = new MessageInterpreter(config);

        //Event gets fired when the bot receives a message (private or in a text channel)
        gateway.on(MessageCreateEvent.class).subscribe(event -> {
           messageInterpreter.interpretMessage(event.getMessage());
        });

        gateway.onDisconnect().block();
    }
}
