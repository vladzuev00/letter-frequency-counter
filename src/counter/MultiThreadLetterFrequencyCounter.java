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
    protected Stream<CountingSubtask> createSubtasks(final Map<Character, Integer> accumulator,
                                                     final char[] chars) {
        final int subtaskCharCount = findSubtaskCharCount(chars);
        return range(0, subtaskCount).mapToObj(i -> createSubtask(accumulator, chars, subtaskCharCount, i));
    }

    @Override
    protected void execute(final Stream<CountingSubtask> subtasks) {
        final List<Thread> threads = run(subtasks);
        waitUntilFinish(threads);
    }

    private int findSubtaskCharCount(final char[] chars) {
        return (int) ceil((double) chars.length / subtaskCount);
    }

    private static CountingSubtask createSubtask(final Map<Character, Integer> accumulator,
                                                 final char[] chars,
                                                 final int charCount,
                                                 final int index) {
        final int start = index * charCount;
        final int end = min((index + 1) * charCount, chars.length);
        return new CountingSubtask(accumulator, chars, start, end);
    }

    private List<Thread> run(final Stream<CountingSubtask> subtasks) {
        return subtasks.map(this::run).toList();
    }

    private Thread run(final CountingSubtask subtask) {
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
