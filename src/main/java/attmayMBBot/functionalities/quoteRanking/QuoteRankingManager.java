package attmayMBBot.functionalities.quoteRanking;

import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizInstance;
import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class QuoteRankingManager {
    private QuoteRankingResults quoteRankingResults;
    private QuoteManager quoteManager;
    private List<QuoteRankingInstance> quoteRankingInstances;

    public QuoteRankingManager(QuoteManager quoteManager, QuoteRankingResults quoteRankingResults) {
        quoteRankingInstances = new ArrayList<>();
        this.quoteManager = quoteManager;
        this.quoteRankingResults = quoteRankingResults;
    }

    public void addQuoteRankingInstance(Message message, String authorMention, Long contestantId){
        QuoteRankingInstance newInstance = new QuoteRankingInstance(this.quoteManager, this.quoteRankingResults, message, authorMention, contestantId);
        this.quoteRankingInstances.add(newInstance);
    }
    public QuoteRankingInstance getQuoteRankingInstanceByMessageId(Long messageId){
        for(QuoteRankingInstance instance : this.quoteRankingInstances)
            if(instance.getMessageId().equals(messageId))
                return instance;
        return null;
    }
    private void updateInstances(){
        for(QuoteRankingInstance instance: this.quoteRankingInstances)
            instance.checkForTimeout();
        for(int i = this.quoteRankingInstances.size() - 1; i >= 0; i--)
            if(this.quoteRankingInstances.get(i).getReadyToDelete())
                this.quoteRankingInstances.remove(i);
    }

    public void startUpdateLoop(){
        new Thread( () -> {
            while(true){
                try {
                    //10 second intervals
                    Thread.sleep(10000);
                    this.updateInstances();
                } catch (InterruptedException | ConcurrentModificationException e) {
                    //eh. not that important
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
