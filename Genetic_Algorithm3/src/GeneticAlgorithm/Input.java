/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneticAlgorithm;

import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class Input {
    private ArrayList<Double> inputs;
    private int expected;

    public Input(String line) {
        inputs = new ArrayList<>();
        String[] items = line.split("\\s");
        for (String item : items) {
            inputs.add(Double.parseDouble(item));
        }
        expected = Integer.parseInt(items[items.length-1]);
        inputs.remove(inputs.size()-1); // Last one we added was the expected bit (probably a better way of doing this...)
    }

    public ArrayList<Double> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<Double> inputs) {
        this.inputs = inputs;
    }

    public int getExpected() {
        return expected;
    }

    public void setExpected(int expected) {
        this.expected = expected;
    }
    
    public String display(){
        String outStr = "";
        for (Double input : inputs) {
            outStr = outStr + input;
        }
        outStr = outStr + " " + expected;
        return outStr; 
    }
    
    
}
