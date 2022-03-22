package com.paba.mfe.domain.enumeration;

/**
 * type on site determining\nrole of him in transfert usecase
 */
public enum Flowusecase {
    A2A("Application to same application"),
    A2B("Application to other application"),
    C2P("Application to external site"),
    P2C("External site to application"),
    A2U("Application to user "),
    U2A("User to application"),
    U2P("User to external site "),
    P2U("External site to user");

    private final String value;

    Flowusecase(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
