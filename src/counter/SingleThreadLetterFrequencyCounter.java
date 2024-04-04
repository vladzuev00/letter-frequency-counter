package counter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class SingleThreadLetterFrequencyCounter extends LetterFrequencyCounter {

    @Override
    protected Map<Character, Integer> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    protected Stream<CountingSubtask> createSubtasks(final Map<Character, Integer> accumulator, final char[] chars) {
        return Stream.of(new CountingSubtask(accumulator, chars, 0, chars.length));
    }

    @Override
    protected void execute(final Stream<CountingSubtask> subtasks) {
        subtasks.forEach(CountingSubtask::execute);
    }
}
