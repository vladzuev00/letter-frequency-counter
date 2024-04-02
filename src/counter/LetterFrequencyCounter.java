package counter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public abstract class LetterFrequencyCounter {

    public final Map<Character, Integer> count(final String input) {
        final Map<Character, Integer> accumulator = createAccumulator();
        final Stream<LetterFrequencySubtask> subtasks = createSubtasks(accumulator, input.toCharArray());
        execute(subtasks);
        return accumulator;
    }

    protected abstract Map<Character, Integer> createAccumulator();

    protected abstract Stream<LetterFrequencySubtask> createSubtasks(final Map<Character, Integer> accumulator,
                                                                     final char[] chars);

    protected abstract void execute(final Stream<LetterFrequencySubtask> subtasks);

    protected static final class LetterFrequencySubtask {
        private final Map<Character, Integer> accumulator;
        private final char[] chars;
        private final int start;
        private final int end;

        public LetterFrequencySubtask(final Map<Character, Integer> accumulator,
                                      final char[] chars,
                                      final int start,
                                      final int end) {
            this.accumulator = accumulator;
            this.chars = chars;
            this.start = start;
            this.end = end;
        }

        public void execute() {
            range(start, end)
                    .map(i -> chars[i])
                    .filter(Character::isLetter)
                    .map(Character::toLowerCase)
                    .forEach(this::accumulate);
        }

        private void accumulate(final int codePoint) {
            accumulator.merge((char) codePoint, 1, Integer::sum);
        }
    }
}
