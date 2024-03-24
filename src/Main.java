import counter.LetterFrequencyCounter;
import counter.MultiThreadLetterFrequencyCounter;
import counter.SingleThreadLetterFrequencyCounter;

public final class Main {

    public static void main(final String[] args) {
        final String input = "Teextt";
        final LetterFrequencyCounter counter = new SingleThreadLetterFrequencyCounter();
        final LetterFrequencyCounter counter2 = new MultiThreadLetterFrequencyCounter(5);
        System.out.println(counter.count(input));
        System.out.println(counter2.count(input));
    }
}