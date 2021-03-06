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

package com.centurylink.cloud.sdk.server.services.dsl.domain.server;

import com.centurylink.cloud.sdk.policy.services.dsl.domain.autoscale.refs.AutoscalePolicy;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.refs.AntiAffinityPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class Machine {
    private Integer cpuCount;
    private Integer ram;
    private List<DiskConfig> disks = new ArrayList<>();

    private AntiAffinityPolicy antiAffinityPolicy;
    private AutoscalePolicy autoscalePolicy;

    public Integer getCpuCount() {
        return cpuCount;
    }

    public void setCpuCount(Integer cpuCount) {
        this.cpuCount = cpuCount;
    }

    public Machine cpuCount(Integer cpuCount) {
        setCpuCount(cpuCount);
        return this;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Machine ram(Integer ram) {
        setRam(ram);
        return this;
    }

    public List<DiskConfig> getDisks() {
        return disks;
    }

    public void setDisks(List<DiskConfig> disks) {
        this.disks = disks;
    }

    public Machine disk(DiskConfig diskConfig) {
        disks.add(diskConfig);
        return this;
    }

    public AntiAffinityPolicy getAntiAffinityPolicy() {
        return antiAffinityPolicy;
    }

    public Machine antiAffinityPolicy(AntiAffinityPolicy antiAffinityPolicy) {
        this.antiAffinityPolicy = antiAffinityPolicy;
        return this;
    }

    public AutoscalePolicy getAutoscalePolicy() {
        return autoscalePolicy;
    }

    public Machine autoscalePolicy(AutoscalePolicy autoscalePolicy) {
        this.autoscalePolicy = autoscalePolicy;
        return this;
    }
}
