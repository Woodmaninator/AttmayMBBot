package attmayMBBot.functionalities.arcade.aline;

import attmayMBBot.functionalities.arcade.AArcadeGameInstance;
import attmayMBBot.functionalities.arcade.ArcadeManager;
import attmayMBBot.functionalities.arcade.ArcadeUser;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.util.concurrent.Semaphore;

public class AlineArcadeGameInstance extends AArcadeGameInstance {
    private Long contestantId;
    private String authorMention;
    private Semaphore gameSemaphore;
    private AlineGame game;
    private boolean timeRanOut;

    public AlineArcadeGameInstance(ArcadeManager arcadeManager, Message message, Long contestantId, EAlineDifficulty difficulty) {
        super(arcadeManager, message, 5L * 60L * 1000L); //5 minutes duration
        this.authorMention = message.getAuthor().get().getMention();
        this.contestantId = contestantId;
        this.game = new AlineGame(difficulty);
        this.gameSemaphore = new Semaphore(1);
        this.timeRanOut = false;
        this.createGameMessage();
    }
    private void createGameMessage(){
        //Build the embed!
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.of(0x006666))
                .title("aLine")
                .url("https://www.twitch.tv/woodmaninator")
                .description("This game of aLine was created for " + this.authorMention + ".\n\n" +
                        "Move the red line to the blue target!\n" +
                        "Press the X reaction to give up!\n" + game.getBoardString())
                //.addField("\u200B", "\u200B", false)
                //.addField("Move the red line to the orange target!", game.getBoardString(), false)
                //.addField("\u200B", "\u200B", false)
                .build();

        Message newMessage = this.getChannel().createMessage(embed).block();
        newMessage.addReaction(ReactionEmoji.unicode("\u2B06\uFE0F")).block(); //UP-Arrow
        newMessage.addReaction(ReactionEmoji.unicode("\u2B07\uFE0F")).block(); //DOWN-Arrow
        newMessage.addReaction(ReactionEmoji.unicode("\u2B05\uFE0F")).block(); //LEFT-Arrow
        newMessage.addReaction(ReactionEmoji.unicode("\u27A1\uFE0F")).block(); //RIGHT-Arrow
        newMessage.addReaction(ReactionEmoji.unicode("\u274C")).block(); //X-Button
        this.setInstanceMessage(newMessage);
    }
    @Override
    protected void sendTimeOutMessage() {
        this.timeRanOut = true;
        updateGameMessage();
        //Remove all reactions after the time ran out
        this.getInstanceMessage().removeAllReactions().block();
    }

    @Override
    protected void handleReaction(User user, ReactionEmoji emoji) {
        //Check if the user from the reaction is the same as the contestant ID
        try {
            if(user.getId().asLong() == this.contestantId) {
                //acqurie semaphore so only one reaction can be handled at a time
                this.gameSemaphore.acquire();

                //Check for movement reactions
                boolean isMovementReaction = false;
                switch (emoji.asEmojiData().name().get()) {
                    case "\u2B06\uFE0F":
                        this.game.moveInDirection(EAlineMovementDirection.UP);
                        isMovementReaction = true;
                        break;
                    case "\u2B07\uFE0F":
                        this.game.moveInDirection(EAlineMovementDirection.DOWN);
                        isMovementReaction = true;
                        break;
                    case "\u27A1\uFE0F":
                        this.game.moveInDirection(EAlineMovementDirection.RIGHT);
                        isMovementReaction = true;
                        break;
                    case "\u2B05\uFE0F":
                        this.game.moveInDirection(EAlineMovementDirection.LEFT);
                        isMovementReaction = true;
                        break;
                    case "\u274C":
                        this.game.giveUp();
                        isMovementReaction = true;
                        break;
                }

                //Update the shit but only if it was a movement reaction
                if (isMovementReaction) {
                    //do shit here
                    //remove the reaction from the user that made it
                    this.getInstanceMessage().removeReaction(emoji, user.getId()).block();
                    //Checking for a win/gameover is done in the move method itself!
                    //Update the message
                    this.updateGameMessage();
                }

                //free semaphore so next request can be handled
                this.gameSemaphore.release();
            }
        } catch (Exception e) { //InterruptedException
            e.printStackTrace();
            this.getChannel().createMessage("Something went wrong with your game. It will no longer be possible to continue!").block();
            this.setReadyToDelete(true);
        }
    }

    private void updateGameMessage(){
        //Build the embed
        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .color(Color.of(0x006666))
                .title("aLine")
                .url("https://www.twitch.tv/woodmaninator")
                .description("This game of aLine was created for " + this.authorMention + ".\n\n" +
                        "Move the red line to the blue target!\n" + game.getBoardString());

        //Update the message according to the state of the game!
        if(this.game.getIsWon()){
            embedBuilder = embedBuilder
                    .addField("You win!", "Congratulations! You have won the game in " + this.game.getAmountOfMoves() + " moves!", false);

            //Create new arcade user if it doesn't exist
            if(!this.getArcadeManager().userExists(contestantId))
                this.getArcadeManager().addNewUser(contestantId);

            //Get the user and add the xp to the user
            ArcadeUser arcadeUser = this.getArcadeManager().getUser(contestantId);
            if(arcadeUser != null){
                arcadeUser.setXp(arcadeUser.getXp() + this.game.getXpForCompleting());
                //Save the added xp to the file!
                this.getArcadeManager().saveArcadeInfoToFile();
            }

            //add a field about the gained xp
            embedBuilder = embedBuilder
                    .addField("Leveling", "You get **" + this.game.getXpForCompleting() + " XP** for completing the game!\n"
                            + this.getArcadeManager().getLevelInfoStringFromUser(contestantId), false);
        } else {
            if(this.game.getIsGameOver()){
                embedBuilder = embedBuilder
                        .addField("Game over!", "You have lost the game! L", false);
            }
        }

        //Update the message if the time ran out. Ready to delete is only set to true here if the game is already over!
        if(this.timeRanOut){
            embedBuilder = embedBuilder
                    .addField("Game over!", "The time has run out! L", false);
        }

        EmbedCreateSpec embed = embedBuilder.build();

        //stupid
        this.getInstanceMessage().edit().withMessage(this.getInstanceMessage()).withEmbeds(embed).block();

        //if the game is lost/won or the time ran out, remove all reactions
        if(this.game.getIsWon() || this.game.getIsGameOver()){
            this.getInstanceMessage().removeAllReactions().block();
            if(this.game.getIsWon()){
                //Add reactions after the game is won
                this.getInstanceMessage().addReaction(ReactionEmoji.unicode("\uD83D\uDC4D")).block();
                this.getInstanceMessage().addReaction(ReactionEmoji.unicode("\uD83D\uDE42")).block();
                this.getInstanceMessage().addReaction(ReactionEmoji.unicode("\uD83C\uDF86")).block();
                this.getInstanceMessage().addReaction(ReactionEmoji.unicode("\uD83C\uDF89")).block();
            } else if(this.game.getIsGameOver()){
                this.getInstanceMessage().addReaction(ReactionEmoji.unicode("\uD83D\uDE22")).block();
                this.getInstanceMessage().addReaction(ReactionEmoji.unicode("\uD83D\uDC4E")).block();
                this.getInstanceMessage().addReaction(ReactionEmoji.unicode("\u26D4")).block();
                this.getInstanceMessage().addReaction(ReactionEmoji.unicode("\uD83D\uDEB3")).block();
            }
            //set ready to delete to true
            this.setReadyToDelete(true);
        }
    }
}
