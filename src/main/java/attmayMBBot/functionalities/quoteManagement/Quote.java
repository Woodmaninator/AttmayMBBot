package attmayMBBot.functionalities.quoteManagement;

import java.util.Date;

public class Quote {
    Long id;
    int quoteYear;
    String quoteText;
    Long quoteIssuerId;
    Date quoteIssuedOn;

    public Quote(Long id, int quoteYear, String quoteText, Long quoteIssuerId, Date quoteIssuedOn) {
        this.id = id;
        this.quoteYear = quoteYear;
        this.quoteText = quoteText;
        this.quoteIssuerId = quoteIssuerId;
        this.quoteIssuedOn = quoteIssuedOn;
    }

    public Long getId() {
        return id;
    }

    public int getQuoteYear() {
        return quoteYear;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public Long getQuoteIssuerId() {
        return quoteIssuerId;
    }

    public Date getQuoteIssuedOn() {
        return quoteIssuedOn;
    }

    public String toFormattedString(String authorName) {
        return quoteText + " - " + authorName + ", " + quoteYear;
    }
}
