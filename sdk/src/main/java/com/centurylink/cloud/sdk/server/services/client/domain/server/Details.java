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

package com.centurylink.cloud.sdk.server.services.client.domain.server;

import com.centurylink.cloud.sdk.policy.services.client.domain.AntiAffinityPolicyMetadata;
import com.centurylink.cloud.sdk.policy.services.client.domain.autoscale.AutoscalePolicyMetadata;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "ipAddresses",
        "secondaryIPAddresses",
        "alertPolicies",
        "antiAffinityPolicy",
        "cpu",
        "diskCount",
        "hostName",
        "inMaintenanceMode",
        "memoryMB",
        "powerState",
        "storageGB",
        "disks",
        "partitions",
        "snapshots",
        "customFields"
})
public class Details {

    @JsonProperty("ipAddresses")
    private List<IpAddress> ipAddresses = new ArrayList<IpAddress>();
    @JsonProperty("secondaryIPAddresses")
    private List<IpAddress> secondaryIPAddresses = new ArrayList<>();
    @JsonProperty("alertPolicies")
    private List<Object> alertPolicies = new ArrayList<Object>();
    @JsonProperty("antiAffinityPolicy")
    private AntiAffinityPolicyMetadata antiAffinityPolicy;
    @JsonProperty("cpuAutoscalePolicy")
    private AutoscalePolicyMetadata autoscalePolicy;
    @JsonProperty("cpu")
    private Integer cpu;
    @JsonProperty("diskCount")
    private Integer diskCount;
    @JsonProperty("hostName")
    private String hostName;
    @JsonProperty("inMaintenanceMode")
    private Boolean inMaintenanceMode;
    @JsonProperty("memoryMB")
    private Integer memoryMB;
    @JsonProperty("powerState")
    private String powerState;
    @JsonProperty("storageGB")
    private Integer storageGB;
    @JsonProperty("disks")
    private List<Disk> disks = new ArrayList<Disk>();
    @JsonProperty("partitions")
    private List<Partition> partitions = new ArrayList<Partition>();
    @JsonProperty("snapshots")
    private List<Snapshot> snapshots = new ArrayList<Snapshot>();
    @JsonProperty("customFields")
    private List<CustomField> customFields = new ArrayList<>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The ipAddresses
     */
    @JsonProperty("ipAddresses")
    public List<IpAddress> getIpAddresses() {
        return ipAddresses;
    }

    /**
     *
     * @param ipAddresses
     * The ipAddresses
     */
    @JsonProperty("ipAddresses")
    public void setIpAddresses(List<IpAddress> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    /**
     *
     * @return
     * The secondaryIPAddresses
     */
    @JsonProperty("secondaryIPAddresses")
    public List<IpAddress> getSecondaryIPAddresses() {
        return secondaryIPAddresses;
    }

    /**
     *
     * @param secondaryIPAddresses
     * The secondaryIPAddresses
     */
    @JsonProperty("secondaryIPAddresses")
    public void setSecondaryIPAddresses(List<IpAddress> secondaryIPAddresses) {
        this.secondaryIPAddresses = secondaryIPAddresses;
    }

    /**
     *
     * @return
     * The alertPolicies
     */
    @JsonProperty("alertPolicies")
    public List<Object> getAlertPolicies() {
        return alertPolicies;
    }

    /**
     *
     * @param alertPolicies
     * The alertPolicies
     */
    @JsonProperty("alertPolicies")
    public void setAlertPolicies(List<Object> alertPolicies) {
        this.alertPolicies = alertPolicies;
    }

    /**
     *
     * @return
     * The antiAffinityPolicy
     */
    @JsonProperty("antiAffinityPolicy")
    public AntiAffinityPolicyMetadata getAntiAffinityPolicy() {
        return antiAffinityPolicy;
    }

    /**
     *
     * @param antiAffinityPolicy
     * The antiAffinityPolicy
     */
    @JsonProperty("antiAffinityPolicy")
    public void setAntiAffinityPolicy(AntiAffinityPolicyMetadata antiAffinityPolicy) {
        this.antiAffinityPolicy = antiAffinityPolicy;
    }

    /**
     *
     * @return
     * The autoscalePolicy
     */
    @JsonProperty("cpuAutoscalePolicy")
    public AutoscalePolicyMetadata getAutoscalePolicy() {
        return autoscalePolicy;
    }

    /**
     *
     * @param autoscalePolicy
     * The autoscalePolicy
     */
    @JsonProperty("cpuAutoscalePolicy")
    public void setAutoscalePolicy(AutoscalePolicyMetadata autoscalePolicy) {
        this.autoscalePolicy = autoscalePolicy;
    }

    /**
     *
     * @return
     * The cpu
     */
    @JsonProperty("cpu")
    public Integer getCpu() {
        return cpu;
    }

    /**
     *
     * @param cpu
     * The cpu
     */
    @JsonProperty("cpu")
    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    /**
     *
     * @return
     * The diskCount
     */
    @JsonProperty("diskCount")
    public Integer getDiskCount() {
        return diskCount;
    }

    /**
     *
     * @param diskCount
     * The diskCount
     */
    @JsonProperty("diskCount")
    public void setDiskCount(Integer diskCount) {
        this.diskCount = diskCount;
    }

    /**
     *
     * @return
     * The hostName
     */
    @JsonProperty("hostName")
    public String getHostName() {
        return hostName;
    }

    /**
     *
     * @param hostName
     * The hostName
     */
    @JsonProperty("hostName")
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     *
     * @return
     * The inMaintenanceMode
     */
    @JsonProperty("inMaintenanceMode")
    public Boolean getInMaintenanceMode() {
        return inMaintenanceMode;
    }

    /**
     *
     * @param inMaintenanceMode
     * The inMaintenanceMode
     */
    @JsonProperty("inMaintenanceMode")
    public void setInMaintenanceMode(Boolean inMaintenanceMode) {
        this.inMaintenanceMode = inMaintenanceMode;
    }

    /**
     *
     * @return
     * The memoryMB
     */
    @JsonProperty("memoryMB")
    public Integer getMemoryMB() {
        return memoryMB;
    }

    /**
     *
     * @param memoryMB
     * The memoryMB
     */
    @JsonProperty("memoryMB")
    public void setMemoryMB(Integer memoryMB) {
        this.memoryMB = memoryMB;
    }

    /**
     *
     * @return
     * The powerState
     */
    @JsonProperty("powerState")
    public String getPowerState() {
        return powerState;
    }

    /**
     *
     * @param powerState
     * The powerState
     */
    @JsonProperty("powerState")
    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }

    /**
     *
     * @return
     * The storageGB
     */
    @JsonProperty("storageGB")
    public Integer getStorageGB() {
        return storageGB;
    }

    /**
     *
     * @param storageGB
     * The storageGB
     */
    @JsonProperty("storageGB")
    public void setStorageGB(Integer storageGB) {
        this.storageGB = storageGB;
    }

    /**
     *
     * @return
     * The disks
     */
    @JsonProperty("disks")
    public List<Disk> getDisks() {
        return disks;
    }

    /**
     *
     * @param disks
     * The disks
     */
    @JsonProperty("disks")
    public void setDisks(List<Disk> disks) {
        this.disks = disks;
    }

    /**
     *
     * @return
     * The partitions
     */
    @JsonProperty("partitions")
    public List<Partition> getPartitions() {
        return partitions;
    }

    /**
     *
     * @param partitions
     * The partitions
     */
    @JsonProperty("partitions")
    public void setPartitions(List<Partition> partitions) {
        this.partitions = partitions;
    }

    /**
     *
     * @return
     * The snapshots
     */
    @JsonProperty("snapshots")
    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    /**
     *
     * @param snapshots
     * The snapshots
     */
    @JsonProperty("snapshots")
    public void setSnapshots(List<Snapshot> snapshots) {
        this.snapshots = snapshots;
    }

    /**
     *
     * @return
     * The customFields
     */
    @JsonProperty("customFields")
    public List<CustomField> getCustomFields() {
        return customFields;
    }

    /**
     *
     * @param customFields
     * The customFields
     */
    @JsonProperty("customFields")
    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}