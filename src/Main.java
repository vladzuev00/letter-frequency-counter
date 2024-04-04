import counter.MultiThreadLetterCounter;
import counter.SingleThreadLetterCounter;

import static test.CounterTestUtil.test;

public final class Main {

    public static void main(final String[] args) {
        test(new SingleThreadLetterCounter());
        test(new MultiThreadLetterCounter(5));
    }
}
