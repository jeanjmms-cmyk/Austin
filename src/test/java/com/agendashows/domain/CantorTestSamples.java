package com.agendashows.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CantorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cantor getCantorSample1() {
        return new Cantor().id(1L).nome("nome1").generoMusical("generoMusical1").fotoPerfil("fotoPerfil1");
    }

    public static Cantor getCantorSample2() {
        return new Cantor().id(2L).nome("nome2").generoMusical("generoMusical2").fotoPerfil("fotoPerfil2");
    }

    public static Cantor getCantorRandomSampleGenerator() {
        return new Cantor()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .generoMusical(UUID.randomUUID().toString())
            .fotoPerfil(UUID.randomUUID().toString());
    }
}
