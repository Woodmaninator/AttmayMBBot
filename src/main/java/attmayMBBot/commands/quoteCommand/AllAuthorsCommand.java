package attmayMBBot.commands.quoteCommand;

import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.functionalities.quoteManagement.QuoteAuthor;
import attmayMBBot.functionalities.quoteManagement.QuoteManager;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AllAuthorsCommand implements ICommand {
    private AttmayMBBotConfig config;
    private QuoteManager quoteManager;

    public AllAuthorsCommand(AttmayMBBotConfig config, QuoteManager quoteManager){
        this.config = config;
        this.quoteManager = quoteManager;
    }

    @Override
    public void execute(Message message, String[] args) {
        List<QuoteAuthor> authorList;
        List<String> embedDescriptions = new ArrayList<>();
        String embedTitle = "List of all Quote Authors";
        StringBuilder sb = new StringBuilder();
        final int MAX_EMBED_CAPACITY = 4096;

        // check if there are any quotes authors
        if (quoteManager.getQuoteAuthors().size() == 0) {
            message.getChannel().block().createMessage("There are no quote authors to list!").block();
            return;
        }

        // sort quote authors in number of quotes
        Comparator<QuoteAuthor> authorComparator = (QuoteAuthor a, QuoteAuthor b) -> ((Integer)b.getQuotes().size()).compareTo(a.getQuotes().size());
        authorList = quoteManager.getQuoteAuthors().stream().sorted(authorComparator).collect(Collectors.toList());

        int lastGoodIndex = 0;
        for (int i = 0; i < authorList.size(); ++i) {
            // get current author
            QuoteAuthor author = authorList.get(i);

            // update last good index
            lastGoodIndex = sb.length();

            // start with author place
            sb.append(i);
            sb.append(". ");
            sb.append(author.getName());

            // begin alias list
            if (author.getAliases().size() > 0) {
                sb.append(" (A.K.A. ");
                // count how many commas there should be
                int commas = author.getAliases().size();
                for (String alias : author.getAliases()) {
                    // append the alias
                    sb.append(alias);
                    // add a comma if needed
                    if (--commas > 0) {
                        sb.append(", ");
                    }
                }
                // close parenthesis
                sb.append(")");
            }

            // append quote count
            sb.append(" has ");
            sb.append(author.getQuotes().size());
            if (author.getQuotes().size() != 1) {
                sb.append(" quotes.");
            } else {
                sb.append(" quote.");
            }

            // append new line
            if (i != authorList.size()-1) {
                sb.append("\n");
            }

            // check if exceeded capacity
            if (sb.length() > MAX_EMBED_CAPACITY) {
                // add everything up to this author to an embed description
                embedDescriptions.add(sb.substring(0, lastGoodIndex));
                // remove previous author entries
                sb.delete(0, lastGoodIndex);
            }
        }

        // add last embed description
        if (sb.length() > 0) {
            embedDescriptions.add(sb.toString());
        }

        // yeet the results out
        for (String embedDescription : embedDescriptions) {
            message.getChannel()
                    .block()
                    .createMessage(y -> {
                       y.setEmbed(x -> {
                           x.setTitle(embedTitle).setDescription(embedDescription).setColor(Color.of(0, 102, 102));
                       });
                    }).block();
        }
    }
}
