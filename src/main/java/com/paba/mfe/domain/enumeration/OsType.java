package com.paba.mfe.domain.enumeration;

/**
 * The OsType enumeration.
 */
public enum OsType {
    WIN("WINDOWS"),
    AIX,
    LNX("LINUX"),
    ZOS("Z_OS"),
    IBMI("IBMI5_ISERIES_AS400");

    private final String value;

    OsType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
