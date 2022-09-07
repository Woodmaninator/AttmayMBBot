package attmayMBBot.functionalities.quoteRanking;

import attmayMBBot.functionalities.quoteManagement.Quote;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class QuoteRankingInstance {
    private QuoteManager quoteManager;
    private QuoteRankingResults quoteRankingResults;
    private Message rankingMessage;
    private String authorMention;
    private Long lastInteractionTime;
    private Long contestantId;
    private Long timeOutDuration; //in milliseconds
    private boolean readyToDelete;
    private Long currentQuote1Id;
    private Long currentQuote2Id;

    public Long getMessageId(){
        return this.rankingMessage.getId().asLong();
    }

    public boolean getReadyToDelete(){
        return this.readyToDelete;
    }

    public QuoteRankingInstance(QuoteManager quoteManager, QuoteRankingResults quoteRankingResults, Message message, String authorMention, Long contestantId) {
        this.quoteManager = quoteManager;
        this.quoteRankingResults = quoteRankingResults;
        this.authorMention = authorMention;
        this.contestantId = contestantId;
        this.lastInteractionTime = new Date().getTime();
        this.timeOutDuration = 5L * 60L * 1000L; //5 minutes
        this.readyToDelete = false;

        initRankingMessage(message);
    }
    private void initRankingMessage(Message message){
        EmbedCreateSpec embed = createQuoteRankingEmbed();

        Message rankingMessage = message.getChannel().block().createMessage(embed).block();
        rankingMessage.addReaction(ReactionEmoji.unicode("\u0031\u20E3")).block();
        rankingMessage.addReaction(ReactionEmoji.unicode("\u0032\u20E3")).block();
        this.rankingMessage = rankingMessage;
    }

    public void submitAnswer(User user, ReactionEmoji emoji){
        if(!this.readyToDelete) {
            if (user.getId().asLong() == this.contestantId) {
                int quote1Votes = 0;
                int quote2Votes = 0;
                switch (emoji.asEmojiData().name().get()) {
                    case "\u0031\u20E3":
                        quote1Votes = 1;
                        break;
                    case "\u0032\u20E3":
                        quote2Votes = 1;
                        break;
                }
                if (quote1Votes != 0 || quote2Votes != 0) { //prevents faulty validation when wrong reaction is added to the message
                     this.quoteRankingResults.addDuelVote(this.currentQuote1Id, this.currentQuote2Id, quote1Votes, quote2Votes, contestantId);
                    //Remove the reaction of the user
                    this.rankingMessage.removeReaction(emoji, user.getId()).block();
                    //Update the message
                    updateRankingMessage();
                }
            }
        }
    }
    private void updateRankingMessage(){
        //Update the last interaction time so that the message does not run out of time
        this.lastInteractionTime = new Date().getTime();

        //Get new quotes
        EmbedCreateSpec embed = createQuoteRankingEmbed();

        this.rankingMessage.edit().withMessage(this.rankingMessage).withEmbeds(embed).block();
    }
    private EmbedCreateSpec createQuoteRankingEmbed(){
        List<Pair<String, Quote>> quoteList = this.quoteManager.getAllQuotesSortedByIssuedDate();

        Random random = new Random();

        Pair<String, Quote> quote1 = quoteList.get(random.nextInt(quoteList.size()));
        Pair<String, Quote> quote2 = quoteList.get(random.nextInt(quoteList.size()));

        //While loop makes sure that the two quotes are not the same
        while(quote1.getValue().getId().equals(quote2.getValue().getId())){
            quote2 = quoteList.get(random.nextInt(quoteList.size()));
        }

        this.currentQuote1Id = quote1.getValue().getId();
        this.currentQuote2Id = quote2.getValue().getId();

        String quote1String = ":one: " + quote1.getValue().getQuoteText() + " - " + quote1.getKey() + ", " + quote1.getValue().getQuoteYear();
        String quote2String = ":two: " + quote2.getValue().getQuoteText() + " - " + quote2.getKey() + ", " + quote2.getValue().getQuoteYear();

        //Build the embed!
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.of(0x006666))
                .title("Quote Ranking")
                .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                .description("This Quote Ranking was created for " + this.authorMention + ".\n" +
                        "You can create your own using !rankQuotes.\n" +
                        "This message will no longer be usable after 5 minutes of inactivity.")
                //.addField("\u200B", "\u200B", false)
                .addField("Which one of these two quotes is superior?", quote1String + "\n\n" + quote2String + "\n\n\n\n", false)
                .build();

        return embed;
    }
    public void checkForTimeout(){
        if(new Date().getTime() > this.lastInteractionTime + this.timeOutDuration){
            //Edit message to display a timeout message
            EmbedCreateSpec embed = EmbedCreateSpec.builder()
                    .color(Color.of(0x006666))
                    .title("Quote Ranking")
                    .url("https://www.youtube.com/watch?v=dQw4w9WgXcQ")
                    .description("This Quote Ranking was created for " + this.authorMention + ".\n" +
                            "This message can no longer be used due to inactivity.\n" +
                            "Create a new one using !rankQuotes")
                    .build();
            this.rankingMessage.edit().withMessage(this.rankingMessage).withEmbeds(embed).block();
            //Remove all reactions
            this.rankingMessage.removeAllReactions().block();
            //Set ready to delete, so it gets removed in the next cycle
            this.readyToDelete = true;
        }
    }
}
