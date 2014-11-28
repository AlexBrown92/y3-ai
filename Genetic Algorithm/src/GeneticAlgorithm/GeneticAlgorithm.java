/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneticAlgorithm;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Alex
 */
public class GeneticAlgorithm {

    public static Random rn;
    public static final int GENE_SIZE = 64;
    public static final int POPULATION_SIZE = 50;
    public static final int NUMBER_OF_RUNS = 500;
    public static final double MUTATION_RATE = 0.03;
    public static final double CROSSOVER_RATE = 0.9;
    public static int[] goal;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        rn = new Random();
        int[] epochTaken = new int[10];
        ArrayList<Datum> data = new ArrayList<>();

        String dirPath = "D:\\Dropbox\\Work\\Year 3\\AI\\Code\\Genetic Algorithm\\data\\";
        // Setup scanner on trainingData file
        //File in = new File("C:\\Users\\Alex\\Dropbox\\Work\\Year 3\\AI\\Code\\Genetic Algorithm\\data\\data2.txt"); // Laptop
        File in = new File(dirPath + "data1.txt"); // Desktop
        //File in = new File("F:\\Code\\Genetic_Algorithm2\\data\\\\data1.txt"); // USB Drive

        Scanner scan = new Scanner(in);
        scan.nextLine();

        // Get an integer array which is the goal for our system
        goal = new int[64];
        int count = 0;
        while (scan.hasNext()) {
            String line = scan.nextLine();
            int split = line.indexOf(' ');
            int[] tmpValue = new int[split];
            for (int i = 0; i < split; i++) {
                tmpValue[i] = Character.getNumericValue(line.charAt(i));
            }
            goal[count] = Character.getNumericValue(line.charAt(split + 1));
            count++;
        }
        /*for (int h : goal) {
         System.out.println(h);
         }*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        File dir = new File(dirPath + timeStamp + "\\");
        if (!dir.exists()) {
            dir.mkdir();
        }
        // Create a settings file and enter run settings values
        File s = new File(dirPath + timeStamp + "\\settings.txt");
        PrintWriter settings = new PrintWriter(s, "UTF-8");
        settings.println("POPULATION_SIZE: " + POPULATION_SIZE);
        settings.println("NUMBER_OF_RUNS: " + NUMBER_OF_RUNS);
        settings.println("MUTATION_RATE: " + MUTATION_RATE);
        settings.println("CROSSOVER_RATE: " + CROSSOVER_RATE);
        settings.close();

        for (int run = 0; run < 10; run++) { // Run 10 times to give an average
            
            // Create a file for storing how the GA is doing
            File out = new File(dirPath + timeStamp + "\\" + "out" + run + ".csv"); // Desktop
            PrintWriter writer = new PrintWriter(out, "UTF-8");
            // Write in the column headers
            writer.println("Run #,Mean,Sum,Best Fitness,Best Gene");

            Population pop = new Population(POPULATION_SIZE, GENE_SIZE);
            pop.runFitnessAll();
            
            Individual generationBest = pop.getFittestIndividual();
            int i = 1;
            // Run until an individual matches the training data perfectly or we hit the max number of runs
            while ((generationBest.getFitness() != goal.length) && (i < NUMBER_OF_RUNS)) {
                Population parents = pop.selectParents();
                parents.combine();
                parents.mutatePopulation();
                parents.runFitnessAll();
                generationBest = parents.getFittestIndividual();
                
                // Log a record of this epoch
                System.out.println("Run #" + i + " Mean:\t" + parents.calculateFitnessMean() + " Sum:\t" + parents.calculateFitnessSum() + " Best: (" + generationBest.getFitness() + ")\t" + generationBest.displayGene());
                writer.println(i + "," + parents.calculateFitnessMean() + "," + parents.calculateFitnessSum() + "," + generationBest.getFitness() + ",[" + generationBest.displayGene() + "]");
                pop = parents;
                i++;
            }
            System.out.println("");
            writer.close();
            pop.clear();
            epochTaken[run] = i;
        }
        File res = new File(dirPath + timeStamp + "\\results.txt");
        PrintWriter r = new PrintWriter(res, "UTF-8");
        r.println("RUN RESULTS: ");
        int epochTakenSum = 0;
        for (int run : epochTaken) {
            r.println(run);
            epochTakenSum += run;
        }
        r.println();
        // Calculate the average of all the runs
        r.println("AVERAGE: " + (float) (epochTakenSum / 10));
        r.close();
                // Open the summary spreadsheet
        File allRuns = new File(dirPath + "summary.csv");
        FileWriter fw = new FileWriter(allRuns, true);
        fw.append("" + POPULATION_SIZE + "," + NUMBER_OF_RUNS + "," + MUTATION_RATE + "," + CROSSOVER_RATE + "," + (float) (epochTakenSum / 10) + "," + timeStamp + "\n");
        fw.close();

    } // main

    public static int calculateFitness(Individual ind) {
        int fitness = 0;
        for (int i = 0; i < ind.getGene().length; i++) {
            if (ind.getGene()[i] == goal[i]) {
                fitness++;
            }
        }
        return fitness;
    }
}
