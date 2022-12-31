package com.diamondgoobird.mod;

import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.diamondgoobird.mod.ModManager.*;

/***
 * @author Diamondgoobird
 */
public class Config {
    public static Variable varFromArray(String name, Variable[] array) {
        for (Variable var : array) {
            if (var.Name.equalsIgnoreCase(name)) {
                return var;
            }
        }
        return new Variable();
    }
    public static String[] checkConfig(String[] input) {
        createConfig();
        if (input == null) {
            input = (String[]) getVariableValues("Name");
        }
        String[] results = new String[input.length];
        int loopedtimes = 0;
        while (results.length > loopedtimes) {
            results[loopedtimes] = checkVariable(input[loopedtimes]);
            loopedtimes++;
        }

        return results;
    }
    public static String checkVariable(String variable) {
        String[] File = readFile();
        int loopedtimes = 0;
        String Line = "";
        while (File.length > loopedtimes) {
            if (File[loopedtimes] == null) {
                break;
            }
            else if (File[loopedtimes].contains(variable)) {
                Line = File[loopedtimes + 1];
            }
            loopedtimes++;
        }
        if (variable.contains("Chat Color")) {
            Line = wordToColorCode(Line);
        }
        return Line;
    }
    public static String wordToColorCode(String input) {
        EnumChatFormatting a = EnumChatFormatting.getValueByName(input);
        if (a == null) {
            return input;
        }
        return a.toString();
    }
    public static String[] checkList(String variable) {
        String[] config = readFile();
        ArrayList<String> results = new ArrayList<>();
        Variable nextVar;
        try {
            int variableIndex = Arrays.asList(Variables).indexOf(varFromArray(variable, Variables));
            nextVar = Variables[variableIndex + 1];
        }
        catch (Exception h) {
            nextVar = null;
        }
        boolean start = false;
        for (String line : config) {
            if (line == null) {
                break;
            }
            else if (line.contains(variable) && line.contains("(list)")) {
                start = true;
            }
            else if (start) {
                if (nextVar != null) {
                    if (line.contains(nextVar.Name) || line.equals("") || line.equals(" ")) {
                        break;
                    } else {
                        results.add(line);
                    }
                }
                else {
                    if (line.equals("") || line.equals(" ")) {
                        break;
                    }
                    results.add(line);
                }
            }
        }
        return results.toArray(new String[0]);
    }
    public static void toggleVariable(String variable) {
        if (checkVariable(variable).equalsIgnoreCase("Disabled")) {
            changeVariable(variable,"Enabled");
        }
        else {
            changeVariable(variable,"Disabled");
        }
    }
    public static void changeVariable(String variable, String... value) {
        silentChangeVariable(variable,value);
        print(checkVariable("Chat Color 1") + "Successfully Changed Variable " + checkVariable("Chat Color 2") + variable + checkVariable("Chat Color 1") + " to " + checkVariable("Chat Color 2") + value[0]);
    }
    public static void silentChangeVariable(String variable, String... value) {
        int x = 0;
        int index = -1;
        String[] names = (String[]) getVariableValues("Name");
        while (names.length > x) {
            if (names[x].equalsIgnoreCase(variable)) {
                index = x;
                break;
            }
            x++;
        }
        readConfig();
        if (value.length > 1) {
            ArrayList<String> values = new ArrayList<>(Arrays.asList(value));
            Variables[index] = new Variable(Variables[index],values);
        }
        else if (value.length == 1) {
            Variables[index] = new Variable(Variables[index], value);
        }
        printDebug("Changed variable (" + Variables[index].Name + ") to value (" + Variables[index].Variable + ")");
        saveConfig();
    }
    public static String[] getVariableValues(String value) {
        ArrayList<String> z = new ArrayList<>();
        switch (value) {
            case ("Name"):
                for (Variable item : Variables) {
                    z.add(item.Name);
                }
                break;
            case ("Variable"):
            case ("Value"):
                for (Variable variable : Variables) {
                    if (variable.Variable == null || variable.Variable.toString().equals("null")) {
                        z.add(null);
                    }
                    else {
                        z.add(variable.Variable);
                    }
                }
                break;
            case ("Options"):
                for (Variable variable : Variables) {
                    z.add(variable.Options);
                }
                break;
            case ("List"):
                for (Variable variable : Variables) {
                    if (variable.List==null) {
                        z.add(null);
                    }
                    else {
                        //printDebug(arrayToString(variable.List));
                        z.add(arrayToString(variable.List));
                    }
                }
        }
        return z.toArray(new String[0]);
    }
    public static boolean isVariableName(String text) {
        String[] Names = (String[]) getVariableValues("Name");
        for (String name : Names) {
            if (text.contains(name)) {
                return true;
            }
        }
        return false;
    }
    public static String[] readFile() {
        createConfig();
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BufferedReader rfb = new BufferedReader(fr);
        int loopedtimes = 0;
        String[] ez = new String[1000];
        boolean stop = false;
        while (!stop) {
            try {
                ez[loopedtimes] = rfb.readLine();
                if (ez[loopedtimes] == null) {
                    stop = true;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            loopedtimes++;
        }

        return ez;
    }
    public static void createConfig() {
        if (!file.exists()) {
            saveConfig();
        }
    }
    public static void readConfig() {
        String[] config = checkConfig(null);
        int i = 0;
        while (i < config.length) {
            if (Variables[i].Options.equals("list")) {
                ArrayList<String> list = new ArrayList<>(Arrays.asList(checkList(Variables[i].Name)));
                Variables[i] = new Variable(Variables[i],list);
            }
            else {
                Variables[i] = new Variable(Variables[i], config[i]);
            }
            i++;
        }
    }
    public static void saveConfig() {
        int loopedtimes = 0;
        String everything = "Config for " + MOD_ID + "\n\n";
        String[] Names = getVariableValues("Name");
        String[] Options = getVariableValues("Options");
        String[] Values = getVariableValues("Value");
        String[] Lists = getVariableValues("List");
        while (Names.length > loopedtimes) {
            String suffix = "\n\n";
            if (Names.length == (loopedtimes + 1)) {
                suffix = "";
            }
            if (Values[loopedtimes] != null) {
                everything = everything + Names[loopedtimes] + " (" + Options[loopedtimes] + "):\n" + Values[loopedtimes] + suffix;
            }
            else if (Lists[loopedtimes] != null) {
                everything = everything + Names[loopedtimes] + " (" + Options[loopedtimes] + "):\n" + Lists[loopedtimes] + suffix;
            }
            else {
                everything = everything + Names[loopedtimes] + " (" + Options[loopedtimes] + "):\n" + Values[loopedtimes] + suffix;
                System.out.println("Error found when adding variable: " + Names[loopedtimes] + ", " + Options[loopedtimes] + ", " + Values[loopedtimes]);
            }
            loopedtimes++;
        }
        CharSequence data = everything;
        try {
            FileUtils.write(file, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
