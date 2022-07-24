package attmayMBBot.functionalities.quoteManagement;

import java.util.ArrayList;
import java.util.List;

public class QuoteAuthor {
    //Stuff that will be added to the JSON File
    private Long discordId;
    private String name;
    private List<String> aliases;
    private List<Quote> quotes;

    public QuoteAuthor(String name, Long discordId) {
        this.discordId = discordId;
        this.name = name;
        this.aliases = new ArrayList<>();
        this.quotes = new ArrayList<>();
    }

    //Getters
    public Long getDiscordId() {
        return discordId;
    }

    public String getName() {
        return name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }
}
