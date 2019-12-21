package project.ElementsOnMap;
import java.util.*;

public class Genotype {
    private int[] genotype;
    private int genotypeSize=32;
    private int numberOfGenes=8;

    public Genotype(){
        this.genotype = new int[this.genotypeSize];
        createGenotype();
        validGenotype();
    }

    public int[] getGenotype(){
        return this.genotype;
    }

    public Genotype(Genotype newGenotype) {
        this();
        this.genotype = Arrays.copyOf(newGenotype.getGenotype(), genotypeSize);
    }

    public Genotype(Genotype firstParent, Genotype secondParent) {
        this();
        int firstCut = (int) (Math.random() * (this.genotypeSize/3 - 1));
        int secondCut = (int)(firstCut+this.genotypeSize/3);

        for (int i = 0; i <= firstCut; i++)
            this.genotype[i] = firstParent.genotype[i];

        for (int i = firstCut + 1; i <= secondCut; i++)
            this.genotype[i] = secondParent.genotype[i];

        for (int i = secondCut; i < genotypeSize; i++)
            this.genotype[i] = firstParent.genotype[i];

        validGenotype();
    }

    private void createGenotype(){
        for(int i = 0; i < this.genotypeSize; i++){
            this.genotype[i] = (int)(Math.random() * this.numberOfGenes);
        }
        Arrays.sort(this.genotype);
    }

    private void validGenotype() {
        int[] genesCount = new int[this.numberOfGenes];
        for (int i = 0; i < this.numberOfGenes; i++)
            genesCount[i] = 0;

        for (int i = 0; i < this.genotypeSize; i++) {
            genesCount[this.genotype[i]]++;
        }

        for (int i = 0; i < this.numberOfGenes; i++) {
            if (genesCount[i] == 0) {
                for (int j = 0; j < this.genotypeSize; j++) {
                    if (genesCount[this.genotype[j]] > 1) {
                        genesCount[i] += 1;
                        genesCount[j] -= 1;
                        this.genotype[j] = i;
                        break;
                    }
                }
            }
        }
        Arrays.sort(genotype);
    }

    public int returnRandomGen() {
        int rand = (int) (Math.random() * (genotypeSize));
        return genotype[rand];
    }
}