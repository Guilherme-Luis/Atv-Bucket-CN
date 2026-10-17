package restaurante.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gcp.storage")
public record StorageProperties(
        String projectId,
        String bucket,
        String clientId,
        String clientEmail,
        String privateKeyId,
        String privateKey,
        String urlPublica
) {
}
