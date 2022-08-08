package attmayMBBot.functionalities.arcade;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;

import java.util.Date;

public abstract class AArcadeGameInstance {
    private boolean readyToDelete;
    private Message instanceMessage; //This is the message of the instance that will get edited
    private MessageChannel channel; //This is the channel that the message was put in
    private Long startTime;
    private Long duration;
    private ArcadeManager arcadeManager;

    public Long getInstanceMessageId(){
        if(this.instanceMessage != null)
            return this.instanceMessage.getId().asLong();
        else
            return 0L; //Means that the message instance does not exist
    }
    protected Message getInstanceMessage(){
        return this.instanceMessage;
    }
    protected void setInstanceMessage(Message instanceMessage){
        this.instanceMessage = instanceMessage;
    }
    protected MessageChannel getChannel(){return this.channel;}
    public boolean getReadyToDelete(){
        return this.readyToDelete;
    }
    protected void setReadyToDelete(boolean readyToDelete){
        this.readyToDelete = readyToDelete;
    }
    protected ArcadeManager getArcadeManager(){
        return this.arcadeManager;
    }
    public AArcadeGameInstance(ArcadeManager arcadeManager, Message message, Long duration) {
        this.readyToDelete = false;
        this.arcadeManager = arcadeManager;
        this.channel = message.getChannel().block();
        this.startTime = new Date().getTime();
        this.duration = duration;
    }
    public void checkForTimeout(){
        if(new Date().getTime() > this.startTime + this.duration){
            this.sendTimeOutMessage();
            this.readyToDelete = true;
        }
    }
    public void submitReaction(User user, ReactionEmoji emoji){
        if(!this.readyToDelete){
            this.handleReaction(user, emoji);
        }
    }
    protected abstract void sendTimeOutMessage();
    protected abstract void handleReaction(User user, ReactionEmoji emoji);
}
