package io.github.realyusufismail.slash.moderation;

import io.github.realyusufismail.handler.Slash;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BanCommand implements Slash {
    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        OptionMapping userOption = event.getOption("user");
        User userToBan = userOption == null ? null : userOption.getAsUser();
        Member memberToBan = userOption == null ? null : userOption.getAsMember();
        Member commandRunner = event.getMember();
        Member bot = event.getGuild().getSelfMember();
        Integer messageDeleteDuration = event.getOption("message_delete_duration", OptionMapping::getAsInt);
        String reason = event.getOption("reason", OptionMapping::getAsString);

        if (messageDeleteDuration == null) {
            messageDeleteDuration = 0;
        }

        if (memberToBan != null) {
            runBanMember(event, memberToBan, commandRunner, bot, messageDeleteDuration, reason);
        } else if (userToBan != null) {
            runBanUser(event, userToBan, commandRunner, bot, messageDeleteDuration, reason);
        } else {
            event.reply("User/Member to ban is null").setEphemeral(true).queue();
        }
    }

    private void runBanUser(SlashCommandInteractionEvent event, User userToBan, Member commandRunner, Member bot, Integer messageDeleteDuration, String reason) {
        checkPerms(event, commandRunner, bot);
        triggerBan(event, userToBan, commandRunner, messageDeleteDuration, reason);
    }

    private void runBanMember(SlashCommandInteractionEvent event, Member memberToBan, Member commandRunner, Member bot, Integer messageDeleteDuration, String reason) {
        checkPerms(event, commandRunner, bot);

        if (!commandRunner.canInteract(memberToBan)) {
            event.reply("You cant not ban this user as they more perms than you").queue();
            return;
        }

        if (!bot.canInteract(memberToBan)) {
            event.reply("I cant not ban this user as they more perms than me").queue();
            return;
        }

        triggerBan(event, memberToBan.getUser(), commandRunner, messageDeleteDuration, reason);
    }

    private void triggerBan(SlashCommandInteractionEvent event, User userBan, Member commandRunner, Integer messageDeleteDuration, String reason) {
        event.getGuild().ban(UserSnowflake.fromId(userBan.getId()), messageDeleteDuration, TimeUnit.DAYS)
                .reason(reason)
                .queue(s -> event.reply(commandRunner.getUser().getAsTag() + " successfully banned the user " + userBan.getAsTag()).queue(),
                        f  -> event.reply("Failed to ban the user").setEphemeral(true).queue()
                );
    }

    private void checkPerms(SlashCommandInteractionEvent event, Member commandRunner, Member bot) {
        if (!commandRunner.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("You can not ban the user as you dont have the BAN_MEMBERS perm").queue();
            return;
        }

        if (!bot.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("I can not ban the user as I dont have the BAN_MEMBERS perm").queue();
        }
    }


    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public String getDescription() {
        return "Used to ban a user";
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
                new OptionData(OptionType.USER, "user", "The user who you want to ban", true),
                new OptionData(OptionType.STRING, "reason", "The reason why you want to ban the user", true),
                new OptionData(OptionType.INTEGER, "message_delete_duration", "The amount of days of messages to delete")
                        .setRequiredRange(1, 28)
        );
    }
}
