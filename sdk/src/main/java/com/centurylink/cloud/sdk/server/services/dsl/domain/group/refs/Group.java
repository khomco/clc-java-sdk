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

package com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.services.refs.Reference;
import com.centurylink.cloud.sdk.server.services.dsl.domain.group.filters.GroupFilter;

/**
 * {@inheritDoc}
 */
public abstract class Group implements Reference<GroupFilter> {

    public static final String ARCHIVE = "Archive";
    public static final String TEMPLATES = "Templates";
    public static final String DEFAULT_GROUP = "Default Group";

    /**
     * Method allow to refer group by it's ID. Comparison is by full match and case sensitive.
     *
     * @param id is not null ID of group
     * @return {@link GroupByIdRef}
     */
    public static GroupByIdRef refById(String id) {
        return new GroupByIdRef(id);
    }

    /**
     * Method allow to refer group by name. Filtering is by full match. Comparison is case insensitive.
     *
     * @return {@link GroupNameRef}
     */
    public static GroupNameRef refByName() {
        return new GroupNameRef(null, null);
    }

    public static GroupNameRef refByName(DataCenter dataCenter, String name) {
        return new GroupNameRef(dataCenter, name);
    }

    @Override
    public String toString() {
        return this.toReadableString();
    }
}
