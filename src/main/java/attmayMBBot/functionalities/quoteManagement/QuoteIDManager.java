package attmayMBBot.functionalities.quoteManagement;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuoteIDManager {
    private Long currentQuoteId;

    public QuoteIDManager(List<QuoteAuthor> quoteAuthors){
        List<Quote> currentQuotes = new ArrayList<Quote>();
        for(QuoteAuthor quoteAuthor : quoteAuthors){
            currentQuotes.addAll(quoteAuthor.getQuotes());
        }
        //The current quote id will be the maximum of the current quotes + 1, so the currentQuoteId is the next free id
        this.currentQuoteId = currentQuotes.stream().max(Comparator.comparing(Quote::getId)).get().getId() + 1;
    }

    public Long getNextQuoteId() {
        return this.currentQuoteId++;
    }
}
