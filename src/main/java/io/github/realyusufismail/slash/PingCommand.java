package io.github.realyusufismail.slash;

import io.github.realyusufismail.handler.Slash;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand implements Slash {

    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        event.reply("pong").queue();
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Returns with Pong";
    }

    @Override
    public boolean isSpecificGuild() {
        return false;
    }

    @Override
    public boolean isGuildOnly() {
        return false;
    }
}
