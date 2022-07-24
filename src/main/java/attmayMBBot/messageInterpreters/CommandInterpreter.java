package attmayMBBot.messageInterpreters;

import attmayMBBot.commands.ICommand;
import attmayMBBot.commands.NotFoundCommand;
import attmayMBBot.commands.dinnerpostCommand.DinnerpostCommand;
import attmayMBBot.commands.goodnightCommand.GoodnightCommand;
import attmayMBBot.commands.jokeCommand.JokeCommand;
import attmayMBBot.commands.quoteCommand.*;
import attmayMBBot.commands.uwuCommand.UwUCommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;

import java.util.HashMap;

public class CommandInterpreter {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;
    private QuoteIDManager quoteIDManager;
    private HashMap<String, ICommand> commandMap;
    private ICommand notFoundCommand;
    public CommandInterpreter(AttmayMBBotConfig config, QuoteManager quoteManager, QuoteIDManager quoteIDManager){
        this.config = config;
        this.quoteManager = quoteManager;
        this.quoteIDManager = quoteIDManager;
        this.commandMap = new HashMap<String, ICommand>();
        this.commandMap.put("!goodnight", new GoodnightCommand(this.config));
        this.commandMap.put("!addquote", new AddQuoteCommand(this.config, this.quoteManager, this.quoteIDManager));
        this.commandMap.put("!quotelist", new AllQuotesCommand(this.config, this.quoteManager));
        this.commandMap.put("!quote", new RandomQuoteCommand(this.config, this.quoteManager));
        this.commandMap.put("!joke", new JokeCommand(this.config));
        this.commandMap.put("!dinnerpost", new DinnerpostCommand(this.config));
        this.commandMap.put("!uwu", new UwUCommand(this.config));
        this.commandMap.put("!addauthor", new AddQuoteAuthorCommand(this.config, this.quoteManager));
        this.commandMap.put("!addalias", new AddQuoteAuthorAliasCommand(this.config, this.quoteManager));
        this.notFoundCommand = new NotFoundCommand();
    }
    public void interpretCommand(Message message){
        String messageContent = message.getContent();
        String[] args = messageContent.split(" ");
        //Execute the command with the most sexy of all methods: getOrDefault
        commandMap.getOrDefault(args[0].toLowerCase(), notFoundCommand).execute(message, args);
    }
}
