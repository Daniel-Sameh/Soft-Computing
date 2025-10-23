package fcai.sclibrary.ga.operators.replacement;

import fcai.sclibrary.ga.chromosome.Chromosome;
import fcai.sclibrary.ga.operators.replacement.functions.CutoffFunction;
import fcai.sclibrary.ga.operators.replacement.functions.MixingFunction;
import fcai.sclibrary.ga.operators.replacement.functions.ReplacementFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SteadyStateReplacement<T extends Object> implements Replacement<T> {

    private final ReplacementFunction<T> replacementFunction;

    public SteadyStateReplacement() {
        Random random = new Random();
        double val = random.nextDouble();
        if (val <= 0.5)
            replacementFunction = new CutoffFunction<>();
        else
            replacementFunction = new MixingFunction<>();
    }

    public SteadyStateReplacement(ReplacementFunction<T> replacementFunction) {
        this.replacementFunction = replacementFunction;
    }

    @Override
    public List<Chromosome<T>> replace(List<Chromosome<T>> parent, List<Chromosome<T>> new_ch, int k) {
        List<Chromosome<T>> res = new ArrayList<>();

        int rem = parent.size() - k;
        if (new_ch.size() < rem) {
            res.addAll(new_ch);
            rem -= new_ch.size();
            res.addAll(replacementFunction.select(parent, k + rem));
        }
        else {
            res.addAll(replacementFunction.select(parent, k));
            res.addAll(replacementFunction.select(new_ch, rem));
        }

        return res;
    }
}
