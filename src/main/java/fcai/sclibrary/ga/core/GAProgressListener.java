package fcai.sclibrary.ga.core;

import fcai.sclibrary.ga.chromosome.Chromosome;

import java.util.List;

public interface GAProgressListener {
    void onGenerationComplete(int generationNumber, Chromosome<?> bestChromosome);
    void onComplete(Chromosome<?> chromosome);
}
