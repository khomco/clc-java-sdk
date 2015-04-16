package com.centurylink.cloud.sdk.servers.client.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.server.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.SourceRestriction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

/**
 * @author aliaksandr.krasitski
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "internalIPAddress",
        "ports",
        "sourceRestrictions"
})
public class PublicIpAddressResponse {
    private String internalIPAddress;
    private List<PortConfig> ports;
    private List<SourceRestriction> sourceRestrictions;

    public String getInternalIPAddress() {
        return internalIPAddress;
    }

    public void setInternalIPAddress(String internalIPAddress) {
        this.internalIPAddress = internalIPAddress;
    }

    public PublicIpAddressResponse internalIPAddress(String internalIPAddress) {
        setInternalIPAddress(internalIPAddress);
        return this;
    }

    public List<PortConfig> getPorts() {
        return ports;
    }

    public void setPorts(List<PortConfig> ports) {
        this.ports = ports;
    }

    public PublicIpAddressResponse ports(List<PortConfig> ports) {
        setPorts(ports);
        return this;
    }

    public List<SourceRestriction> getSourceRestrictions() {
        return sourceRestrictions;
    }

    /**
     * Set source restrictions in CIDR(Classless Inter-Domain Routing) format
     * @param sourceRestrictions
     */
    public void setSourceRestrictions(List<SourceRestriction> sourceRestrictions) {
        this.sourceRestrictions = sourceRestrictions;
    }

    /**
     * Specify List of source restrictions in CIDR(Classless Inter-Domain Routing) format
     * @param sourceRestrictions
     * @return PublicIp configuration
     */
    public PublicIpAddressResponse sourceRestrictions(List<SourceRestriction> sourceRestrictions) {
        setSourceRestrictions(sourceRestrictions);
        return this;
    }
}
