package attmayMBBot.functionalities.quoteRanking;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class QuoteRankingResults {
    private List<QuoteDuel> duelList;

    public List<QuoteDuel> getDuelList() {
        return duelList;
    }
    public void addDuelVote(long quote1Id, long quote2Id, int quote1Votes, int quote2Votes, long voterId){
        boolean reversed = false;
        //Let's see if that combination already exists
        QuoteDuel duel = duelList.stream().filter(quoteDuel -> quoteDuel.getQuote1Id() == quote1Id && quoteDuel.getQuote2Id() == quote2Id).findFirst().orElse(null);
        if(duel == null){
            //Check if the other way around exists
            duel = duelList.stream().filter(quoteDuel -> quoteDuel.getQuote1Id() == quote2Id && quoteDuel.getQuote2Id() == quote1Id).findFirst().orElse(null);
            if(duel != null){
                reversed = true;
            }
        }

        if(duel == null){
            //At this point, if duel is still null, we have to create a new duel
            //Create new duel
            duel = new QuoteDuel(quote1Id, quote2Id, quote1Votes, quote2Votes);
            duel.getVoterIds().add(voterId); //Add the voter ID
            duelList.add(duel);
            //Save that shit
            saveConfigToFile();
        } else {
            //The duel has to be edited here

            //Swap the votes if the duel was reversed
            int h;
            if(reversed){
                h = quote1Votes;
                quote1Votes = quote2Votes;
                quote2Votes = h;
            }

            //Set the new votes, if the voterId is not in the list of voter ids
            if(!duel.getVoterIds().contains(voterId)) {
                duel.setQuote1Votes(duel.getQuote1Votes() + quote1Votes);
                duel.setQuote2Votes(duel.getQuote2Votes() + quote2Votes);
                //Add the voter id to the list
                duel.getVoterIds().add(voterId);
            }
            //Save that shit
            saveConfigToFile();
        }
    }

    public void saveConfigToFile(){
        String path = "AMBBQuoteRanking.json";
        try {
            String jsonString = new Gson().toJson(this);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(jsonString);
            writer.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
