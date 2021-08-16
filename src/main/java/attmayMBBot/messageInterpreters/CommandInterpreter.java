package attmayMBBot.messageInterpreters;

import attmayMBBot.commands.ICommand;
import attmayMBBot.commands.NotFoundCommand;
import attmayMBBot.commands.dinnerpostCommand.DinnerpostCommand;
import attmayMBBot.commands.goodnightCommand.GoodnightCommand;
import attmayMBBot.commands.jokeCommand.JokeCommand;
import attmayMBBot.commands.quoteCommand.AddQuoteCommand;
import attmayMBBot.commands.quoteCommand.AllQuotesCommand;
import attmayMBBot.commands.quoteCommand.RandomQuoteCommand;
import attmayMBBot.commands.uwuCommand.UwUCommand;
import attmayMBBot.config.AttmayMBBotConfig;
import discord4j.core.object.entity.Message;

import java.util.HashMap;

public class CommandInterpreter {
    private AttmayMBBotConfig config;
    private HashMap<String, ICommand> commandMap;
    private ICommand notFoundCommand;
    public CommandInterpreter(AttmayMBBotConfig config){
        this.config = config;
        this.commandMap = new HashMap<String, ICommand>();
        this.commandMap.put("!goodnight", new GoodnightCommand(this.config));
        this.commandMap.put("!addquote", new AddQuoteCommand(this.config));
        this.commandMap.put("!quotelist", new AllQuotesCommand(this.config));
        this.commandMap.put("!quote", new RandomQuoteCommand(this.config));
        this.commandMap.put("!joke", new JokeCommand(this.config));
        this.commandMap.put("!dinnerpost", new DinnerpostCommand(this.config));
        this.commandMap.put("!uwu", new UwUCommand(this.config));
        this.notFoundCommand = new NotFoundCommand();
    }
    public void interpretCommand(Message message){
        String messageContent = message.getContent();
        String[] args = messageContent.split(" ");
        //Execute the command with the most sexy of all methods: getOrDefault
        commandMap.getOrDefault(args[0].toLowerCase(), notFoundCommand).execute(message, args);
    }
}
