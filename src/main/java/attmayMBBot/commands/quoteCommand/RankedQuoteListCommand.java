package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteRanking.QuoteDuel;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingResults;
import attmayMBBot.functionalities.quoteRanking.QuoteRankingStats;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class RankedQuoteListCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteRankingResults results;
    private QuoteManager quoteManager;

    public RankedQuoteListCommand(AttmayMBBotConfig config, QuoteRankingResults results, QuoteManager quoteManager) {
        this.config = config;
        this.results = results;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        int count = 10; //The default number of quotes to show
        String authorName = ""; //Default author name aka everyone
        //Second argument is the number of quotes to show
        if(args.length > 1){
            try{
                count = Integer.parseInt(args[1]);
            }catch (NumberFormatException e){
                //not a number (obviously), so the second argument is the author name
                authorName = args[1];
                if(args.length > 2){
                    //third argument could be the number of quotes to show (if the second argument was the author name)
                    try{
                        count = Integer.parseInt(args[2]);
                    }catch(NumberFormatException e2) {
                        //eh. not that important
                    }
                }
            }
        }

        List<QuoteRankingStats> stats = new ArrayList<>();
        //Get the quotes and sort them by wins or some shit
        //loop through all quotes
        List<Pair<String, Quote>> quotes = null;
        if(!authorName.equals("")){
            if(quoteManager.checkIfQuoteAuthorNameExists(authorName)){
                quotes = quoteManager.getAllQuotesFromAuthorSortedByIssuedDate(authorName);
            }else{
                message.getChannel().block().createMessage("Your specified author name does not exist!").block();
                return;
            }
        } else {
            quotes = this.quoteManager.getAllQuotesSortedByIssuedDate();
        }

        for(Pair<String, Quote> quote : quotes){
            //Get the stats for the quote
            QuoteRankingStats quoteStats = new QuoteRankingStats(quote);
            //Loop through the quote duel in the results
            for(QuoteDuel duel : this.results.getDuelList()){
                //If the quote is in the duel, add the stats to the quote stats
                if(duel.getQuote1Id() == quote.getValue().getId()){
                    //Quote 1 is the quote so we check the quote1Votes
                    if(duel.getQuote1Votes() > duel.getQuote2Votes())
                        quoteStats.addWin();
                    else if(duel.getQuote1Votes() < duel.getQuote2Votes())
                        quoteStats.addLoss();
                    else
                        quoteStats.addDraw();
                } else if(duel.getQuote2Id() == quote.getValue().getId()){
                    //Quote 2 is the quote so we check the quote2Votes
                    if(duel.getQuote2Votes() > duel.getQuote1Votes())
                        quoteStats.addWin();
                    else if(duel.getQuote2Votes() < duel.getQuote1Votes())
                        quoteStats.addLoss();
                    else
                        quoteStats.addDraw();
                }
            }
            stats.add(quoteStats);
        }

        //All quotes have stats now. It is time to sort them now

        stats.sort( (x,y) -> {
           if(x.getWins() != y.getWins())
               return y.getWins() - x.getWins();
           else if(x.getLosses() != y.getLosses())
               return x.getLosses() - y.getLosses();
           else if(x.getDraws() != y.getDraws())
               return y.getDraws() - x.getDraws();
           return 0;
        });

        if(count < 1)
            count = 1;
        if(count > stats.size())
            count = stats.size();

        //Print the quotes
        if(stats.size() < 1) //Cancel if there are no quotes
            return;
        List<String> embedDescriptions = new ArrayList<>();
        String embedTitle = "list of ranked Quotes (Wins|Draws|Losses)";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            String statString = "(" + stats.get(i).getWins() + "|" + stats.get(i).getDraws() + "|" + stats.get(i).getLosses() + ")";
            String nextQuote = (i + 1) + ". " + statString  + " " + stats.get(i).getQuote().getValue().getQuoteText() + " - " + stats.get(i).getQuote().getKey() + ", " + stats.get(i).getQuote().getValue().getQuoteYear();
            if (sb.toString().length() + (nextQuote + "\n\n").length() < 4096)
                sb.append(nextQuote + "\n\n");
            else {
                //In case the first embed Descriptions is full (limit of 4096), start another one and create a new StringBuilder instance
                //The description gets stored in the embedDescription list
                embedDescriptions.add(sb.toString());
                sb = new StringBuilder(nextQuote + "\n\n");
            }
        }
        //After every quote is done, add the last string in the StringBuilder to the list
        //But only if the length of the string is greater than one
        if (sb.toString().length() > 0)
            embedDescriptions.add(sb.toString());
        //Just yeet them out there
        for (String embedDescription : embedDescriptions) {
            message.getChannel().block().createMessage(y -> y.setEmbed(x -> x.setTitle(embedTitle).setDescription(embedDescription).setColor(Color.of(0, 102, 102)))).block();
        }
    }
}