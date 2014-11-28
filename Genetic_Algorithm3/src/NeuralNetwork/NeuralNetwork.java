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
public class NeuralNetwork {
    ArrayList<Node> hiddenLayer;
    Node outputNode;
    
    public NeuralNetwork(ArrayList<Double> inputs, ArrayList<Double> weights, int numberOfNodes){
        int lb = 0;
        int ub = inputs.size();
        hiddenLayer = new ArrayList<Node>();
        
        for (int i = 0; i < numberOfNodes; i++) {
            hiddenLayer.add(new Node(inputs, new ArrayList<>(weights.subList(lb, ub)), weights.get(ub)));
            lb += inputs.size()+1;
            ub += inputs.size()+1;
        }
        
        ArrayList<Double> hiddenOutputs = new ArrayList<>();
        for (Node n : hiddenLayer) {
            hiddenOutputs.add(n.getOutput());
        }
        
        outputNode = new Node(hiddenOutputs,new ArrayList<>(weights.subList((weights.size()-numberOfNodes)-1, weights.size()-1)), weights.get(weights.size()-1));
    }
    
    public Node getOutputNode() {
        return outputNode;
    }
    
}
