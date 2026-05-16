package com.agendashows.domain;

import static com.agendashows.domain.CantorTestSamples.*;
import static com.agendashows.domain.ShowTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.agendashows.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Show.class);
        Show show1 = getShowSample1();
        Show show2 = new Show();
        assertThat(show1).isNotEqualTo(show2);

        show2.setId(show1.getId());
        assertThat(show1).isEqualTo(show2);

        show2 = getShowSample2();
        assertThat(show1).isNotEqualTo(show2);
    }

    @Test
    void cantorTest() {
        Show show = getShowRandomSampleGenerator();
        Cantor cantorBack = getCantorRandomSampleGenerator();

        show.setCantor(cantorBack);
        assertThat(show.getCantor()).isEqualTo(cantorBack);

        show.cantor(null);
        assertThat(show.getCantor()).isNull();
    }
}
