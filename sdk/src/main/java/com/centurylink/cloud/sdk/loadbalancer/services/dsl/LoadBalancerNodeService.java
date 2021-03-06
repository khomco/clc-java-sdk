/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.loadbalancer.services.dsl;

import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.base.services.dsl.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.loadbalancer.services.client.LoadBalancerNodeClient;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerNodeMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerPoolMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.filter.LoadBalancerNodeFilter;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.node.LoadBalancerNode;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.pool.LoadBalancerPool;

import java.util.List;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.preconditions.Preconditions.checkNotNull;

public class LoadBalancerNodeService implements QueryService<LoadBalancerNode, LoadBalancerNodeFilter, LoadBalancerNodeMetadata> {

    private final LoadBalancerNodeClient loadBalancerNodeClient;
    private final LoadBalancerPoolService loadBalancerPoolService;

    public LoadBalancerNodeService(
            LoadBalancerNodeClient loadBalancerNodeClient,
            LoadBalancerPoolService loadBalancerPoolService
    ) {
        this.loadBalancerNodeClient = loadBalancerNodeClient;
        this.loadBalancerPoolService = loadBalancerPoolService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<LoadBalancerNodeMetadata> findLazy(LoadBalancerNodeFilter filter) {
        checkNotNull(filter, "Filter must be not a null");

        Stream<LoadBalancerPoolMetadata> loadBalancerPools = loadBalancerPoolService.findLazy(
                filter.getLoadBalancerPoolFilter()
        );

        return
            loadBalancerPools
                .flatMap(loadBalancerPoolMetadata -> loadBalancerNodeClient
                                .getLoadBalancerNodes(
                                        loadBalancerPoolMetadata.getDataCenterId(),
                                        loadBalancerPoolMetadata.getLoadBalancerId(),
                                        loadBalancerPoolMetadata.getId()
                                )
                                .stream()
                )
                .filter(filter.getPredicate());
    }

    /**
     * Update load balancer node list of the load balancer pool
     *
     * @param loadBalancerPool load balancer pool
     * @param nodesList node balancer node list
     * @return OperationFuture wrapper for load balancer pool
     */
    public OperationFuture<LoadBalancerPool> update(
            LoadBalancerPool loadBalancerPool,
            List<LoadBalancerNodeMetadata> nodesList
    ) {
        LoadBalancerPoolMetadata loadBalancerPoolMetadata = loadBalancerPoolService.findByRef(
                loadBalancerPool
        );

        loadBalancerNodeClient.update(
                loadBalancerPoolMetadata.getDataCenterId(),
                loadBalancerPoolMetadata.getLoadBalancerId(),
                loadBalancerPoolMetadata.getId(),
                nodesList
        );

        return new OperationFuture<>(
                loadBalancerPool,
                new NoWaitingJobFuture()
        );
    }

}
