package attmayMBBot.functionalities.arcade;

import attmayMBBot.functionalities.arcade.aline.AlineArcadeGameInstance;
import attmayMBBot.functionalities.arcade.aline.EAlineDifficulty;
import attmayMBBot.functionalities.quoteQuiz.QuoteQuizInstance;
import discord4j.core.object.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class ArcadeGameManager {
    private ArcadeManager arcadeManager;
    private List<AArcadeGameInstance> arcadeGameInstanceList;
    public ArcadeGameManager(ArcadeManager arcadeManager) {
        this.arcadeManager = arcadeManager;
        this.arcadeGameInstanceList = new ArrayList<>();
    }
    public void addAlineArcadeGameInstance(Message message, Long contestantId, EAlineDifficulty difficulty) {
        this.arcadeGameInstanceList.add(new AlineArcadeGameInstance(this.arcadeManager, message, contestantId, difficulty));
    }
    public AArcadeGameInstance getAlineArcadeGameInstanceByMessageId(Long id){
        for(AArcadeGameInstance instance : this.arcadeGameInstanceList)
            if(instance.getInstanceMessageId().equals(id))
                return instance;
        return null;
    }
    private void updateInstances(){
        for(AArcadeGameInstance instance: this.arcadeGameInstanceList)
            instance.checkForTimeout();
        for(int i = this.arcadeGameInstanceList.size() - 1; i >= 0; i--)
            if(this.arcadeGameInstanceList.get(i).getReadyToDelete())
                this.arcadeGameInstanceList.remove(i);
        //System.out.println("Current number of running instances: " + this.arcadeGameInstanceList.size());
    }
    public void startUpdateLoop(){
        new Thread( () -> {
            while(true){
                try {
                    //10 second intervals
                    Thread.sleep(10000);
                    this.updateInstances();
                } catch (InterruptedException e) {
                    //eh. not that important
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
