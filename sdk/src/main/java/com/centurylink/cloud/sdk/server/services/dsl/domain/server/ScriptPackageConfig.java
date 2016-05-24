package com.centurylink.cloud.sdk.server.services.dsl.domain.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by khomco on 5/23/16.
 */
public class ScriptPackageConfig {

    private String packageId;
    private Map<String, String> parameters = new HashMap<>();

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public ScriptPackageConfig packageId(String packageId) {
        setPackageId(packageId);
        return this;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public ScriptPackageConfig parameters(Map<String, String> parameters) {
        setParameters(parameters);
        return this;
    }

    public ScriptPackageConfig parameter(String key, String value) {
        getParameters().put(key, value);
        return this;
    }
}
