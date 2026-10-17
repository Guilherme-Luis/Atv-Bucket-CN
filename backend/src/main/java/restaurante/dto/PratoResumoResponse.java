package restaurante.dto;

import restaurante.model.Imagem;
import restaurante.model.Prato;

import java.math.BigDecimal;

public record PratoResumoResponse(
        String id,
        String nome,
        String descricao,
        BigDecimal preco,
        String imagemPrincipalUrl,
        int quantidadeImagens
) {

    public static PratoResumoResponse de(Prato prato) {
        return new PratoResumoResponse(
                prato.getId(),
                prato.getNome(),
                prato.getDescricao(),
                prato.getPreco(),
                prato.getImagemPrincipal().map(Imagem::getUrl).orElse(null),
                prato.getImagens().size()
        );
    }

}
