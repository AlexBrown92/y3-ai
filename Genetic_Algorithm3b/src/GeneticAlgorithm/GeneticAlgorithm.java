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

    public static final int POPULATION_SIZE = 26;
    public static final int NUMBER_OF_RULES = 10;
    public static final int DATA_LENGTH = 6;
    public static final int NUMBER_OF_RUNS = 1000;
    public static final int TOURNAMENT_SIZE = 2;
    public static final double MUTATION_RATE = 0.015;
    public static final double MAX_MUTATION = 0.1;
    public static final double CROSSOVER_RATE = 0.9;
    public static final int GENE_SIZE = NUMBER_OF_RULES * ((DATA_LENGTH*2) + 1);
    public static final double TEST_PERCENTAGE = 0;
    public static final int DATA_SET = 3;
    public static ArrayList<Input> trainingData;
    public static final boolean DEBUG = false;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        rn = new Random();

        int[] epochTaken = new int[10];
        double[] fitnessPercentages = new double[10];

        trainingData = new ArrayList<>();
        ArrayList<Input> testData = new ArrayList<>();
        //String dirPath = "D:\\Dropbox\\Work\\Year 3\\AI\\Code\\Genetic_Algorithm3\\data\\";
        String dirPath = "C:\\Users\\Alex\\Dropbox\\Work\\Year 3\\AI\\Code\\Genetic_Algorithm3b\\data\\";
        // Setup scanner on trainingData file
        //File in = new File("C:\\Users\\Alex\\Dropbox\\Work\\Year 3\\AI\\Code\\Genetic Algorithm\\data\\data2.txt"); // Laptop
        File in = new File(dirPath + "data" + DATA_SET + ".txt"); // Desktop
        //File in = new File("F:\\Code\\Genetic_Algorithm2\\data\\\\data1.txt"); // USB Drive

        Scanner scan = new Scanner(in);
        scan.nextLine(); // Ignore the first line

        // Read in the data from the file
        while (scan.hasNext()) {
            String line = scan.nextLine();
            trainingData.add(new Input(line));
        }

        // Calculate the number of rows from the file to be used in testing. 
        int trainingNumber = (int) Math.round(trainingData.size() * TEST_PERCENTAGE);

        // Timestamp for use in the directory name to ensure that it's unique
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        // Create a directory to hold all the data from the runs.
        File dir = new File(dirPath + timeStamp + "\\");
        if (!dir.exists()) {
            dir.mkdir();
        }
        // Create a settings file and enter run settings values
        File s = new File(dirPath + timeStamp + "\\settings.txt");
        PrintWriter settings = new PrintWriter(s, "UTF-8");
        settings.println("POPULATION_SIZE: " + POPULATION_SIZE);
        settings.println("NUMBER_OF_RULES: " + NUMBER_OF_RULES);
        settings.println("NUMBER_OF_RUNS: " + NUMBER_OF_RUNS);
        settings.println("TOURNAMENT_SIZE: " + TOURNAMENT_SIZE);
        settings.println("MUTATION_RATE: " + MUTATION_RATE);
        settings.println("CROSSOVER_RATE: " + CROSSOVER_RATE);
        settings.println("TEST_PERCENTAGE: " + TEST_PERCENTAGE);
        settings.close();

        for (int run = 0; run < 2; run++) { // Run 10 times to give an average
            // Split off some data for testing later.
            for (int i = 0; i < trainingNumber; i++) {
                testData.add(trainingData.remove(rn.nextInt(trainingData.size())));
            }
            // Create a file for storing how the GA is doing
            File out = new File(dirPath + timeStamp + "\\" + "out" + run + ".csv"); // Desktop
            PrintWriter writer = new PrintWriter(out, "UTF-8");
            // Write in the column headers
            writer.println("Run #,Mean,Sum,Best Fitness,Best Gene");

            // Create a new random population
            Population pop = new Population(POPULATION_SIZE, GENE_SIZE);
            pop.runFitnessAll();

            Individual generationBest = pop.getFittestIndividual();
            int i = 1;

            // Run until an individual matches the training data perfectly or we hit the max number of runs
            while ((generationBest.getFitness() != trainingData.size()) && (i < NUMBER_OF_RUNS)) {
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

            epochTaken[run] = i;

            System.out.println("END OF TRAINING");
            writer.println();
            writer.println();
            writer.println();
            writer.println("TEST DATA:");
            // Test final generation best (This is essentially the fitness function)
            /*
            if (!testData.isEmpty()) {
                int fitness = 0;
                for (Input d : testData) {
                    NeuralNetwork nn = new NeuralNetwork(d.getInputs(), generationBest.getGeneArrayList(), NUMBER_OF_HIDDEN_NODES);
                    if (d.getExpected() == Math.round(nn.getOutputNode().getOutput())) {
                        fitness++;
                    }
                }

                // Calculate the percentage the GA got right
                double testPercentage = ((float) fitness / (float) testData.size()) * 100;
                System.out.println("Final Gen Best Test results");
                System.out.println("Fitness: " + fitness + " (" + testPercentage + "%)");
                writer.println("RESULT:," + fitness + "," + testPercentage);
                fitnessPercentages[run] = testPercentage;
            } else {
                System.out.println("No Test Data");
            }
            */
            writer.close();
            while (!testData.isEmpty()) {
                trainingData.add(testData.remove(0));
            }
        }
        // Create a file for storing a record of the run's results
        File res = new File(dirPath + timeStamp + "\\results.txt");
        PrintWriter r = new PrintWriter(res, "UTF-8");
        r.println("RUN RESULTS: ");
        double fitnessSum = 0;
        int epochTakenSum = 0;
        r.println("FITNESS PERCENTAGE");
        for (double result : fitnessPercentages) {
            r.println(result);
            fitnessSum += result;
        }
        r.println();
        // Calculate the average of all the runs
        r.println("AVERAGE: " + (fitnessSum / 10));
        r.println("EPOCHS TAKEN");
        for (int run : epochTaken) {
            r.println(run);
            epochTakenSum += run;
        }
        r.println();
        r.print("AVERAGE: " + (float) (epochTakenSum / 10));

        r.close();
        // Open the summary spreadsheet
        File allRuns = new File(dirPath + "summary" + DATA_SET + ".csv");
        FileWriter fw = new FileWriter(allRuns, true);
        fw.append("" + POPULATION_SIZE + "," + NUMBER_OF_RULES + "," + NUMBER_OF_RUNS + "," + TOURNAMENT_SIZE + "," + MUTATION_RATE + "," + CROSSOVER_RATE + "," + TEST_PERCENTAGE + "," + (fitnessSum / 10) + "," + timeStamp + "\n");
        fw.close();
    } // main

    public static int calculateFitness(Individual ind) {
        int fitness = 0;
        if (DEBUG) {
            System.out.println("===========================");
            System.out.println(ind.displayGene());
        }
        for (Input d : trainingData) {
            if (DEBUG) {
                System.out.println("TEST: " + d.display());
            }
            /*
            NeuralNetwork nn = new NeuralNetwork(d.getInputs(), ind.getGeneArrayList(), NUMBER_OF_HIDDEN_NODES);
            if (d.getExpected() == Math.round(nn.getOutputNode().getOutput())) {
                if (DEBUG){
                    System.out.println("MATCHED");
                }
                fitness++;
            }
            nn = null;*/
        }
        return fitness;
    }
}
