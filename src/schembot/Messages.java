package schembot;

import arc.Core;
import arc.files.Fi;
import arc.util.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;

import static schembot.SchemBot.*;

public class Messages extends ListenerAdapter {
    public static JDA jda;
    public static Guild guild;
    public static Guild testGuild;
    public Color normalColor = Color.decode("#00FF7F");
    public JSONObject data;

    public Messages() {
        String pureJson = new Fi("./settings.json").readString();
        data = new JSONObject(new JSONTokener(pureJson));
        String token = data.getString("token");
        Log.info("Found token: @", token != null);

        try {
            jda = JDABuilder.createDefault(token).disableCache(CacheFlag.VOICE_STATE).build();
            jda.awaitReady();
            jda.addEventListener(this);
            guild = jda.getGuildById(guildID);
            testGuild = jda.getGuildById(testGuildID);

            Log.info("Logged in as @!", jda.getSelfUser().getAsTag());
            Core.net = new arc.Net();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {
            commands.handle(event.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
