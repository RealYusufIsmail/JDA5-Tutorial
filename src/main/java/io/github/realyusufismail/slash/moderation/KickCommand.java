package io.github.realyusufismail.slash.moderation;

import io.github.realyusufismail.handler.Slash;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Objects;

public class KickCommand implements Slash {
    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        Member memberToKick = event.getOption("user", OptionMapping::getAsMember);
        String reason = event.getOption("reason", OptionMapping::getAsString);
        Guild guild = event.getGuild();
        Member bot = Objects.requireNonNull(guild, "Bot is null").getSelfMember();
        Member commandRunner = event.getMember();

        checkForRightPerms(commandRunner, bot, event);

        if (memberToKick == null) {
            event.reply("You can only kick a member").setEphemeral(true).queue();
            return;
        }

        if (!commandRunner.canInteract(memberToKick)) {
            event.reply("You cant kick a person who has more perms than you").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(memberToKick)) {
            event.reply("I cant kick a person who has more perms than you").setEphemeral(true).queue();
            return;
        }

        guild.kick(memberToKick).reason(reason)
                .queue(
                        s -> event.reply("Successful kick the member " + memberToKick.getEffectiveName()).queue(),
                        e -> event.reply("Unable to kick the user and the error is " + e).queue()
                );
    }

    private void checkForRightPerms(Member commandRunner, Member bot, SlashCommandInteractionEvent event) {
        if (!commandRunner.hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("You can not unban the user as you dont have the KICK_MEMBERS perm").queue();
            return;
        }

        if (!bot.hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("I can not unban the user as I dont have the KICK_MEMBERS perm").queue();
        }
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getDescription() {
        return "Used to kick a member";
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
                new OptionData(OptionType.USER, "user", "The user to kick", true),
                new OptionData(OptionType.STRING, "reason", "The reason to kick", true)
        );
    }
}
