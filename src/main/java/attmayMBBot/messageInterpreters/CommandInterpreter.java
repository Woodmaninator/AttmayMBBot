package attmayMBBot.messageInterpreters;

import attmayMBBot.commands.CommandsCommand;
import attmayMBBot.commands.ICommand;
import attmayMBBot.commands.NotFoundCommand;
import attmayMBBot.commands.arcadeCommand.ArcadeHighscoreCommand;
import attmayMBBot.commands.arcadeCommand.ArcadeXpCommand;
import attmayMBBot.commands.arcadeCommand.alineCommand.AlineCommand;
import attmayMBBot.commands.arcadeCommand.triviaCommand.TriviaCommand;
import attmayMBBot.commands.dinnerpostCommand.DinnerpostCommand;
import attmayMBBot.commands.emojiKitchenCommand.EmojiKitchenCommand;
import attmayMBBot.commands.goodnightCommand.GoodnightCommand;
import attmayMBBot.commands.jokeCommand.JokeCommand;
import attmayMBBot.commands.quoteCommand.*;
import attmayMBBot.commands.uwuCommand.UwUCommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.arcade.ArcadeGameManager;
import attmayMBBot.functionalities.arcade.ArcadeManager;
import attmayMBBot.functionalities.quoteManagement.QuoteIDManager;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizManager;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingManager;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingResults;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.HashMap;

public class CommandInterpreter {
    private AttmayMBBotConfig config;
    private HashMap<String, ICommand> commandMap;
    private ICommand notFoundCommand;
    public CommandInterpreter(GatewayDiscordClient gateway, AttmayMBBotConfig config, QuoteManager quoteManager, QuoteRankingManager quoteRankingManager, QuoteRankingResults quoteRankingResults, ArcadeManager arcadeManager, QuoteIDManager quoteIDManager, QuoteQuizManager quoteQuizManager, ArcadeGameManager arcadeGameManager) {
        this.config = config;
        this.commandMap = new HashMap<String, ICommand>();
        this.commandMap.put("goodnight", new GoodnightCommand(config));
        this.commandMap.put("addquote", new AddQuoteCommand(config, quoteManager, quoteIDManager));
        this.commandMap.put("editquote", new EditQuoteCommand(config, quoteManager, quoteIDManager));
        this.commandMap.put("removequote", new RemoveQuoteCommand(config, quoteManager, quoteIDManager));
        this.commandMap.put("quotelist", new AllQuotesCommand(config, quoteManager));
        this.commandMap.put("quoteidlist", new AllQuoteIDsCommand(config, quoteManager));
        this.commandMap.put("quote", new GetQuoteCommand(config, quoteManager, quoteIDManager));
        this.commandMap.put("joke", new JokeCommand(config));
        this.commandMap.put("dinnerpost", new DinnerpostCommand(config));
        this.commandMap.put("uwu", new UwUCommand(config));
        this.commandMap.put("addauthor", new AddQuoteAuthorCommand(config, quoteManager));
        this.commandMap.put("renameauthor", new RenameQuoteAuthorCommand(config, quoteManager));
        this.commandMap.put("removeauthor", new RemoveQuoteAuthorCommand(config, quoteManager));
        this.commandMap.put("authorlist", new AllAuthorsCommand(config, quoteManager));
        this.commandMap.put("addalias", new AddQuoteAuthorAliasCommand(config, quoteManager));
        this.commandMap.put("removealias", new RemoveQuoteAuthorAliasCommand(config, quoteManager));
        this.commandMap.put("quotequiz", new QuoteQuizCommand(config, quoteQuizManager));
        this.commandMap.put("commands", new CommandsCommand());
        this.commandMap.put("aline", new AlineCommand(config, arcadeGameManager));
        this.commandMap.put("xp", new ArcadeXpCommand(config, arcadeManager));
        this.commandMap.put("highscore", new ArcadeHighscoreCommand(gateway, config, arcadeManager));
        this.commandMap.put("trivia", new TriviaCommand(config, arcadeGameManager));
        this.commandMap.put("combine", new EmojiKitchenCommand(config));
        this.commandMap.put("rankquotes", new QuoteRankingCommand(config, quoteRankingManager));
        this.commandMap.put("rankedquotelist", new RankedQuoteListCommand(config, quoteRankingResults, quoteManager));
        this.notFoundCommand = new NotFoundCommand();
    }
    public void interpretCommand(String command, User sender, MessageChannel channel){
        String[] args = command.split(" ");
        //Since the command interpreter is no longer executed within the message interpreter, exception handling needs to happen here too
        try{
            //Execute the command with the most sexy of all methods: getOrDefault
            commandMap.getOrDefault(args[0].toLowerCase(), notFoundCommand).execute(args, sender, channel);
        } catch (Exception ex) {
            ex.printStackTrace();
            try { //Try to send a message to the channel, but if there's something wrong with the channel, that might not work either and break the entire bot
                channel.createMessage("Whoops, something went wrong. Sorry about that.\n" + ex.getMessage()).block();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
    }
}
