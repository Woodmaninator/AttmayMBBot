package attmayMBBot.functionalities.quoteManagement;

import com.google.gson.Gson;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuoteManager {
    //Stuff that will be added to the JSON File
    private List<QuoteAuthor> quoteAuthors;

    public List<QuoteAuthor> getQuoteAuthors() {
        return quoteAuthors;
    }

    public boolean checkIfQuoteAuthorNameExists(String name){
        for(QuoteAuthor quoteAuthor : quoteAuthors) {
            //Check if the name exists as an actual name
            if (quoteAuthor.getName().equalsIgnoreCase(name)) {
                return true;
            }
            //Check if the name exists as an alias
            for (String alias : quoteAuthor.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        //Default case: the name does not exist
        return false;
    }

    public QuoteAuthor getAuthorFromName(String authorName){
        //Only call this method if the author name exists
        QuoteAuthor author = null;
        for(QuoteAuthor quoteAuthor : quoteAuthors) {
            //Check if the name exists as an actual name
            if (quoteAuthor.getName().equalsIgnoreCase(authorName)) {
                author = quoteAuthor;
                break;
            }
            //Check if the name exists as an alias
            for (String alias : quoteAuthor.getAliases()) {
                if (alias.equalsIgnoreCase(authorName)) {
                    author = quoteAuthor;
                    break;
                }
            }
            //if author has a value here, exit the for loop
            if(author != null) {
                break;
            }
        }
        return author;
    }
    public void addQuote(String authorName, Quote quote){
        //Only call this method if the author name exists
        QuoteAuthor author = getAuthorFromName(authorName);

        if(author != null){
            //add the quote to the proper author
            author.getQuotes().add(quote);
        }
    }

    public List<Pair<String, Quote>> getAllQuotesSortedByIssuedDate(){
        List<Pair<String, Quote>> quotes = new ArrayList<>();
        //Accumulate all the quotes from all the authors
        for(QuoteAuthor quoteAuthor : quoteAuthors) {
            for(Quote quote: quoteAuthor.getQuotes()) {
                quotes.add(new Pair<>(quoteAuthor.getName(), quote));
            }
        }
        //Sort the quotes by issued date
        List<Pair<String, Quote>> sortedQuotes = quotes.stream().sorted((x,y) -> x.getValue().getQuoteIssuedOn().compareTo(y.getValue().quoteIssuedOn)).collect(Collectors.toList());
        return sortedQuotes;
    }

    public List<Pair<String, Quote>> getAllQuotesFromAuthorSortedByIssuedDate(String authorName){
        //Only call this method if the author name exists
        List<Pair<String, Quote>> quotes = new ArrayList<>();
        //Get the Author
        QuoteAuthor author = getAuthorFromName(authorName);

        if(author != null) {
            //Accumulate all the quotes from the author
            for(Quote quote: author.getQuotes()){
                quotes.add(new Pair<>(author.getName(), quote));
            }
        }
        //Sort the quotes by issued date
        List<Pair<String, Quote>> sortedQuotes = quotes.stream().sorted((x,y) -> x.getValue().getQuoteIssuedOn().compareTo(y.getValue().quoteIssuedOn)).collect(Collectors.toList());
        return sortedQuotes;
    }

    public void saveQuotesToFile(){
        String path = "AMBBQuotes.json";
        try {
            String jsonString = new Gson().toJson(this);
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(jsonString);
            writer.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
