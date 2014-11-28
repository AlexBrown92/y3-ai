package GeneticAlgorithm;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        rn = GeneticAlgorithm.rn;
    }

    public Individual(int size) {
        gene = new int[size];
        rn = GeneticAlgorithm.rn;
        this.populate();
    }

    private void populate() {
        for (int i = 0; i < gene.length; i++) {
            gene[i] = rn.nextInt(2);

        }
    }

    public Individual[] crossover(Individual parent2) {
        Individual parent1 = this;
        int crossoverPoint = generateCrossOverPoint();
        int[] g1, g2;

        g1 = new int[gene.length];
        g2 = new int[gene.length];

        for (int i = 0; i < gene.length; i++) {
            if (i < crossoverPoint) {
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

    private int generateCrossOverPoint() {
        //int ub = (int) Math.round(gene.length * 0.9);
        //int lb = (int) Math.round(gene.length * 0.6);
        int ub = gene.length - 1;
        int lb = 1;
        int point = rn.nextInt(ub - lb) + lb;
        return point;
    }

    public Individual mutate(double mutationRate) {
        int[] tempGene = this.gene;
        for (int i = 0; i < tempGene.length; i++) {
            if (rn.nextDouble() <= mutationRate) {
                if (tempGene[i] == 0) {
                    tempGene[i] = 1;
                } else {
                    tempGene[i] = 0;
                }
            }
        }
        return new Individual(tempGene);
    }

    public void updateFitness() {
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
