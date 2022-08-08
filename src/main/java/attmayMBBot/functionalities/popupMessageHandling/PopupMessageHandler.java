package attmayMBBot.functionalities.popupMessageHandling;

import discord4j.core.object.entity.Message;

public class PopupMessageHandler {
    public static void sendTemporaryMessageAndDeleteInvoker(Message invokerMessage, String tempMessageText, int duration){
        //send the temporary message
        Message newMessage = invokerMessage.getChannel().block().createMessage(tempMessageText).block();
        //delete invoker message
        invokerMessage.delete().block();
        //start a new thread that sleeps for the duration and then deletes the temporary message
        new Thread(() -> {
            try {
                Thread.sleep(duration);
                newMessage.delete().block();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
