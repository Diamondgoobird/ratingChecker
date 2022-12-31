package com.diamondgoobird.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.File;
import java.util.ArrayList;

import static com.diamondgoobird.mod.Config.*;

/***
 * @author Diamondgoobird
 */
public class ModManager {
    public static final String MOD_ID = "ratingchecker";
    public static Variable[] Variables = new Variable[]{
        new Variable("Rating","0","Integer")
    };
    public static final String minecraftPath = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    public static final String Path = minecraftPath + "\\diamondmods\\";
    public static final String ConfigPath = Path + MOD_ID + ".txt";
    public static File file = new File(ConfigPath);

    public static void print(String input) {
        try {
            Minecraft.getMinecraft().thePlayer.addChatMessage(formatColors(input));
        }
        catch (Exception h) {
            System.out.println(input);
        }
    }

    public static String wordToColorCode(String input) {
        EnumChatFormatting a = EnumChatFormatting.getValueByName(input);
        if (a == null) {
            return input;
        }
        return a.toString();
    }

    public static IChatComponent formatColors(String input) {
        ChatComponentText e = new ChatComponentText(input);
        char[] a = new char[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','k','l','m','n','o','r'};
        int x = 0;
        int b = 22;
        while (x < b) {
            e = new ChatComponentText(e.getUnformattedText().replaceAll("&" + a[x], "\u00a7" + a[x]));
            e = new ChatComponentText(e.getUnformattedText().replaceAll("&&", "&"));
            x++;
        }
        return e;
    }

    public static void printDebug(String input) {
        if (debug()) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(formatColors(input));
        }
    }

    public static boolean debug() {
        return Config.checkVariable("Debug").equals("Enabled");
    }

    public static String arrayToString(ArrayList<String> list) {
        StringBuilder end = new StringBuilder();
        for (String string : list) {
            if (list.get(list.size()-1).equals(string)) {
                end.append(string);
            }
            else {
                end.append(string).append("\n");
            }
        }
        return end.toString();
    }

    public static void printChat(IChatComponent input) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(input);
    }

    public static void printLines(IChatComponent[] input) {
        int z = 0;
        while (input.length > z) {
            printChat(input[z]);
            z++;
        }
    }

    public static boolean contains(String checking, String[] check) {
        boolean result = false;
        try {
            int x = 0;
            while (check.length > x) {
                if (check[x] != null) {
                    if (checking.contains(check[x])) {
                        //System.out.println(checking + " contains " + check[x]);
                        result = true;
                    }
                }
                x++;
            }
        }
        catch (Exception e) {

        }
        return result;
    }
}
