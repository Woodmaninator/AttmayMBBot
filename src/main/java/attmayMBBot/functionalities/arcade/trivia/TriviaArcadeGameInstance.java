package attmayMBBot.functionalities.arcade.trivia;

import attmayMBBot.APIHandling.triviaAPIHandling.TriviaAPIHandler;
import attmayMBBot.functionalities.arcade.AArcadeGameInstance;
import attmayMBBot.functionalities.arcade.ArcadeManager;
import attmayMBBot.functionalities.arcade.ArcadeUser;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.util.Random;

public class TriviaArcadeGameInstance extends AArcadeGameInstance {
    private static Random random = new Random();
    private long contestantId;
    private String authorMention;
    private TriviaQuestionInformation questionInformation;
    private int guess;
    private int correctAnswer;
    private ETriviaDifficulty difficulty;
    private boolean timeRanOut;
    private String possibleAnswersString;

    public TriviaArcadeGameInstance(ArcadeManager arcadeManager, MessageChannel channel, User user, ETriviaDifficulty difficulty) {
        super(arcadeManager, channel, 20000L); //Duration of 20 seconds
        this.authorMention = user.getMention();
        this.guess = 0; //This means that no guess has been made yet
        this.contestantId = user.getId().asLong();
        this.difficulty = difficulty;
        this.timeRanOut = false;
        initTriviaQuestion();
    }
    private void initTriviaQuestion(){
        this.correctAnswer = random.nextInt(4) + 1; //1-4

        TriviaQuestionInformation questionInformation = new TriviaAPIHandler().getTriviaQuestion(this.difficulty);
        if(questionInformation != null){
            this.questionInformation = questionInformation;
            String possibleAnswersString = "";
            boolean correctAnswerInAlready = false;
            for(int i = 0; i < 4; i++){
                if(i + 1 == this.correctAnswer){
                    possibleAnswersString += this.getNumberEmoji(i + 1) + " " + questionInformation.getCorrectAnswer() + "\n";
                    correctAnswerInAlready = true;
                }
                else{
                    if(correctAnswerInAlready){
                        possibleAnswersString += this.getNumberEmoji(i + 1) + " " + questionInformation.getWrongAnswers().get(i - 1) + "\n";
                    }
                    else{
                        possibleAnswersString += this.getNumberEmoji(i + 1) + " " + questionInformation.getWrongAnswers().get(i) + "\n";
                    }
                }
            }
            this.possibleAnswersString = possibleAnswersString;

            //Build the embed!
            EmbedCreateSpec embed = EmbedCreateSpec.builder()
                    .color(Color.of(0x006666))
                    .title("Trivia Time!")
                    .url("https://www.twitch.tv/woodmaninator")
                    .description("This Trivia Question was created for " + this.authorMention + ".\nYou have 20 seconds to answer it!")
                    .addField("Question", questionInformation.getQuestion(), false)
                    .addField("Here are your options:", possibleAnswersString, false)
                    .build();

            Message triviaMessage = this.getChannel().createMessage(embed).block();
            triviaMessage.addReaction(ReactionEmoji.unicode("\u0031\u20E3")).block();
            triviaMessage.addReaction(ReactionEmoji.unicode("\u0032\u20E3")).block();
            triviaMessage.addReaction(ReactionEmoji.unicode("\u0033\u20E3")).block();
            triviaMessage.addReaction(ReactionEmoji.unicode("\u0034\u20E3")).block();
            this.setInstanceMessage(triviaMessage);
        } else {
            this.setReadyToDelete(true);
        }
    }
    private void updateMessage(){
        this.possibleAnswersString = possibleAnswersString;

        //Build the embed!
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder()
                .color(Color.of(0x006666))
                .title("Trivia Time!")
                .url("https://www.twitch.tv/woodmaninator")
                .description("This Trivia Question was created for " + this.authorMention + ".\nYou have 20 seconds to answer it!")
                .addField("Question", questionInformation.getQuestion(), false)
                .addField("Here are your options:", possibleAnswersString, false);

        if(this.timeRanOut && this.guess == 0){
            builder = builder
                    .addField("Game over!", "The time ran out. The correct answer was " + this.getNumberEmoji(this.correctAnswer) + " " + this.questionInformation.getCorrectAnswer() + ".", false);
        } else {
            if(this.guess != 0){
                if(this.guess == this.correctAnswer){
                    //Win
                    builder = builder
                            .addField("Correct!", "You guessed the correct answer " + this.getNumberEmoji(this.guess) + " " + this.questionInformation.getCorrectAnswer() + ".", false);

                    //Create new arcade user if it doesn't exist
                    if(!this.getArcadeManager().userExists(contestantId))
                        this.getArcadeManager().addNewUser(contestantId);

                    int xpForCompleting = 0;
                    switch(this.difficulty){
                        case EASY:
                            xpForCompleting = 15;
                            break;
                        case MEDIUM:
                            xpForCompleting = 30;
                            break;
                        case HARD:
                            xpForCompleting = 60;
                            break;
                    }

                    //Get the user and add the xp to the user
                    ArcadeUser arcadeUser = this.getArcadeManager().getUser(contestantId);
                    if(arcadeUser != null){
                        arcadeUser.setXp(arcadeUser.getXp() + xpForCompleting);
                        //Save the added xp to the file!
                        this.getArcadeManager().saveArcadeInfoToFile();
                    }

                    //add a field about the gained xp
                    builder = builder
                            .addField("Leveling", "You get **" + xpForCompleting + " XP** for completing the game!\n"
                                    + this.getArcadeManager().getLevelInfoStringFromUser(contestantId), false);
                } else {
                    builder = builder
                            .addField("Wrong!", "You guessed the wrong answer! The correct answer would have been " + this.getNumberEmoji(this.correctAnswer) + " " + this.questionInformation.getCorrectAnswer() + ".", false);
                }
                this.getInstanceMessage().removeAllReactions().block();
            }
        }

        EmbedCreateSpec embed = builder.build();

        this.getInstanceMessage().edit().withMessage(this.getInstanceMessage()).withEmbeds(embed).block();

        if(this.guess != 0)
            this.setReadyToDelete(true);
    }
    private String getNumberEmoji(int number){
        switch(number){
            case 1: return ":one:";
            case 2: return ":two:";
            case 3: return ":three:";
            case 4: return ":four:";
        }
        return "";
    }
    @Override
    protected void handleReaction(User user, ReactionEmoji emoji) {
        if(user.getId().asLong() == this.contestantId){
            switch(emoji.asEmojiData().name().get()){
                case "\u0031\u20E3":
                    this.guess = 1;
                    break;
                case "\u0032\u20E3":
                    this.guess = 2;
                    break;
                case "\u0033\u20E3":
                    this.guess = 3;
                    break;
                case "\u0034\u20E3":
                    this.guess = 4;
                    break;
            }
            if(this.guess != 0){
                updateMessage();
            }
        }
    }
    @Override
    protected void sendTimeOutMessage() {
        this.timeRanOut = true;
        updateMessage();
        this.getInstanceMessage().removeAllReactions().block();
    }
}
