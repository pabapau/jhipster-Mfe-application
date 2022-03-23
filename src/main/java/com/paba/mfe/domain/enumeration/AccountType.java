package com.paba.mfe.domain.enumeration;

/**
 * The AccountType enumeration.
 */
public enum AccountType {
    TRF("Linked 2 site"),
    CONF("For mfe conf usage"),
    GUI("For consulting usage");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
