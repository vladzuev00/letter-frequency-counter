package counter;

import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static java.util.stream.IntStream.range;

public abstract class LetterFrequencyCounter {
    private final int subtaskCount;

    public LetterFrequencyCounter(final int subtaskCount) {
        this.subtaskCount = subtaskCount;
    }

    public final Map<Character, Integer> count(final String input) {
        final Map<Character, Integer> accumulator = createAccumulator();
        final Stream<LetterFrequencySubtask> subtasks = createSubtasks(accumulator, input);
        execute(subtasks);
        return accumulator;
    }

    protected abstract Map<Character, Integer> createAccumulator();

    protected abstract void execute(final Stream<LetterFrequencySubtask> subtasks);

    private Stream<LetterFrequencySubtask> createSubtasks(final Map<Character, Integer> accumulator, final String input) {
        final char[] chars = input.toCharArray();
        final int subtaskCharCount = (int) Math.ceil((double) chars.length / (double) subtaskCount);
        return range(0, subtaskCount).mapToObj(i -> createSubtask(accumulator, chars, subtaskCharCount, i));
    }

    private static LetterFrequencySubtask createSubtask(final Map<Character, Integer> accumulator,
                                                        final char[] chars,
                                                        final int subtaskCharCount,
                                                        final int index) {
        final int start = index * subtaskCharCount;
        final int end = min((index + 1) * subtaskCharCount, chars.length);
        return new LetterFrequencySubtask(accumulator, chars, start, end);
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
