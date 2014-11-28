/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GeneticAlgorithm;

/**
 *
 * @author Alex
 */
public class Datum {
    private int[] value;
    private int result;

    public Datum(int[] value, int result) {
        this.value = value;
        this.result = result;
    }

    public int[] getValue() {
        return value;
    }

    public void setValue(int[] value) {
        this.value = value;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
    
    public String display(){
        String out = "";
        for(int i : value){
            out = out + i;
        }
        out = out + " " + result;
        return out;
    }
    
}
