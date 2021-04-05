package schembot;

public final class SchemBot {
    public static final long guildID = 623884757268299786L;
    public static final long testGuildID = 720897247952371712L;

    public static ContentHandler contentHandler = new ContentHandler();
    public static Messages messages = new Messages();
    public static Commands commands = new Commands();
    public static Net net = new Net();

    public static void main(String[] args){
        new SchemBot();
    }
}
