package counter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class SingleThreadLetterFrequencyCounter extends LetterFrequencyCounter {

    public SingleThreadLetterFrequencyCounter() {
        super(1);
    }

    @Override
    protected Map<Character, Integer> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    protected void execute(final Stream<FrequencySubtask> tasks) {
        tasks.forEach(FrequencySubtask::run);
    }
}
