package com.paba.mfe.domain.enumeration;

/**
 * type on site determining\nrole of him in transfert usecase
 */
public enum SiteType {
    INT("Application site"),
    EXT("External site"),
    USR("Site for users usage"),
    NODE("Site for monitor");

    private final String value;

    SiteType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
