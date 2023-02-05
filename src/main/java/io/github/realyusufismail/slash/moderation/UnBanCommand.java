package io.github.realyusufismail.slash.moderation;

import io.github.realyusufismail.handler.Slash;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.pagination.BanPaginationAction;

import java.util.List;
import java.util.Objects;

public class UnBanCommand implements Slash {

    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        User userToUnban = event.getOption("user", OptionMapping::getAsUser);
        String reason = event.getOption("reason", OptionMapping::getAsString);
        Member commandRunner = Objects.requireNonNull(event.getMember(), "Command Runner is null");
        Member bot = Objects.requireNonNull(guild, "Bot is null").getSelfMember();

        checkForRightPerms(commandRunner, bot, event);

        if (!checkIfUserIsBanned(userToUnban, guild, event)) {
            event.reply("The user who you tried to unban is not banned.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        guild.unban(userToUnban).reason(reason)
                .queue(
                s -> event.reply("The user has been successfully unbanned").setEphemeral(true).queue(),
                f -> event.reply("Failed to unban the user").setEphemeral(true).queue()
        );
    }

    private void checkForRightPerms(Member commandRunner, Member bot, SlashCommandInteractionEvent event) {
        if (!commandRunner.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("You can not unban the user as you dont have the BAN_MEMBERS perm").queue();
            return;
        }

        if (!bot.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("I can not unban the user as I dont have the BAN_MEMBERS perm").queue();
        }
    }

    private boolean checkIfUserIsBanned(User userToUnban, Guild guild, SlashCommandInteractionEvent event) {
        BanPaginationAction bannedUsers = Objects.requireNonNull(guild, "Ban List is null").retrieveBanList();

        return bannedUsers.stream()
                .anyMatch(ban -> ban.getUser().getId().equals(userToUnban.getId()));
    }


    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public String getDescription() {
        return "Used to unban a user";
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
                new OptionData(OptionType.USER, "user", "The user to unban", true),
                new OptionData(OptionType.STRING, "reason", "The reason for unbanning the user", true)
        );
    }
}
