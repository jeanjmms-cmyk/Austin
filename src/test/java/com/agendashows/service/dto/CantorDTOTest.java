package com.agendashows.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agendashows.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CantorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CantorDTO.class);
        CantorDTO cantorDTO1 = new CantorDTO();
        cantorDTO1.setId(1L);
        CantorDTO cantorDTO2 = new CantorDTO();
        assertThat(cantorDTO1).isNotEqualTo(cantorDTO2);
        cantorDTO2.setId(cantorDTO1.getId());
        assertThat(cantorDTO1).isEqualTo(cantorDTO2);
        cantorDTO2.setId(2L);
        assertThat(cantorDTO1).isNotEqualTo(cantorDTO2);
        cantorDTO1.setId(null);
        assertThat(cantorDTO1).isNotEqualTo(cantorDTO2);
    }
}
