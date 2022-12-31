package com.diamondgoobird.mod;

import java.util.ArrayList;
import java.util.Arrays;
/***
 * @author Diamondgoobird
 */

public class Variable {
    public final String Name;
    public final String Variable;
    public final String Options;
    public ArrayList<String> List;
    public Variable(String name, String variable, String options) {
        Name = name;
        Variable = variable;
        Options = options;
        List = null;
    }
    public Variable(String name, ArrayList<String> list, String options) {
        Name = name;
        List = list;
        Options = options;
        Variable = null;
    }
    public Variable(Variable oldVariable, String newVariable) {
        Name = oldVariable.Name;
        Variable = newVariable;
        Options = oldVariable.Options;
        List = null;
    }
    public Variable(Variable oldVariable, String[] newVariable) {
        Name = oldVariable.Name;
        Variable = null;
        Options = oldVariable.Options;
        List = new ArrayList<>(Arrays.asList(newVariable));
    }
    public Variable(Variable oldVariable, ArrayList<String> newVariable) {
        Name = oldVariable.Name;
        List = newVariable;
        Options = oldVariable.Options;
        Variable = null;
    }
    public Variable() {
        Name = null;
        List = null;
        Options = null;
        Variable = null;
    }
}