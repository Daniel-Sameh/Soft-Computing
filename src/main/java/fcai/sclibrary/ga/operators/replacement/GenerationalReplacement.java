package fcai.sclibrary.ga.operators.replacement;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.operators.replacement.functions.CutoffFunction;
import fcai.sclibrary.ga.operators.replacement.functions.MixingFunction;
import fcai.sclibrary.ga.operators.replacement.functions.ReplacementFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerationalReplacement<T extends Number> implements Replacement<T> {

    private final ReplacementFunction<T> replacementFunction;

    public GenerationalReplacement() {
        Random random = new Random();
        double val = random.nextDouble();
        if (val <= 0.5)
            replacementFunction = new CutoffFunction<>();
        else
            replacementFunction = new MixingFunction<>();
    }

    public GenerationalReplacement(ReplacementFunction<T> replacementFunction) {
        this.replacementFunction = replacementFunction;
    }

    @Override
    public List<Chromosome<T>> replace(List<Chromosome<T>> parent, List<Chromosome<T>> new_ch, int k) {
        List<Chromosome<T>> res = new ArrayList<>();
        if (new_ch.size() < parent.size()) {
            res.addAll(new_ch);
            res.addAll(replacementFunction.select(parent, parent.size() - new_ch.size()));
        }
        else
            res.addAll(replacementFunction.select(new_ch, parent.size()));
        return res;
    }
}
