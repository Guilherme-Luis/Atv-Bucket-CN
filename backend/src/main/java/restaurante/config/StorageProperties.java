package restaurante.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gcp.storage")
public record StorageProperties(
        String projectId,
        String bucket,
        String credentials,
        String urlPublica
) {
}
