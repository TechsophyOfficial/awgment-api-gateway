package com.techsophy.tsf.services.gateway.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ConfigurationProperties(prefix = "tenants")
@Component
@Data
public class TenantRegistration
{
    private List<String> registrations;

    /**
     * Every realm listed, whether as YAML list entries, comma separated values in one entry, or both.
     * Not a bean property, so the configuration binder ignores it.
     */
    public Set<String> trustedRealms()
    {
        if (registrations == null)
        {
            return Set.of();
        }
        return registrations.stream()
                .filter(entry -> entry != null)
                .flatMap(entry -> Arrays.stream(entry.split(",")))
                .map(String::trim)
                .filter(realm -> !realm.isEmpty())
                .collect(Collectors.toSet());
    }
}
