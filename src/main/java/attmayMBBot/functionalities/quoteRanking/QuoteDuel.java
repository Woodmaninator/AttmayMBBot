package attmayMBBot.functionalities.quoteRanking;

import java.util.ArrayList;
import java.util.List;

public class QuoteDuel {
    private long quote1Id;
    private long quote2Id;
    private int quote1Votes;
    private int quote2Votes;
    private List<Long> voterIds;

    public long getQuote1Id() {
        return quote1Id;
    }

    public long getQuote2Id() {
        return quote2Id;
    }

    public int getQuote1Votes() {
        return quote1Votes;
    }

    public int getQuote2Votes() {
        return quote2Votes;
    }
    public List<Long> getVoterIds() {
        return voterIds;
    }
    public void setQuote1Votes(int quote1Votes) {
        this.quote1Votes = quote1Votes;
    }

    public void setQuote2Votes(int quote2Votes) {
        this.quote2Votes = quote2Votes;
    }

    public QuoteDuel(long quote1Id, long quote2Id, int quote1Votes, int quote2Votes) {
        this.quote1Id = quote1Id;
        this.quote2Id = quote2Id;
        this.quote1Votes = quote1Votes;
        this.quote2Votes = quote2Votes;
        this.voterIds = new ArrayList<>();
    }
}
