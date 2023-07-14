package attmayMBBot.commands.dinnerpostCommand;

import attmayMBBot.APIHandling.spoonacularAPIHandling.SpoonacularAPIHandler;
import attmayMBBot.authorization.AdvancedBotUserAuthorization;
import attmayMBBot.commands.ICommand;
import attmayMBBot.config.AttmayMBBotConfig;
import attmayMBBot.util.Recipe;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;

public class DinnerpostCommand implements ICommand {
    private AttmayMBBotConfig config;

    public DinnerpostCommand(AttmayMBBotConfig config) {
        this.config = config;
    }

    @Override
    public void execute(String[] args, User sender, MessageChannel channel) {
        if(new AdvancedBotUserAuthorization(this.config).checkIfUserIsAuthorized(sender)){
            Recipe recipe = null;
            if(args.length <= 1)
                recipe = new SpoonacularAPIHandler(this.config).getRandomRecipe();
            else
                recipe = new SpoonacularAPIHandler(this.config).getRandomRecipe(args[1]);
            if(recipe != null) {
                String embedDescription = "I made " + recipe.getTitle() + " today and it turned out pretty well.\nIf you want to try this recipe for yourself you can go check it out here:\n" + recipe.getRecipeUrl();
                String embedTitle = recipe.getTitle();
                String embedImageUrl = recipe.getImageUrl();
                //EmbedCreateSpec embedCreateSpec = new EmbedCreateSpec().setTitle(embedTitle).setDescription(embedDescription).setImage(embedImageUrl);
                //MessageCreateSpec messageCreateSpec = new MessageCreateSpec().setEmbed(x -> x.setTitle(embedTitle).setDescription(embedDescription).setImage(embedImageUrl));
                //No idea what Im doing, but hey, maybe this will work.
                channel.createMessage(y -> y.setEmbed(x -> x.setTitle(embedTitle).setDescription(embedDescription).setImage(embedImageUrl).setColor(Color.of(0, 102, 102)))).block();
            } else {
                channel.createMessage("Either there is nothing that fits the desired description or something went terribly wrong. Do not judge me, I am not a wizard (yet)").block();
            }
        } else
            channel.createMessage("Well, I know this is somewhat awkward but you are not allowed to perform this command.").block();
    }
}