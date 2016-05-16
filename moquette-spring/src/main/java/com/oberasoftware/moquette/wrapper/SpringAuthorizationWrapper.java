package com.oberasoftware.moquette.wrapper;

import io.moquette.spi.security.IAuthorizator;

/**
 * @author Renze de Vries
 */
public class SpringAuthorizationWrapper implements IAuthorizator {
    @Override
    public boolean canWrite(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean canRead(String s, String s1, String s2) {
        return false;
    }
}
