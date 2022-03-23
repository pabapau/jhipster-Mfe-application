package com.paba.mfe.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.paba.mfe.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileDescriptorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileDescriptor.class);
        FileDescriptor fileDescriptor1 = new FileDescriptor();
        fileDescriptor1.setId(1L);
        FileDescriptor fileDescriptor2 = new FileDescriptor();
        fileDescriptor2.setId(fileDescriptor1.getId());
        assertThat(fileDescriptor1).isEqualTo(fileDescriptor2);
        fileDescriptor2.setId(2L);
        assertThat(fileDescriptor1).isNotEqualTo(fileDescriptor2);
        fileDescriptor1.setId(null);
        assertThat(fileDescriptor1).isNotEqualTo(fileDescriptor2);
    }
}
