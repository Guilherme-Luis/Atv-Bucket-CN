package restaurante.dto;

import restaurante.model.Imagem;

import java.time.Instant;

public record ImagemResponse(
        String id,
        String objeto,
        String url,
        String nomeOriginal,
        String contentType,
        long tamanho,
        boolean principal,
        Instant dataUpload
) {

    public static ImagemResponse de(Imagem imagem) {
        return new ImagemResponse(
                imagem.getId(),
                imagem.getObjeto(),
                imagem.getUrl(),
                imagem.getNomeOriginal(),
                imagem.getContentType(),
                imagem.getTamanho(),
                imagem.isPrincipal(),
                imagem.getDataUpload()
        );
    }

}
