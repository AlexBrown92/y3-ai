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
public class Node {

    private ArrayList<Double> inputs;
    private ArrayList<Double> weights;
    private double bias;
    private double output;

    public Node(ArrayList<Double> inputs, ArrayList<Double> weights, double bias) {
        this.inputs = inputs;
        this.weights = weights;
        this.bias = bias;
        this.output = 0;
    }
    
    public Node(){
        this.inputs = new ArrayList<>();
        this.weights = new ArrayList<>();
        this.bias = 0;
        this.output = 0;
    }
    
    private void calculateOutput(){
        double inputWeightSum = 0;
        for (int i = 0; i < inputs.size(); i++) {
            inputWeightSum += (inputs.get(i) * weights.get(i));
        }
        inputWeightSum += bias;
        this.output = sigmoidFunction(inputWeightSum);
    }
    
    private double sigmoidFunction(double sum){
        return 1 / (1 + Math.exp(-(sum)));
    }

    public ArrayList<Double> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<Double> inputs) {
        this.inputs = inputs;
    }

    public double getInput(int index) {
        return inputs.get(index);
    }

    public void setInput(double input, int index) {
        this.inputs.set(index, input);
    }

    public void addInput(double input) {
        this.inputs.add(input);
    }

    public double removeInput(int index) {
        return inputs.remove(index);
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    public double getWeight(int index) {
        return weights.get(index);
    }

    public void setWeight(double weight, int index) {
        this.weights.set(index, weight);
    }

    public void addWeight(double weight) {
        this.weights.add(weight);
    }

    public double removeWeight(int index) {
        return weights.remove(index);
    }

    public double getOutput() {
        calculateOutput();
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

}
