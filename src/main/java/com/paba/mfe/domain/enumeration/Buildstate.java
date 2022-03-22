package com.paba.mfe.domain.enumeration;

/**
 * state to determine the build &#39;deployement&#39;\nstatus off some components (sites,flows,...)
 */
public enum Buildstate {
    NOTBUILD,
    REQUIRED,
    BUILDING,
    ERROR,
    BUILDED,
    TODESTROY,
    DESTROYED,
}
