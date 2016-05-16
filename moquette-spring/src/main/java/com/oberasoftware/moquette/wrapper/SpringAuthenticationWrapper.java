package com.oberasoftware.moquette.wrapper;

import io.moquette.spi.security.IAuthenticator;

/**
 * @author Renze de Vries
 */
public class SpringAuthenticationWrapper implements IAuthenticator {
    @Override
    public boolean checkValid(String s, byte[] bytes) {
        return false;
    }
}
