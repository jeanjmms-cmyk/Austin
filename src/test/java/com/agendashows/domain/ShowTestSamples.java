package com.agendashows.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ShowTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Show getShowSample1() {
        return new Show().id(1L).local("local1");
    }

    public static Show getShowSample2() {
        return new Show().id(2L).local("local2");
    }

    public static Show getShowRandomSampleGenerator() {
        return new Show().id(longCount.incrementAndGet()).local(UUID.randomUUID().toString());
    }
}
