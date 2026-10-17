package restaurante.dto;

import restaurante.model.Prato;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PratoDetalheResponse(
        String id,
        String nome,
        String descricao,
        BigDecimal preco,
        ImagemResponse imagemPrincipal,
        List<ImagemResponse> imagens,
        Instant criadoEm,
        Instant atualizadoEm
) {

    public static PratoDetalheResponse de(Prato prato) {
        return new PratoDetalheResponse(
                prato.getId(),
                prato.getNome(),
                prato.getDescricao(),
                prato.getPreco(),
                prato.getImagemPrincipal().map(ImagemResponse::de).orElse(null),
                prato.getImagens().stream().map(ImagemResponse::de).toList(),
                prato.getCriadoEm(),
                prato.getAtualizadoEm()
        );
    }

}
