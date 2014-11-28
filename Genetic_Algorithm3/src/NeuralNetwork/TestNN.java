/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class TestNN {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<Double> inputs = new ArrayList<>();
        ArrayList<Double> weights = new ArrayList<>();
        
        inputs.add((double) 1);
        inputs.add((double) -1);
        
        weights.add((double) 0.5);
        weights.add((double) 0);
        weights.add((double) -0.5);
        weights.add((double) 0.2);
        weights.add((double) -0.1);
        weights.add((double) 0.1);
        
        NeuralNetwork network = new NeuralNetwork(inputs, weights,2);
        System.out.println(network.outputNode.getOutput());
        
    }
    
}
