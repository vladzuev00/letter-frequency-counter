import counter.MultiThreadLetterFrequencyCounter;
import counter.SingleThreadLetterFrequencyCounter;

import static test.CounterTestUtil.testCounter;

public final class Main {

    public static void main(final String[] args) {
        testCounter(new SingleThreadLetterFrequencyCounter());
        testCounter(new MultiThreadLetterFrequencyCounter(5));
    }
}
