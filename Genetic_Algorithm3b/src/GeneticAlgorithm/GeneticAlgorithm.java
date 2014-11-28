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
    public static final int NUMBER_OF_RULES = 9;
    public static final int DATA_LENGTH = 6;
    public static final int NUMBER_OF_RUNS = 1000;
    public static final int TOURNAMENT_SIZE = 2;
    public static final double MUTATION_RATE = 0.015;
    public static final double CROSSOVER_RATE = 0.9;
    public static final int GENE_SIZE = NUMBER_OF_RULES * (DATA_LENGTH + 1);
    public static final double TEST_PERCENTAGE = 0;
    public static final int DATA_SET = 1;
    public static ArrayList<Datum> trainingData;
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
        ArrayList<Datum> testData = new ArrayList<>();
        String dirPath = "D:\\Dropbox\\Work\\Year 3\\AI\\Code\\Genetic_Algorithm3b\\data\\";
        // Setup scanner on trainingData file
        //File in = new File("C:\\Users\\Alex\\Dropbox\\Work\\Year 3\\AI\\Code\\Genetic Algorithm\\data\\data2.txt"); // Laptop
        File in = new File(dirPath + "data" + DATA_SET + ".txt"); // Desktop
        //File in = new File("F:\\Code\\Genetic_Algorithm2\\data\\\\data1.txt"); // USB Drive

        Scanner scan = new Scanner(in);
        scan.nextLine(); // Ignore the first line

        // Read in the data from the file
        while (scan.hasNext()) {
            String line = scan.nextLine();
            //int split = line.indexOf(' ');
            int split = DATA_LENGTH;
            int[] tmpValue = new int[split];
            for (int i = 0; i < split; i++) {
                tmpValue[i] = Character.getNumericValue(line.charAt(i));
            }
            trainingData.add(new Datum(tmpValue, Character.getNumericValue(line.charAt(split + 1))));
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

        for (int run = 0; run < 10; run++) { // Run 10 times to give an average
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
            if (!testData.isEmpty()) {
                int fitness = 0;
                ArrayList<Datum> rules = geneToRules(generationBest.getGene());
                for (Datum d : testData) {
                    writer.println(d.display());
                    for (Datum rule : rules) {
                        boolean ruleFits = true;
                        for (int j = 0; j < d.getValue().length; j++) {
                            if ((rule.getValue()[j] != 2) && (rule.getValue()[j]) != d.getValue()[j]) {
                                ruleFits = false;
                                break;
                            }
                        }
                        if (ruleFits) {
                            if (d.getResult() == rule.getResult()) {
                                // Rule & expected outcome match, increase fitness
                                fitness++;
                            }
                            break;
                        }
                    }
                }
                if (DATA_SET == 1) {
                    System.out.println("");
                } else {
                    // Calculate the percentage the GA got right
                    double testPercentage = ((float) fitness / (float) testData.size()) * 100;
                    System.out.println("Final Gen Best Test results");
                    System.out.println("Fitness: " + fitness + " (" + testPercentage + "%)");
                    writer.println("RESULT:," + fitness + "," + testPercentage);
                    fitnessPercentages[run] = testPercentage;
                }
            } else {
                System.out.println("No Test Data");
            }
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
        if (DATA_SET == 1) {
            for (int run : epochTaken) {
                r.println(run);
                epochTakenSum += run;
            }
            r.println();
            r.print("AVERAGE: " + (float) (epochTakenSum / 10));
        } else {
            for (double result : fitnessPercentages) {
                r.println(result);
                fitnessSum += result;
            }
            r.println();
            // Calculate the average of all the runs
            r.println("AVERAGE: " + (fitnessSum / 10));
        }

        r.close();
        // Open the summary spreadsheet
        File allRuns = new File(dirPath + "summary" + DATA_SET + ".csv");
        FileWriter fw = new FileWriter(allRuns, true);
        if (DATA_SET == 1) {
            fw.append("" + POPULATION_SIZE + "," + NUMBER_OF_RULES + "," + NUMBER_OF_RUNS + "," + TOURNAMENT_SIZE + "," + MUTATION_RATE + "," + CROSSOVER_RATE + "," + (float) (epochTakenSum / 10) + "," + timeStamp + "\n");
        } else {
            fw.append("" + POPULATION_SIZE + "," + NUMBER_OF_RULES + "," + NUMBER_OF_RUNS + "," + TOURNAMENT_SIZE + "," + MUTATION_RATE + "," + CROSSOVER_RATE + "," + TEST_PERCENTAGE + "," + (fitnessSum / 10) + "," + timeStamp + "\n");
        }
        fw.close();
    } // main

    // Used to split the int array representing the gene into "Datum" representation for easier calculation
    public static ArrayList<Datum> geneToRules(int[] gene) {
        ArrayList<Datum> rules = new ArrayList<>();
        int ruleLength = (gene.length / NUMBER_OF_RULES) - 1;
        int[] tmpValue = new int[ruleLength];
        int count = 0;
        for (int i = 0; i < gene.length; i++) {
            if (count < (ruleLength)) {
                tmpValue[count] = gene[i];
                count++;
            } else {
                count = 0;
                rules.add(new Datum(tmpValue, gene[i]));
                tmpValue = new int[ruleLength];
            }
        }
        return rules;
    }

    public static int calculateFitness(Individual ind) {
        int fitness = 0;
        ArrayList<Datum> rules = geneToRules(ind.getGene());
        if (DEBUG) {
            System.out.println("===========================");
            System.out.println(ind.displayGene());
        }
        for (Datum d : trainingData) {
            if (DEBUG) {
                System.out.println("TEST: " + d.display());
            }
            for (Datum rule : rules) {
                if (DEBUG) {
                    System.out.print("RULE: " + rule.display());
                }
                boolean ruleFits = true;
                for (int i = 0; i < d.getValue().length; i++) {
                    if ((rule.getValue()[i] != 2) && (rule.getValue()[i]) != d.getValue()[i]) {
                        ruleFits = false;
                        if (DEBUG) {
                            System.out.println(" NO MATCH (POS " + i + ")");
                        }
                        break;
                    }
                }
                if (ruleFits) {
                    if (d.getResult() == rule.getResult()) {
                        // Rule & expected outcome match, increase fitness
                        fitness++;
                        if (DEBUG) {
                            System.out.println(" MATCH");
                        }
                    } else {
                        if (DEBUG) {
                            System.out.println(" NO MATCH (RES)");
                        }
                    }
                    // We've found a matching rule, stop looking (this should stop all 2's from dominating)
                    break;
                }
            }
        }
        return fitness;
    }
}
