package counter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public final class MultiThreadLetterFrequencyCounter extends LetterFrequencyCounter {


    public MultiThreadLetterFrequencyCounter(final int taskCharCount) {
        super(taskCharCount);
    }

    @Override
    protected Map<Character, Integer> createAccumulator() {
        return new ConcurrentHashMap<>();
    }

    @Override
    protected void execute(final Stream<FrequencySubtask> tasks) {
        List<Thread> threads = tasks.map(Thread::new).toList();
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
