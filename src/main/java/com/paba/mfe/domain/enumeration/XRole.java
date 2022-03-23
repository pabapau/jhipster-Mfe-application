package com.paba.mfe.domain.enumeration;

/**
 * The XRole enumeration.
 */
public enum XRole {
    CLI("client"),
    SRV("serveur");

    private final String value;

    XRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
