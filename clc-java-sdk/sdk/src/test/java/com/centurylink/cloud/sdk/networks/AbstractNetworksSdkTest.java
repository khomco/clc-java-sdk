package com.centurylink.cloud.sdk.networks;

import com.centurylink.cloud.sdk.core.AbstractSdkTest;
import com.centurylink.cloud.sdk.core.auth.AuthModule;
import com.google.inject.Module;

import java.util.List;

/**
 * @author ilya.drabenia
 */
public class AbstractNetworksSdkTest extends AbstractSdkTest {

    @Override
    protected List<Module> modules() {
        return list(new AuthModule(), new NetworksModule());
    }

}