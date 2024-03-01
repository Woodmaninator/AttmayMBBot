package attmayMBBot.functionalities.xkcdUpdater;

import discord4j.core.object.entity.channel.MessageChannel;

public class XKCDPoster {
    public void postXKCD(XKCDInfo info, MessageChannel channel) {
        channel.createMessage(spec -> {
            spec.addEmbed(embed -> {
                embed.setTitle("XKCD " + info.getNumber() + " - " + info.getTitle());
                embed.setImage(info.getImageUrl());
                //embed.setDescription(info.getAltText());
                embed.addField("Hover text", info.getAltText(), false);
                embed.addField("Link", "https://xkcd.com/" + info.getNumber(), false);
            });
        }).block();
    }
}
