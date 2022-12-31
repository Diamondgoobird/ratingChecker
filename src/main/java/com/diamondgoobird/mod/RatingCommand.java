package com.diamondgoobird.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

import static com.diamondgoobird.mod.RatingChecker.*;

public class RatingCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }
    @Override
    public String getCommandName() {
        return "rating";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) {

        }
        else if (args[0].equalsIgnoreCase("check")) {
            getRating(false);
        }
        else if (args[0].equalsIgnoreCase("wins")) {
            if (args.length < 2) {
                return;
            }
            int desiredRating = 2000;
            try {
                desiredRating = Integer.parseInt(args[1]);
            }
            catch (Exception h) {
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Failed to parse integer " + args[1]));
            }
            int finalDesiredRating = desiredRating;
            new Thread( () -> {
                int rating = 0;
                try {
                    rating = getRating();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                int wins = 0;
                while (rating < finalDesiredRating) {
                    rating = fakeWin(rating);
                    wins++;
                }
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.LIGHT_PURPLE + "You need " + EnumChatFormatting.DARK_PURPLE + wins + EnumChatFormatting.LIGHT_PURPLE + " wins to get to " + EnumChatFormatting.DARK_PURPLE + finalDesiredRating));
            }).start();
        }
    }

    public static int fakeWin(int rating) {
        int winRating = 0;
        if (rating < 500) {
            winRating = rating + 50;
        }
        else if (rating > 500 && rating <= 600) {
            winRating = rating + 42;
        }
        else if (rating > 600 && rating <= 700) {
            winRating = rating + 38;
        }
        else if (rating > 700 && rating <= 800) {
            winRating = rating + 36;
        }
        else if (rating > 800 && rating <= 900) {
            winRating = rating + 34;
        }
        else if (rating > 900 && rating <= 1000) {
            winRating = rating + 32;
        }
        else if (rating > 1000 && rating <= 1100) {
            winRating = rating + 30;
        }
        else if (rating > 1100 && rating <= 1200) {
            winRating = rating + 28;
        }
        else if (rating > 1200 && rating <= 1300) {
            winRating = rating + 26;
        }
        else if (rating > 1300 && rating <= 1400) {
            winRating = rating + 24;
        }
        else if (rating > 1400 && rating <= 1500) {
            winRating = rating + 22;
        }
        else if (rating > 1500 && rating <= 1600) {
            winRating = rating + 20;
        }
        else if (rating > 1600 && rating <= 1700) {
            winRating = rating + 18;
        }
        else if (rating > 1700 && rating <= 1800) {
            winRating = rating + 16;
        }
        else if (rating > 1800 && rating <= 1900) {
            winRating = rating + 14;
        }
        else if (rating > 1900 && rating <= 2000) {
            winRating = rating + 12;
        }
        else if (rating > 2000) {
            winRating = rating + 10;
        }
        return winRating;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length >= 0) {
            return getListOfStringsMatchingLastWord(args, new ArrayList<String>(Arrays.asList(new String[]{"check","wins"})));
        }
        else {
            return null;
        }
    }
}
