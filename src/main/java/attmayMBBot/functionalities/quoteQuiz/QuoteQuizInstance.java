package attmayMBBot.functionalities.quoteQuiz;

import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteAuthor;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import attmayMBBot.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class QuoteQuizInstance {
    private static Random random  = new Random();
    private QuoteManager quoteManager;
    private Message quizMessage;
    private String authorMention;
    private Long startTime;
    private Long contestantId;
    private Long guessDuration; //in milliseconds
    private boolean readyToDelete;
    private int guess;
    private int correctAnswer;

    public Long getMessageId(){
        return this.quizMessage.getId().asLong();
    }
    public boolean getReadyToDelete(){
        return this.readyToDelete;
    }

    public QuoteQuizInstance(QuoteManager quoteManager, MessageChannel channel, User user) {
        this.quoteManager = quoteManager;
        this.authorMention = user.getMention();
        this.contestantId = user.getId().asLong();
        this.startTime = new Date().getTime();
        this.guessDuration = 5L * 60L * 1000L; //5 minutes
        this.readyToDelete = false;
        this.guess = 0; //0 means that no guess was made yet

        initQuiz(channel);
    }

    private void initQuiz(MessageChannel channel){
        this.correctAnswer = random.nextInt(4) + 1; //1-4

        List<Pair<String, Quote>> quotePairList = this.quoteManager.getAllQuotesSortedByIssuedDate();
        Pair<String, Quote> randomPair = quotePairList.get(random.nextInt(quotePairList.size()));

        List<QuoteAuthor> quoteAuthorList = this.quoteManager.getQuoteAuthors();

        //Only init the quiz if there are at least 4 different authors in the database
        if(quoteAuthorList.size() < 4){
            channel.createMessage("There are not enough authors in the database to create a quote quiz. There have to be at least 4 authors..").block();
            this.readyToDelete = true;
            return;
        }

        List<String> quoteAuthorNames = new ArrayList<>();
        for(QuoteAuthor author : quoteAuthorList)
            quoteAuthorNames.add(author.getName());

        //Get the correct answer and the quote author
        String correctAuthor = randomPair.getKey();
        Quote quote = randomPair.getValue();

        //Get 3 random wrong answers
        List<String> wrongAuthors = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            String name = quoteAuthorNames.get(random.nextInt(quoteAuthorNames.size()));
            while((name.equals(correctAuthor)) || wrongAuthors.contains(name)){
                name = quoteAuthorNames.get(random.nextInt(quoteAuthorNames.size()));
            }
            wrongAuthors.add(name);
        }

        //This is questionable code. I'm not sure if it's the best way to do this.
        String possibleAnswersString = "";
        boolean correctAnswerInAlready = false;
        for(int i = 0; i < 4; i++){
            if(i + 1 == correctAnswer){
                possibleAnswersString += this.getNumberEmoji(i + 1) + " " + correctAuthor + "\n";
                correctAnswerInAlready = true;
            }
            else{
                if(correctAnswerInAlready){
                    possibleAnswersString += this.getNumberEmoji(i + 1) + " " + wrongAuthors.get(i - 1) + "\n";
                }
                else{
                    possibleAnswersString += this.getNumberEmoji(i + 1) + " " + wrongAuthors.get(i) + "\n";
                }
            }
        }

        //Build the embed!
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.of(0x006666))
                .title("Quote Quiz")
                .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                .description("This Quote Quiz was created for " + this.authorMention + ".")
                //.addField("\u200B", "\u200B", false)
                .addField("Guess the author of this quote", quote.getQuoteText(), false)
                //.addField("\u200B", "\u200B", false)
                .addField("Here are your options:", possibleAnswersString, false)
                .build();

        Message quizMessage = channel.createMessage(embed).block();
        quizMessage.addReaction(ReactionEmoji.unicode("\u0031\u20E3")).block();
        quizMessage.addReaction(ReactionEmoji.unicode("\u0032\u20E3")).block();
        quizMessage.addReaction(ReactionEmoji.unicode("\u0033\u20E3")).block();
        quizMessage.addReaction(ReactionEmoji.unicode("\u0034\u20E3")).block();
        this.quizMessage = quizMessage;
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

    public void submitAnswer(User user, ReactionEmoji emoji){
        if(!this.readyToDelete) {
            if (user.getId().asLong() == this.contestantId) {
                switch (emoji.asEmojiData().name().get()) {
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
                if (this.guess != 0) //prevents faulty validation when wrong reaction is added to the message
                    validateAnswer();
            }
        }
    }
    private void validateAnswer(){
        if(this.guess == this.correctAnswer){
            this.quizMessage.getChannel().block().createMessage(this.authorMention + " Correct!").block();
        } else {
            this.quizMessage.getChannel().block().createMessage(this.authorMention + " Incorrect! The correct answer was " + this.getNumberEmoji(this.correctAnswer)).block();
        }
        this.readyToDelete = true;
    }

    public void checkForTimeout(){
        if(new Date().getTime() > this.startTime + this.guessDuration){
            this.quizMessage.getChannel().block().createMessage(this.authorMention + " You took too long to guess. The correct answer was " + this.getNumberEmoji(this.correctAnswer)).block();
            this.readyToDelete = true;
        }
    }
}
