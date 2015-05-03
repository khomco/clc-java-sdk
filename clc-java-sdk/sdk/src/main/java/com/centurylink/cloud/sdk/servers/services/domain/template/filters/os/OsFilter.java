package com.centurylink.cloud.sdk.servers.services.domain.template.filters.os;

import com.centurylink.cloud.sdk.common.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata;

import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class used for filter predefined image operating system types
 *
 * @author Ilya Drabenia
 */
public class OsFilter {
    private String osType;
    private CpuArchitecture architecture;
    private String version;
    private String edition;

    public OsFilter() {

    }

    /**
     * Method allow to restrict operating system vendors. It use string equality comparison.
     * Matching is case insensitive.
     *
     * @param osType is enum value of {@link OsType}
     * @return {@link OsFilter}
     * @throws NullPointerException
     */
    public OsFilter type(OsType osType) {
        this.osType =
            checkNotNull(osType, "OS type must be not a null")
                .getCode();

        return this;
    }

    /**
     * Method allow to restrict operating system vendors. It will use case sensitive string comparison.
     *
     * @param osType is not null string value of operating system vendor
     * @return {@link OsFilter}
     * @throws NullPointerException
     */
    public OsFilter type(String osType) {
        this.osType = checkNotNull(osType, "OS type must be not a null");

        return this;
    }

    /**
     * Method allow to restrict target CPU architecture of operating system
     *
     * @param architecture is value of {@link CpuArchitecture}
     * @return {@link OsFilter}
     * @throws NullPointerException in case when architecture is null
     */
    public OsFilter architecture(CpuArchitecture architecture) {
        this.architecture = checkNotNull(architecture, "Architecture must be not a null");

        return this;
    }

    /**
     * Method allow to restrict operating system version. It use string equality comparison.
     * Matching is case insensitive.
     *
     * @param version is not null string representation of operating system version ("6", "7")
     * @return {@link OsFilter}
     * @throws NullPointerException when version is null
     */
    public OsFilter version(String version) {
        this.version = checkNotNull(version, "Version must be not a null");

        return this;
    }

    /**
     * Method allow to restrict operating system edition. It use string equality comparison.
     * Matching is case insensitive.
     *
     * @param edition is not null string representation of operating system edition ("Datacenter", "Business").
     * @return {@link OsFilter}
     * @throws NullPointerException
     */
    public OsFilter edition(String edition) {
        this.edition = checkNotNull(edition, "Edition must be not a null");

        return this;
    }

    public Predicate<TemplateMetadata> getPredicate() {
        return t -> {
            String osDescription = t.getOsType().toUpperCase();

            if (osType != null) {
                if (osDescription.startsWith(osType.toUpperCase())) {
                    osDescription = osDescription.replace(osType.toUpperCase(), "");
                } else {
                    return false;
                }
            }

            if (architecture != null) {
                if (osDescription.endsWith(architecture.getCode().toUpperCase())) {
                    osDescription = osDescription.replace(architecture.getCode().toUpperCase(), "");
                } else {
                    return false;
                }
            }

            if (version != null) {
                if (osDescription.startsWith(version.toUpperCase())) {
                    osDescription = osDescription.replace(version.toUpperCase(), "");
                } else {
                    return false;
                }
            }

            if (edition != null) {
                if (osDescription.startsWith(edition.toUpperCase())) {
                    osDescription = osDescription.replace(edition.toUpperCase(), "");
                } else {
                    return false;
                }
            }

            return true;
        };
    }
}
