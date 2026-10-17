package restaurante.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
public class GcpStorageConfig {

    @Bean
    public Storage storage(StorageProperties properties) throws IOException {
        ServiceAccountCredentials credenciais = ServiceAccountCredentials.fromPkcs8(
                properties.clientId(),
                properties.clientEmail(),
                properties.privateKey().replace("\\n", "\n"),
                properties.privateKeyId(),
                List.of());

        return StorageOptions.newBuilder()
                .setProjectId(properties.projectId())
                .setCredentials(credenciais)
                .build()
                .getService();
    }

}
