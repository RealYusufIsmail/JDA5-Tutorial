package io.github.realyusufismail.handler;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.ArrayList;
import java.util.List;

public interface Slash {

    void onSlashCommandEvent(SlashCommandInteractionEvent event);

    String getName();

    String getDescription();

    /**
     * Only appears in guilds which have been specified using there id
     *
     * @return Whether the command is guild only for specific guilds
     */
    boolean isSpecificGuild();

    /**
     * Whether the command can appear in dms or not
     *
     * @return Whether the command can appear in dms or not
     */
    boolean isGuildOnly();

    default List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    default CommandData getCommandData() {
        return new CommandDataImpl(getName(), getDescription())
                .setGuildOnly(isGuildOnly())
                .addOptions(getOptions());
    }
}
