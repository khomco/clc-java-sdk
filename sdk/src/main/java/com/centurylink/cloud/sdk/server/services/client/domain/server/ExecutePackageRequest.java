package com.centurylink.cloud.sdk.server.services.client.domain.server;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khomco on 5/23/16.
 */
public class ExecutePackageRequest {
    private List<String> servers;

    @JsonProperty("package")
    private Package pkg;


    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    public ExecutePackageRequest pkg(Package pkg) {
        setPkg(pkg);
        return this;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public ExecutePackageRequest servers(List<String> servers) {
        setServers(servers);
        return this;
    }


    public class Package {
        private String packageId;
        private Map<String, String> parameters = new HashMap<>();

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public Package packageId(String packageId) {
            setPackageId(packageId);
            return this;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, String> parameters) {
            this.parameters = parameters;
        }

        public Package parameters(Map<String, String> parameters) {
            setParameters(parameters);
            return this;
        }

        public Package parameter(String key, String value) {
            getParameters().put(key, value);
            return this;
        }
    }
}
