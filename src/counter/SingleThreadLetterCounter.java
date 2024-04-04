package counter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public final class SingleThreadLetterCounter extends LetterCounter {

    @Override
    protected Map<Character, Integer> createAccumulator() {
        return new HashMap<>();
    }

    @Override
    protected Stream<Subtask> createSubtasks(final Map<Character, Integer> accumulator, final char[] chars) {
        return Stream.of(new Subtask(accumulator, chars, 0, chars.length));
    }

    @Override
    protected void execute(final Stream<Subtask> subtasks) {
        subtasks.forEach(Subtask::execute);
    }
}
