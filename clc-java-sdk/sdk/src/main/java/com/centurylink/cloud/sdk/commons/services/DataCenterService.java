package com.centurylink.cloud.sdk.commons.services;

import com.centurylink.cloud.sdk.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.commons.client.domain.datacenters.DataCenterMetadata;
import com.centurylink.cloud.sdk.commons.services.domain.datacenters.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.commons.services.domain.datacenters.refs.DataCenter;
import com.google.inject.Inject;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.base.function.Streams.map;
import static com.centurylink.cloud.sdk.base.services.refs.References.exceptionIfNotFound;
import static com.google.common.collect.Iterables.getFirst;

/**
 * @author ilya.drabenia
 */
public class DataCenterService {
    private final DataCentersClient serverClient;

    @Inject
    public DataCenterService(DataCentersClient serverClient) {
        this.serverClient = serverClient;
    }

    public DataCenterMetadata findByRef(DataCenter dataCenterRef) {
        return exceptionIfNotFound(
            findFirst(dataCenterRef.asFilter())
        );
    }

    public DataCenterMetadata findFirst(DataCenterFilter criteria) {
        return getFirst(find(criteria.getPredicate()), null);
    }

    public List<DataCenterMetadata> find(DataCenterFilter criteria) {
        return find(criteria.getPredicate());
    }

    public Stream<DataCenterMetadata> findLazy(DataCenterFilter criteria) {
        return findLazy(criteria.getPredicate());
    }

    public List<DataCenterMetadata> find(Predicate<DataCenterMetadata> predicate) {
        return findLazy(predicate).collect(Collectors.toList());
    }

    public Stream<DataCenterMetadata> findLazy(Predicate<DataCenterMetadata> predicate) {
        return findAll().stream().filter(predicate);
    }

    public List<DataCenterMetadata> findAll() {
        return serverClient.findAllDataCenters();
    }
}