package attmayMBBot.functionalities.popupMessageHandling;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;

public class PopupMessageHandler {
    public static void sendTemporaryMessageAndDeleteInvoker(MessageChannel channel, String tempMessageText, int duration){
        //send the temporary message
        Message newMessage = channel.createMessage(tempMessageText).block();
        //delete invoker message (this was done in an old version. the new version no longer does that)
        //invokerMessage.delete().block();
        //start a new thread that sleeps for the duration and then deletes the temporary message
        new Thread(() -> {
            try {
                Thread.sleep(duration);
                newMessage.delete().block();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
