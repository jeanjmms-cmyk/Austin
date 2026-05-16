package com.agendashows.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.agendashows.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShowDTO.class);
        ShowDTO showDTO1 = new ShowDTO();
        showDTO1.setId(1L);
        ShowDTO showDTO2 = new ShowDTO();
        assertThat(showDTO1).isNotEqualTo(showDTO2);
        showDTO2.setId(showDTO1.getId());
        assertThat(showDTO1).isEqualTo(showDTO2);
        showDTO2.setId(2L);
        assertThat(showDTO1).isNotEqualTo(showDTO2);
        showDTO1.setId(null);
        assertThat(showDTO1).isNotEqualTo(showDTO2);
    }
}
