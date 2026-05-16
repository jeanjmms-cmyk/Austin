package com.agendashows.service.mapper;

import static com.agendashows.domain.CantorAsserts.*;
import static com.agendashows.domain.CantorTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CantorMapperTest {

    private CantorMapper cantorMapper;

    @BeforeEach
    void setUp() {
        cantorMapper = new CantorMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCantorSample1();
        var actual = cantorMapper.toEntity(cantorMapper.toDto(expected));
        assertCantorAllPropertiesEquals(expected, actual);
    }
}
