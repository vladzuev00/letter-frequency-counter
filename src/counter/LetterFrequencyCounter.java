package counter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;
import static java.util.stream.IntStream.rangeClosed;

public abstract class LetterFrequencyCounter {
    private static final String NOT_LETTER_REGEX = "[^a-zA-Z]";

    private final int threadCount;

    public LetterFrequencyCounter(final int threadCount) {
        this.threadCount = threadCount;
    }

    public final Map<Character, Integer> count(final String input) {
        final Map<Character, Integer> accumulator = createAccumulator();
        final char[] chars = getLettersInLowerCase(input);
        execute(createSubtasks(accumulator, chars));
        return accumulator;
    }

    protected abstract Map<Character, Integer> createAccumulator();

    protected abstract void execute(final Stream<FrequencySubtask> tasks);

    private char[] getLettersInLowerCase(final String input) {
        return input.replaceAll(NOT_LETTER_REGEX, "")
                .toLowerCase()
                .toCharArray();
    }

    private Stream<FrequencySubtask> createSubtasks(final Map<Character, Integer> accumulator, final char[] chars) {
        final int subtaskCharCount = chars.length / threadCount;
        return range(0, threadCount).mapToObj(i -> new FrequencySubtask(accumulator, chars, i * subtaskCharCount, i * threadCount * subtaskCharCount - 1));
    }

    protected static final class FrequencySubtask implements Runnable {
        private final Map<Character, Integer> accumulator;
        private final char[] chars;
        private final int start;
        private final int end;

        public FrequencySubtask(final Map<Character, Integer> accumulator,
                                final char[] chars,
                                final int start,
                                final int end) {
            this.accumulator = accumulator;
            this.chars = chars;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            rangeClosed(start, end).forEach(this::accumulate);
        }

        private void accumulate(final int index) {
            accumulator.merge(chars[index], 1, Integer::sum);
        }
    }
}
