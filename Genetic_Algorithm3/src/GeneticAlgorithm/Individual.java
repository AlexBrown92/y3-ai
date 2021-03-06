package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class Individual {
    private double[] gene;
    private int fitness;
    private Random rn;

    public Individual(double[] gene) {
        this.gene = gene;
        //this.fitness = this.updateFitness();
        this.fitness = 0;
        rn = new Random();
    }

    public Individual(int size) {
        gene = new double[size];
        rn = new Random();
        this.populate();
        this.fitness = 0;
        //fitness = this.updateFitness();
    }

    private void populate() {
        for (int i = 0; i < gene.length; i++) {
            if(rn.nextBoolean()){
                gene[i] = rn.nextDouble();
            } else {
                gene[i] = -(rn.nextDouble());
            }
        }
    }

    public Individual[] crossover(Individual parent2) {
        Individual parent1 = this;
        int crossoverPoint = generateCrossOverPoint();
        double[] g1, g2;

        g1 = new double[gene.length];
        g2 = new double[gene.length];
        
        for (int i = 0; i < gene.length; i++) {
            if (i < crossoverPoint){
                g1[i] = parent1.getGene()[i];
                g2[i] = parent2.getGene()[i];
            } else {
                g1[i] = parent2.getGene()[i];
                g2[i] = parent1.getGene()[i];
            }
        }
        
        Individual[] children = new Individual[2];
        children[0] = new Individual(g1);
        children[1] = new Individual(g2);

        return children;
    }
    
    private int generateCrossOverPoint(){
        int ub = gene.length -1;
        int lb = 1;
        int point = rn.nextInt(ub - lb) + lb;
        return point; 
    }


    
    public Individual mutate(double mutationRate){
        double[] tempGene = this.gene;
        for (int i = 0; i < tempGene.length; i++) {
            if (rn.nextDouble() <= mutationRate){
                double modifier;
                if (rn.nextBoolean()){
                    modifier = rn.nextDouble() * GeneticAlgorithm.MAX_MUTATION;
                } else {
                    modifier = -(rn.nextDouble() * GeneticAlgorithm.MAX_MUTATION);
                }
                if ((tempGene[i] + modifier) > 1){
                    tempGene[i] = 1;
                } else if ((modifier + tempGene[i]) < -1){
                    tempGene[i] = -1;
                } else {
                    tempGene[i] += modifier;
                }
            }
        }
        return new Individual(tempGene);
    }
    
    public void updateFitness(){
        this.fitness = GeneticAlgorithm.calculateFitness(this);
    }

    public String displayGene() {
        String out = "";
        for (int i = 0; i < gene.length; i++) {
            out = out.concat(" " + Double.toString(gene[i]));
        }
        return out;
    }

    public double[] getGene() {
        return gene;
    }
    
    public ArrayList<Double> getGeneArrayList(){
        ArrayList<Double> al = new ArrayList<>();
        for (double g : gene) {
            al.add(g);
        }
        return al;
    }

    public void setGene(double[] gene) {
        this.gene = gene;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

}
