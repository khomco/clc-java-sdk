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

package com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "dataCenterEnabled",
        "importVMEnabled",
        "supportsPremiumStorage",
        "supportsSharedLoadBalancer",
        "deployableNetworks",
        "templates",
        "importableOSTypes"
})
public class DatacenterDeploymentCapabilitiesMetadata {

    @JsonProperty("dataCenterEnabled")
    private Boolean dataCenterEnabled;
    @JsonProperty("importVMEnabled")
    private Boolean importVMEnabled;
    @JsonProperty("supportsPremiumStorage")
    private Boolean supportsPremiumStorage;
    @JsonProperty("supportsSharedLoadBalancer")
    private Boolean supportsSharedLoadBalancer;
    @JsonProperty("deployableNetworks")
    private List<NetworkMetadata> deployableNetworks = new ArrayList<NetworkMetadata>();
    @JsonProperty("templates")
    private List<TemplateMetadata> templates = new ArrayList<TemplateMetadata>();
    @JsonProperty("importableOSTypes")
    private List<ImportableOSType> importableOSTypes = new ArrayList<ImportableOSType>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The dataCenterEnabled
     */
    @JsonProperty("dataCenterEnabled")
    public Boolean getDataCenterEnabled() {
        return dataCenterEnabled;
    }

    /**
     *
     * @param dataCenterEnabled
     * The dataCenterEnabled
     */
    @JsonProperty("dataCenterEnabled")
    public void setDataCenterEnabled(Boolean dataCenterEnabled) {
        this.dataCenterEnabled = dataCenterEnabled;
    }

    /**
     *
     * @return
     * The importVMEnabled
     */
    @JsonProperty("importVMEnabled")
    public Boolean getImportVMEnabled() {
        return importVMEnabled;
    }

    /**
     *
     * @param importVMEnabled
     * The importVMEnabled
     */
    @JsonProperty("importVMEnabled")
    public void setImportVMEnabled(Boolean importVMEnabled) {
        this.importVMEnabled = importVMEnabled;
    }

    /**
     *
     * @return
     * The supportsPremiumStorage
     */
    @JsonProperty("supportsPremiumStorage")
    public Boolean getSupportsPremiumStorage() {
        return supportsPremiumStorage;
    }

    /**
     *
     * @param supportsPremiumStorage
     * The supportsPremiumStorage
     */
    @JsonProperty("supportsPremiumStorage")
    public void setSupportsPremiumStorage(Boolean supportsPremiumStorage) {
        this.supportsPremiumStorage = supportsPremiumStorage;
    }

    /**
     *
     * @return
     * The supportsSharedLoadBalancer
     */
    @JsonProperty("supportsSharedLoadBalancer")
    public Boolean getSupportsSharedLoadBalancer() {
        return supportsSharedLoadBalancer;
    }

    /**
     *
     * @param supportsSharedLoadBalancer
     * The supportsSharedLoadBalancer
     */
    @JsonProperty("supportsSharedLoadBalancer")
    public void setSupportsSharedLoadBalancer(Boolean supportsSharedLoadBalancer) {
        this.supportsSharedLoadBalancer = supportsSharedLoadBalancer;
    }

    /**
     *
     * @return
     * The deployableNetworks
     */
    @JsonProperty("deployableNetworks")
    public List<NetworkMetadata> getDeployableNetworks() {
        return deployableNetworks;
    }

    /**
     *
     * @param deployableNetworks
     * The deployableNetworks
     */
    @JsonProperty("deployableNetworks")
    public void setDeployableNetworks(List<NetworkMetadata> deployableNetworks) {
        this.deployableNetworks = deployableNetworks;
    }

    public DatacenterDeploymentCapabilitiesMetadata deployableNetworks(List<NetworkMetadata> networks) {
        setDeployableNetworks(networks);
        return this;
    }

    /**
     *
     * @return
     * The templates
     */
    @JsonProperty("templates")
    public List<TemplateMetadata> getTemplates() {
        return templates;
    }

    /**
     *
     * @param templates
     * The templates
     */
    @JsonProperty("templates")
    public void setTemplates(List<TemplateMetadata> templates) {
        this.templates = templates;
    }

    /**
     *
     * @return
     * The importableOSTypes
     */
    @JsonProperty("importableOSTypes")
    public List<ImportableOSType> getImportableOSTypes() {
        return importableOSTypes;
    }

    /**
     *
     * @param importableOSTypes
     * The importableOSTypes
     */
    @JsonProperty("importableOSTypes")
    public void setImportableOSTypes(List<ImportableOSType> importableOSTypes) {
        this.importableOSTypes = importableOSTypes;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public NetworkMetadata findNetworkById(String id) {
        for (NetworkMetadata curNetwork : deployableNetworks) {
            if (curNetwork.getNetworkId().equalsIgnoreCase(id)) {
                return curNetwork;
            }
        }

        return null;
    }

}