package restaurante.dto;

import java.time.OffsetDateTime;

public record ObjetoBucketResponse(
        String nome,
        String url,
        Long tamanho,
        String contentType,
        OffsetDateTime criadoEm
) {
}
