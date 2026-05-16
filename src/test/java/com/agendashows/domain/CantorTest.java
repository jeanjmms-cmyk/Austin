package com.agendashows.domain;

import static com.agendashows.domain.CantorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agendashows.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CantorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cantor.class);
        Cantor cantor1 = getCantorSample1();
        Cantor cantor2 = new Cantor();
        assertThat(cantor1).isNotEqualTo(cantor2);

        cantor2.setId(cantor1.getId());
        assertThat(cantor1).isEqualTo(cantor2);

        cantor2 = getCantorSample2();
        assertThat(cantor1).isNotEqualTo(cantor2);
    }
}
