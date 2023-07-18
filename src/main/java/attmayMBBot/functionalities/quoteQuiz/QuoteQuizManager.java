package attmayMBBot.functionalities.quoteQuiz;

import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class QuoteQuizManager {
    private QuoteManager quoteManager;
    private List<QuoteQuizInstance> quoteQuizInstances;

    public QuoteQuizManager(QuoteManager quoteManager) {
        quoteQuizInstances = new ArrayList<>();
        this.quoteManager = quoteManager;
    }

    public void addQuoteQuizInstance(MessageChannel channel, User user){
        QuoteQuizInstance newInstance = new QuoteQuizInstance(this.quoteManager, channel, user);
        this.quoteQuizInstances.add(newInstance);
    }

    public QuoteQuizInstance getQuoteQuizInstanceByMessageId(Long messageId){
        for(QuoteQuizInstance instance : this.quoteQuizInstances)
            if(instance.getMessageId().equals(messageId))
                return instance;
        return null;
    }

    private void updateInstances(){
        for(QuoteQuizInstance instance: this.quoteQuizInstances)
            instance.checkForTimeout();
        for(int i = this.quoteQuizInstances.size() - 1; i >= 0; i--)
            if(this.quoteQuizInstances.get(i).getReadyToDelete())
                this.quoteQuizInstances.remove(i);
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
