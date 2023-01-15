package io.github.realyusufismail.slash;

import io.github.realyusufismail.handler.Slash;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Objects;

public class OptionCommand implements Slash {
    @Override
    public void onSlashCommandEvent(SlashCommandInteractionEvent event) {
        String yesOrNo = event.getOption("option", OptionMapping::getAsString);

        if (Objects.equals(yesOrNo, "yes")) {
            event.reply("You said yes. Have a cookie! :cookie:").queue();
        } else {
            event.reply("You said no. You don't get a cookie. :cookie:").queue();
        }
    }

    @Override
    public String getName() {
        return "wantcookie";
    }

    @Override
    public String getDescription() {
        return "Do you want a cookie?";
    }

    @Override
    public boolean isSpecificGuild() {
        return false;
    }

    @Override
    public boolean isGuildOnly() {
        return false;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "option", "Do you want a cookie?", true)
                        .addChoice("Yes", "yes")
                        .addChoice("No", "no")
        );
    }
}
