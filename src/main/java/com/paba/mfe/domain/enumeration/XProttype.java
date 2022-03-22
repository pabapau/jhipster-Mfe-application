package com.paba.mfe.domain.enumeration;

/**
 * The XProttype enumeration.
 */
public enum XProttype {
    PESITANY("pesit cft"),
    PESITSSL("pesit ssl cft"),
    SFTP("sftp");

    private final String value;

    XProttype(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
