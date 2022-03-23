package com.paba.mfe.domain.enumeration;

/**
 * state to determine the build &#39;deployement&#39;\nstatus off some CONFigurations components (sites,flows,...)
 */
public enum BuildState {
    NOTBUILD,
    REQUIRED,
    BUILDING,
    BUILDERROR,
    BUILDED,
    TODESTROY,
    DESTROYED,
}
