package com.paba.mfe.domain.enumeration;

/**
 * The Xrole enumeration.
 */
public enum Xrole {
    CLI("client"),
    SRV("serveur");

    private final String value;

    Xrole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
