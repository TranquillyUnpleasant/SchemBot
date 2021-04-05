package schembot;

import arc.files.Fi;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.Schematic;
import mindustry.game.Schematics;
import mindustry.type.ItemStack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Message;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.UUID;

import static schembot.Messages.testGuild;
import static schembot.SchemBot.*;

public class Commands{
    void handle(Message message){
        if(message.getAuthor().isBot() || message.getChannel().getType() != ChannelType.TEXT) return;
        //schematic preview
        Message.Attachment attachment = message.getAttachments().size() == 1 ? message.getAttachments().get(0) : null;
        if((message.getContentRaw().startsWith(ContentHandler.schemHeader) && message.getAttachments().isEmpty()) ||
                (attachment != null && attachment.getFileExtension() != null && attachment.getFileExtension().equals(Vars.schematicExtension))){
            try{
                Schematic schem = attachment != null ? contentHandler.parseSchematicURL(attachment.getUrl()) : contentHandler.parseSchematic(message.getContentRaw());
                BufferedImage preview = contentHandler.previewSchematic(schem);
                String sname = schem.name().replace("/", "_").replace(" ", "_");
                if(sname.isEmpty()) sname = "untitled";

                new File("cache").mkdir();
                File previewFile = new File("cache/img_" + UUID.randomUUID().toString() + ".png");
                File schemFile = new File("cache/" + sname + "." + Vars.schematicExtension);
                Schematics.write(schem, new Fi(schemFile));
                ImageIO.write(preview, "png", previewFile);

                EmbedBuilder builder = new EmbedBuilder().setColor(messages.normalColor).setColor(messages.normalColor)
                .setImage("attachment://" + previewFile.getName())
                .setAuthor(message.getAuthor().getName(), message.getAuthor().getAvatarUrl(), message.getAuthor().getAvatarUrl()).setTitle(schem.name());

                if(!schem.description().isEmpty()) builder.setFooter(schem.description());

                StringBuilder field = new StringBuilder();

                for(ItemStack stack : schem.requirements()){
                    List<Emote> emotes = testGuild.getEmotesByName(stack.item.name.replace("-", ""), true);
                    if (!emotes.isEmpty()) {
                        field.append(emotes.get(0).getAsMention()).append(stack.amount).append("⠀");
                    }
                }
                builder.addField("Requirements", field.toString(), false);

                message.getChannel().sendFile(schemFile).addFile(previewFile).embed(builder.build()).queue();
                message.delete().queue();
            }catch(Throwable e){
                Log.err("Failed to parse schematic, skipping.");
                Log.err(e);
            }
        }
    }
}
