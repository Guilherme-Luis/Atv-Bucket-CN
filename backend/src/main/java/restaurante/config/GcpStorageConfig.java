package restaurante.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GcpStorageConfig {

    @Bean
    public Storage storage(StorageProperties properties) throws IOException {
        StorageOptions.Builder builder = StorageOptions.newBuilder()
                .setProjectId(properties.projectId());

        if (StringUtils.hasText(properties.credentials())) {
            try (InputStream chave = new FileInputStream(properties.credentials())) {
                builder.setCredentials(ServiceAccountCredentials.fromStream(chave));
            }
        }

        return builder.build().getService();
    }

}
