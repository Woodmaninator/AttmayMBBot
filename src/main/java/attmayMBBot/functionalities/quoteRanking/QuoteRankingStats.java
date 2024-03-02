package attmayMBBot.functionalities.quoteRanking;

import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.util.Pair;

public class QuoteRankingStats {
    private int wins;
    private int draws;
    private int losses;
    private Pair<String, Quote> quote;

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public Pair<String, Quote> getQuote() {
        return quote;
    }

    public QuoteRankingStats(Pair<String, Quote> quote){
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
        this.quote = quote;
    }

    public void addWin(){
        this.wins++;
    }
    public void addDraw(){
        this.draws++;
    }
    public void addLoss(){
        this.losses++;
    }
}
