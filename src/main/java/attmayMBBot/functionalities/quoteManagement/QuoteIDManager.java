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
        if(currentQuotes.size() > 0) {
            //The current quote id will be the maximum of the current quotes + 1, so the currentQuoteId is the next free id
            this.currentQuoteId = currentQuotes.stream().max(Comparator.comparing(Quote::getId)).get().getId() + 1;
        } else {
            //Default case if there are no quotes in the system yet: ID will be 1
            this.currentQuoteId = 1L;
        }
    }

    public Long getLastQuoteId() {
        return this.currentQuoteId-1;
    }

    public Long getNextQuoteId() {
        return this.currentQuoteId++;
    }
}
