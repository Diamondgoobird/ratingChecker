package com.diamondgoobird.mod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import static com.diamondgoobird.mod.Config.*;
import static com.diamondgoobird.mod.ModManager.print;

@Mod(modid = "ratingChecker", name = "Rating Checker")
public class RatingChecker {
    public static boolean searching = false;
    public static int rating;
    public static boolean loud = true;
    public static long lastChecked = System.currentTimeMillis();
    public static int highestRating = 0;
    public static KeyBinding getRating = new KeyBinding("Check rating", Keyboard.KEY_LMENU, "Rating Checker");
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientRegistry.registerKeyBinding(getRating);
        ClientCommandHandler.instance.registerCommand(new RatingCommand());
        MinecraftForge.EVENT_BUS.register(this);
    }
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        highestRating = Integer.parseInt(Config.checkVariable("Rating"));
    }
    @SubscribeEvent
    public void keyListener(InputEvent.KeyInputEvent event) {
        if (getRating.isPressed()) {
            if (lastChecked + 15000 < System.currentTimeMillis()) {
                lastChecked = System.currentTimeMillis();
                getRating(false);
            }
            else {
                print(EnumChatFormatting.AQUA + "Please wait: " + EnumChatFormatting.BLUE +  ((double) (lastChecked - System.currentTimeMillis() + 15000) / 1000) + EnumChatFormatting.AQUA + " seconds to try again.");
            }
        }
    }

    @SubscribeEvent
    public void ratingListener(ClientChatReceivedEvent event) {
        if (!searching) {
            return;
        }
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if (
                message.contains("Current EXP:") ||
                message.contains("Overall Wins:") ||
                message.contains("Overall Kills:") ||
                message.contains("Monthly Kills:")
        ) {
            event.setCanceled(true);
        }
        else if (message.contains("Overall Rating: ")) {
            event.setCanceled(true);
            message = message.replace("Overall Rating: ","");
            char[] stuff = message.toCharArray();
            String result = "";
            for (char thing : stuff) {
                if (thing == ' ') {
                    break;
                }
                else if (thing == ',') {

                }
                else {
                    try {
                        int a = Integer.parseInt(String.valueOf(thing));
                        result += a;
                    }
                    catch (Exception e) {
                    
                    }
                }
            }
            rating = Integer.parseInt(result);
            if (rating > highestRating) {
                highestRating = rating;
                changeVariable("Rating", String.valueOf(highestRating));
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "You got a new highest rating of: " + EnumChatFormatting.GOLD + highestRating));
            }
            else if (loud) {
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.AQUA + "" +
                        //"b" +
                        "Your rating is: " + EnumChatFormatting.YELLOW + rating));
            }
        }
        else if (message.contains("Weekly Kills:")) {
            searching = false;
            event.setCanceled(true);
        }
    }
    public static void getRating(boolean silent) {
        loud = !silent;
        searching = true;
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/mypos skywars");
    }

    public static int getRating() throws InterruptedException {
        loud = false;
        searching = true;
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/mypos skywars");
        while (searching) {
            Thread.sleep(100);
        }
        return rating;
    }

    @SubscribeEvent
    public void CommandFailListener(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        if (message.startsWith("Command Failed: This command is on cooldown! Try again in about ")) {
            System.out.println(message);
            if (searching) {
                int delay;
                message = message.replace("Command Failed: This command is on cooldown! Try again in about ","");
                message = message.replace(" seconds!","");
                try {
                    delay = Integer.parseInt(message);
                } catch (Exception h) { delay = 11; }
                try {
                    int finalDelay = delay + 1;
                    new Thread( () -> {
                        try {
                            Thread.sleep(finalDelay * 1000L);
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/mypos skywars");
                        } catch (InterruptedException h) {
                            System.out.println("Failed to sleep for " + finalDelay + " seconds");
                        }
                    }).start();
                } catch (Exception h) {
                    System.out.println("Failed to set finalDelay to " + delay + " + 1");
                }
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void locrawListener(ClientChatReceivedEvent event) {
        String message = EnumChatFormatting.getTextWithoutFormattingCodes(event.message.getUnformattedText());
        try {
            JsonParser parser = new JsonParser();
            JsonObject data = parser.parse(message).getAsJsonObject();
            String game = data.get("gametype").getAsString();
            String mode = data.get("mode").getAsString();
            if (game.equals("SKYWARS") && mode.equals("ranked_normal")) {
                new Thread( () -> {
                    getRating(true);
                }).start();
            }
        }
        catch (Exception h) {

        }
    }
}