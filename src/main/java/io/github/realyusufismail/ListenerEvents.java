package io.github.realyusufismail;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListenerEvents extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ListenerEvents.class);

    @Override
    public void onReady(ReadyEvent event) {
        logger.info("Logged in as {}", event.getJDA().getSelfUser().getAsTag());
    }
}
