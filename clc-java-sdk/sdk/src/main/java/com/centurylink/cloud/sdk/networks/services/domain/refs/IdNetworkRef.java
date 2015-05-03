package com.centurylink.cloud.sdk.networks.services.domain.refs;

import com.centurylink.cloud.sdk.common.services.services.domain.datacenters.refs.DataCenter;

/**
 * @author ilya.drabenia
 */
public class IdNetworkRef extends NetworkRef {
    private final String id;

    public IdNetworkRef(DataCenter dataCenter, String id) {
        super(dataCenter);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
