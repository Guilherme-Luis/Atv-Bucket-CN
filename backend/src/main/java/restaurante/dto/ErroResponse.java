package restaurante.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErroResponse(
        int status,
        String mensagem,
        Map<String, String> campos,
        Instant dataHora
) {

    public ErroResponse(int status, String mensagem) {
        this(status, mensagem, Map.of(), Instant.now());
    }

    public ErroResponse(int status, String mensagem, Map<String, String> campos) {
        this(status, mensagem, campos, Instant.now());
    }

}
