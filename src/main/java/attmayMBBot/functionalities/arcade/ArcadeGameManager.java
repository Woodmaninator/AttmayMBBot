package attmayMBBot.functionalities.arcade;

import attmayMBBot.functionalities.arcade.aline.AlineArcadeGameInstance;
import attmayMBBot.functionalities.arcade.aline.EAlineDifficulty;
import attmayMBBot.functionalities.arcade.trivia.ETriviaDifficulty;
import attmayMBBot.functionalities.arcade.trivia.TriviaArcadeGameInstance;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizInstance;
import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ArcadeGameManager {
    private ArcadeManager arcadeManager;
    private List<AArcadeGameInstance> arcadeGameInstanceList;
    private Semaphore semaphore;
    public ArcadeGameManager(ArcadeManager arcadeManager) {
        this.arcadeManager = arcadeManager;
        this.arcadeGameInstanceList = new ArrayList<>();
        this.semaphore = new Semaphore(1);
    }
    public void addAlineArcadeGameInstance(Message message, Long contestantId, EAlineDifficulty difficulty) {
        try {
            this.semaphore.acquire(1);
            this.arcadeGameInstanceList.add(new AlineArcadeGameInstance(this.arcadeManager, message, contestantId, difficulty));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.semaphore.release(1);
    }
    public void addTriviaArcadeGameInstance(Message message, Long contestantId, ETriviaDifficulty difficulty) {
        try {
            this.semaphore.acquire(1);
            this.arcadeGameInstanceList.add(new TriviaArcadeGameInstance(this.arcadeManager, message, contestantId, difficulty));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.semaphore.release(1);
    }
    public AArcadeGameInstance getAlineArcadeGameInstanceByMessageId(Long id){
        for(AArcadeGameInstance instance : this.arcadeGameInstanceList)
            if(instance.getInstanceMessageId().equals(id))
                return instance;
        return null;
    }
    private void updateInstances() throws InterruptedException {
        this.semaphore.acquire(1);
        for(AArcadeGameInstance instance: this.arcadeGameInstanceList)
            instance.checkForTimeout();
        for(int i = this.arcadeGameInstanceList.size() - 1; i >= 0; i--)
            if(this.arcadeGameInstanceList.get(i).getReadyToDelete())
                this.arcadeGameInstanceList.remove(i);
        this.semaphore.release(1);
        //System.out.println("Current number of running instances: " + this.arcadeGameInstanceList.size());
    }
    public void startUpdateLoop(){
        new Thread( () -> {
            while(true){
                try {
                    //2 second intervals
                    Thread.sleep(2000);
                    this.updateInstances();
                } catch (InterruptedException | ConcurrentModificationException e) {
                    //eh. not that important
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
