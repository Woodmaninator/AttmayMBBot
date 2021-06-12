package attmayMBBot.authorization;

import attmayMBBot.config.AttmayMBBotConfig;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;

import java.util.List;

public class AdvancedBotUserAuthorization {
    private AttmayMBBotConfig config;

    public AdvancedBotUserAuthorization(AttmayMBBotConfig config) {
        this.config = config;
    }

    public boolean checkIfUserIsAuthorized(User user){
        long authorizedRoleID = this.config.getAuthorizedRoleID();

        Member member = user.asMember(Snowflake.of(this.config.getGuildID())).block();
        if(member != null)
        {
            List<Role> memberRoles = member.getRoles().collectList().block();
            if(memberRoles != null) {
                return memberRoles.stream().anyMatch(x -> x.getId().asLong() == authorizedRoleID);
            }
        }
        return false;
    }
}
