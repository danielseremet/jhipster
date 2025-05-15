package y.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Sale getSaleSample1() {
        return new Sale().id(1L).quantity(1);
    }

    public static Sale getSaleSample2() {
        return new Sale().id(2L).quantity(2);
    }

    public static Sale getSaleRandomSampleGenerator() {
        return new Sale().id(longCount.incrementAndGet()).quantity(intCount.incrementAndGet());
    }
}
