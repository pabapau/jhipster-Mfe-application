package com.paba.mfe.domain.enumeration;

/**
 * The XProtType enumeration.
 */
public enum XProtType {
    PESITANY("pesit cft"),
    PESITSSL("pesit ssl cft"),
    SFTP("sftp");

    private final String value;

    XProtType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
