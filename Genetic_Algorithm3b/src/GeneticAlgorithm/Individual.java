package GeneticAlgorithm;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Alex
 */
public class Individual {
    private int[] gene;
    private int fitness;
    private Random rn;

    public Individual(int[] gene) {
        this.gene = gene;
        //this.fitness = this.updateFitness();
        this.fitness = 0;
        rn = new Random();
    }

    public Individual(int size) {
        gene = new int[size];
        rn = new Random();
        this.populate();
        this.fitness = 0;
        //fitness = this.updateFitness();
    }

    private void populate() {
        for (int i = 0; i < gene.length; i++) {
            //gene[i] = Math.round(rn.nextFloat());
            if ((i+1)%(gene.length/GeneticAlgorithm.NUMBER_OF_RULES)==0){
                gene[i] = rn.nextInt(2);
            } else {
                gene[i] = rn.nextInt(3);
            }
        }
    }

    public Individual[] crossover(Individual parent2) {
        Individual parent1 = this;
        int crossoverPoint = generateCrossOverPoint();
        int[] g1, g2;

        g1 = new int[gene.length];
        g2 = new int[gene.length];
        
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
        int[] tempGene = this.gene;
        for (int i = 0; i < tempGene.length; i++) {
            if (rn.nextDouble() <= mutationRate){
                switch (tempGene[i]){
                    case 0:
                        // We need to know if we are accidentally mutating the outcome for the rule
                        if (((i+1)%(gene.length/GeneticAlgorithm.NUMBER_OF_RULES)==0) || (rn.nextInt(2) == 0)){
                            tempGene[i] = 1;
                        } else {
                            tempGene[i] = 2;
                        }
                        break;
                    case 1:
                        // We need to know if we are accidentally mutating the outcome for the rule
                        if (((i+1)%(gene.length/GeneticAlgorithm.NUMBER_OF_RULES)==0) || (rn.nextInt(2) == 0)){
                            tempGene[i] = 0;
                        } else {
                            tempGene[i] = 2;
                        }
                        break;
                    case 2:
                        tempGene[i] = rn.nextInt(2);
                        break;
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
            out = out.concat(Integer.toString(gene[i]));
        }
        return out;
    }

    public int[] getGene() {
        return gene;
    }

    public void setGene(int[] gene) {
        this.gene = gene;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

}
