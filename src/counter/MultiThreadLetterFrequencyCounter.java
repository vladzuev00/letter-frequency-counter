package counter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.lang.Math.ceil;
import static java.lang.Math.min;
import static java.lang.Thread.currentThread;
import static java.util.stream.IntStream.range;

public final class MultiThreadLetterFrequencyCounter extends LetterFrequencyCounter {
    private final int subtaskCount;

    public MultiThreadLetterFrequencyCounter(final int subtaskCount) {
        this.subtaskCount = subtaskCount;
    }

    @Override
    protected Map<Character, Integer> createAccumulator() {
        return new ConcurrentHashMap<>();
    }

    @Override
    protected Stream<LetterFrequencySubtask> createSubtasks(final Map<Character, Integer> accumulator,
                                                            final char[] chars) {
        final int subtaskCharCount = findSubtaskCharCount(chars);
        return range(0, subtaskCount).mapToObj(i -> createSubtask(accumulator, chars, subtaskCharCount, i));
    }

    @Override
    protected void execute(final Stream<LetterFrequencySubtask> subtasks) {
        final List<Thread> threads = run(subtasks);
        waitUntilFinish(threads);
    }

    private int findSubtaskCharCount(final char[] chars) {
        return (int) ceil((double) chars.length / subtaskCount);
    }

    private static LetterFrequencySubtask createSubtask(final Map<Character, Integer> accumulator,
                                                        final char[] chars,
                                                        final int subtaskCharCount,
                                                        final int subtaskIndex) {
        final int start = subtaskIndex * subtaskCharCount;
        final int end = min((subtaskIndex + 1) * subtaskCharCount, chars.length);
        return new LetterFrequencySubtask(accumulator, chars, start, end);
    }

    private List<Thread> run(final Stream<LetterFrequencySubtask> subtasks) {
        return subtasks.map(this::run).toList();
    }

    private Thread run(final LetterFrequencySubtask subtask) {
        final Thread thread = new Thread(subtask::execute);
        thread.start();
        return thread;
    }

    private void waitUntilFinish(final List<Thread> threads) {
        threads.forEach(this::waitUntilFinish);
    }

    private void waitUntilFinish(final Thread thread) {
        try {
            thread.join();
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
        }
    }
}
