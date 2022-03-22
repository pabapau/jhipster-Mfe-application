package com.paba.mfe.domain.enumeration;

/**
 * The Accounttype enumeration.
 */
public enum Accounttype {
    TRF("Linked 2 site"),
    CONF("For mfe conf usage"),
    GUI("For consulting usage");

    private final String value;

    Accounttype(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
