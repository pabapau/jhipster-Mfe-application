package com.paba.mfe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.paba.mfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UseraccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Useraccount.class);
        Useraccount useraccount1 = new Useraccount();
        useraccount1.setId(1L);
        Useraccount useraccount2 = new Useraccount();
        useraccount2.setId(useraccount1.getId());
        assertThat(useraccount1).isEqualTo(useraccount2);
        useraccount2.setId(2L);
        assertThat(useraccount1).isNotEqualTo(useraccount2);
        useraccount1.setId(null);
        assertThat(useraccount1).isNotEqualTo(useraccount2);
    }
}
