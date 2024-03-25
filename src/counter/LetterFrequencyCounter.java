package counter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

public abstract class LetterFrequencyCounter {
    private final int subtaskCount;

    public LetterFrequencyCounter(final int subtaskCount) {
        this.subtaskCount = subtaskCount;
    }

    public final Map<Character, Integer> count(final String input) {
        final Map<Character, Integer> accumulator = createAccumulator();
        final char[] chars = input.toCharArray();
        final Stream<LetterFrequencySubtask> subtasks = createSubtasks(accumulator, chars);
        execute(subtasks);
        return accumulator;
    }

    protected abstract Map<Character, Integer> createAccumulator();

    protected abstract void execute(final Stream<LetterFrequencySubtask> tasks);

    private Stream<LetterFrequencySubtask> createSubtasks(final Map<Character, Integer> accumulator, final char[] chars) {
        final int subtaskCharCount = chars.length / subtaskCount;
        return range(0, subtaskCount).mapToObj(i -> new LetterFrequencySubtask(accumulator, chars, i * subtaskCharCount, i * subtaskCount * subtaskCharCount - 1));
    }

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
            rangeClosed(start, end)
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
