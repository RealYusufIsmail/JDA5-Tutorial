package io.github.realyusufismail.slash;

import io.github.realyusufismail.handler.Slash;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class UserInfoCommand implements Slash {
    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        Member member = event.getOption("member", OptionMapping::getAsMember);
        Member selfMember = event.getMember();

        if (member != null) {
            sendMemberInfo(event, member);
        } else if (selfMember != null) {
            sendMemberInfo(event, selfMember);
        } else {
            event.reply("You are not in this guild.").queue();
        }
    }

    public void sendMemberInfo(SlashCommandInteractionEvent event, Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("Info for " + member.getUser().getAsTag());
        embedBuilder.addField("Joined at", member.getTimeJoined().toString(), true);
        embedBuilder.addField("User ID", member.getId(), true);

        event.replyEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public String getDescription() {
        return "Get info about a user.";
    }

    @Override
    public boolean isSpecificGuild() {
        return false;
    }

    @Override
    public boolean isGuildOnly() {
        return true;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.USER, "member", "The member to get info about.", false)
        );
    }
}
