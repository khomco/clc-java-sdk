package com.centurylink.cloud.sdk.core.datacenters.services.domain.refs;

import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;

/**
 * {@inheritDoc}
 *
 * @author ilya.drabenia
 */
public abstract class DataCenterRef implements Reference {

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract DataCenterFilter asFilter();

}
