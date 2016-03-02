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

package com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.TemplateFilter;

/**
 * @author ilya.drabenia
 */
public class TemplateByNameRef extends Template {
    private final String name;
    private final String revision;

    TemplateByNameRef(DataCenter dataCenter, String name, String revision) {
        super(dataCenter);
        this.name = name;
        this.revision = revision;
    }

    public String getName() {
        return name;
    }

    public String getRevision() {
        return revision;
    }
    public TemplateByNameRef revision(String revision) {
        return new TemplateByNameRef(getDataCenter(), name, revision);
    }

    public TemplateByNameRef name(String name) {
        return new TemplateByNameRef(getDataCenter(), name, revision);
    }

    public TemplateByNameRef dataCenter(DataCenter dataCenter) {
        return new TemplateByNameRef(dataCenter, name, revision);
    }

    @Override
    public TemplateFilter asFilter() {
        return
            new TemplateFilter()
                .dataCenters(getDataCenter())
                .names(name)
                .revision(revision);
    }
}
