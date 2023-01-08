package io.github.realyusufismail;

import io.github.realyusufismail.handler.SlashCommandHandler;
import io.github.realyusufismail.jconfig.util.JConfigUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.util.Objects;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        JDA jda = JDABuilder.createDefault(JConfigUtils.getString("token"))
                .build();

        jda.awaitReady().addEventListener(new ListenerEvents(), new SlashCommandHandler(jda, Objects.requireNonNull(jda.getGuildById("938122131949097052"))));
    }
}