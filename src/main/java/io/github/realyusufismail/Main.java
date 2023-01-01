package io.github.realyusufismail;

import io.github.realyusufismail.jconfig.util.JConfigUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(JConfigUtils.getString("token"))
                .build();

        jda.addEventListener(new ListenerEvents());
    }
}