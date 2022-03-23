package com.paba.mfe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.paba.mfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class XProtTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(XProt.class);
        XProt xProt1 = new XProt();
        xProt1.setId(1L);
        XProt xProt2 = new XProt();
        xProt2.setId(xProt1.getId());
        assertThat(xProt1).isEqualTo(xProt2);
        xProt2.setId(2L);
        assertThat(xProt1).isNotEqualTo(xProt2);
        xProt1.setId(null);
        assertThat(xProt1).isNotEqualTo(xProt2);
    }
}
