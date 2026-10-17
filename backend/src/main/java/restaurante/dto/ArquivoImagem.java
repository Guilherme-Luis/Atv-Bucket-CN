package restaurante.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

public record ArquivoImagem(String nome, String contentType, byte[] conteudo) {

    public static ArquivoImagem de(MultipartFile arquivo) {
        try {
            String nome = Objects.requireNonNullElse(arquivo.getOriginalFilename(), arquivo.getName());
            return new ArquivoImagem(nome, arquivo.getContentType(), arquivo.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("Não foi possível ler o arquivo " + arquivo.getOriginalFilename(), e);
        }
    }

    public long tamanho() {
        return conteudo.length;
    }

}
