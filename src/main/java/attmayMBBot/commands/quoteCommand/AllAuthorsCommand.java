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

    private Comparator<QuoteAuthor> authorComparator;

    public AllAuthorsCommand(AttmayMBBotConfig config, QuoteManager quoteManager){
        this.config = config;
        this.quoteManager = quoteManager;
        this.authorComparator = (QuoteAuthor a, QuoteAuthor b) -> Integer.compare(b.getQuotes().size(), a.getQuotes().size());
    }

    @Override
    public void execute(Message message, String[] args) {
        List<QuoteAuthor> authorList;
        List<String> embedDescriptions = new ArrayList<>();
        String embedTitle = "List of all Quote Authors";
        StringBuilder sb = new StringBuilder();
        final int MAX_EMBED_CAPACITY = 4096;
        int maxPlaceCount = quoteManager.getQuoteAuthors().size();
        int maxAuthorsCount = quoteManager.getQuoteAuthors().size();

        // check if there are any quotes authors
        if (maxPlaceCount == 0) {
            message.getChannel().block().createMessage("There are no quote authors to list!").block();
            return;
        }

        // sort quote authors in number of quotes
        authorList = quoteManager.getQuoteAuthors().stream().sorted(this.authorComparator).collect(Collectors.toList());

        // check if there is an author count provided
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("top")) {
                // command is !authorlist top [place]
                if (args.length < 3) {
                    message.getChannel()
                            .block()
                            .createMessage("This command feels incomplete.\nUse !authorlist top [place] instead.")
                            .block();
                    return;
                }

                // parse the [place] in !authorlist top [place]
                try {
                    maxPlaceCount = Math.min(Integer.parseInt(args[2]), maxPlaceCount);

                    if (maxPlaceCount <= 0) {
                        // invalid count
                        message.getChannel()
                                .block()
                                .createMessage(":face_with_raised_eyebrow:")
                                .block();
                        return;
                    }
                } catch (Exception e) {
                    // not a number
                    message.getChannel()
                            .block()
                            .createMessage("Not a valid place number.")
                            .block();
                    return;
                }
            } else {
                // command is !authorlist [count]
                try {
                    maxAuthorsCount = Math.min(Integer.parseInt(args[1]), maxAuthorsCount);

                    if (maxAuthorsCount <= 0) {
                        // invalid count
                        message.getChannel()
                                .block()
                                .createMessage(":face_with_raised_eyebrow:")
                                .block();
                        return;
                    }
                } catch (Exception e) {
                    // not a number
                    message.getChannel()
                            .block()
                            .createMessage("Not a valid count.")
                            .block();
                    return;
                }
            }
        }

        int lastGoodLength;
        int lastPlaceNumber = 1;
        int lastPlaceIndex = 0;
        int listedAuthorCount = 0;
        for (int i = 0; i < maxAuthorsCount; ++i) {
            // get current author
            QuoteAuthor currentAuthor = authorList.get(i);
            QuoteAuthor lastPlaceAuthor = authorList.get(lastPlaceIndex);

            // update place number
            if (currentAuthor.getQuotes().size() < lastPlaceAuthor.getQuotes().size()) {
                ++lastPlaceNumber;
                lastPlaceIndex = i;
            }

            // check if the desired count is reached
            if (lastPlaceNumber > maxPlaceCount) {
                --lastPlaceNumber;
                break;
            }

            // update last good index
            lastGoodLength = sb.length();

            // start with author place
            sb.append("#");
            sb.append(lastPlaceNumber);
            sb.append(" ");

            // append author name
            sb.append(currentAuthor.getName());

            // begin alias list
            if (currentAuthor.getAliases().size() > 0) {
                sb.append(" (A.K.A. ");
                // count how many commas there should be
                int commas = currentAuthor.getAliases().size();
                for (String alias : currentAuthor.getAliases()) {
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
            sb.append(currentAuthor.getQuotes().size());
            if (currentAuthor.getQuotes().size() != 1) {
                sb.append(" quotes.");
            } else {
                sb.append(" quote.");
            }

            // append new line
            if (i != maxAuthorsCount-1) {
                sb.append("\n");
            }

            // check if exceeded capacity
            if (sb.length() > MAX_EMBED_CAPACITY) {
                // add everything up to this author to an embed description
                embedDescriptions.add(sb.substring(0, lastGoodLength));
                // remove previous author entries
                sb.delete(0, lastGoodLength);
            }

            // update listed author count
            ++listedAuthorCount;
        }

        // add last embed description
        if (sb.length() > 0) {
            embedDescriptions.add(sb.toString());
        }

        // update the embed title
        if (listedAuthorCount < authorList.size()) {
            embedTitle = String.format("List of the Top %d Quote Authors", lastPlaceNumber);
        }

        // yeet the results out
        String finalEmbedTitle = embedTitle;
        for (String embedDescription : embedDescriptions) {
            message.getChannel()
                    .block()
                    .createMessage(y -> {
                       y.setEmbed(x -> {
                           x.setTitle(finalEmbedTitle).setDescription(embedDescription).setColor(Color.of(0, 102, 102));
                       });
                    }).block();
        }
    }
}
