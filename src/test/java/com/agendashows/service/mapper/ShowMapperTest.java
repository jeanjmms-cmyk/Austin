package com.agendashows.service.mapper;

import static com.agendashows.domain.ShowAsserts.*;
import static com.agendashows.domain.ShowTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShowMapperTest {

    private ShowMapper showMapper;

    @BeforeEach
    void setUp() {
        showMapper = new ShowMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShowSample1();
        var actual = showMapper.toEntity(showMapper.toDto(expected));
        assertShowAllPropertiesEquals(expected, actual);
    }
}
