package com.paba.mfe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.paba.mfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BusinessunitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Businessunit.class);
        Businessunit businessunit1 = new Businessunit();
        businessunit1.setId(1L);
        Businessunit businessunit2 = new Businessunit();
        businessunit2.setId(businessunit1.getId());
        assertThat(businessunit1).isEqualTo(businessunit2);
        businessunit2.setId(2L);
        assertThat(businessunit1).isNotEqualTo(businessunit2);
        businessunit1.setId(null);
        assertThat(businessunit1).isNotEqualTo(businessunit2);
    }
}
