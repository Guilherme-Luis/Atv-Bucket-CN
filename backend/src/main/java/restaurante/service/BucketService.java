package restaurante.service;

import restaurante.config.StorageProperties;
import restaurante.dto.ArquivoImagem;
import restaurante.dto.ObjetoBucketResponse;
import restaurante.exception.BucketException;
import restaurante.exception.RecursoNaoEncontradoException;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class BucketService {

    private static final String CACHE_CONTROL = "no-cache";

    private final Storage storage;
    private final StorageProperties properties;

    public BucketService(Storage storage, StorageProperties properties) {
        this.storage = storage;
        this.properties = properties;
    }

    public String enviar(String objeto, ArquivoImagem arquivo) {
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId(objeto))
                .setContentType(arquivo.contentType())
                .setCacheControl(CACHE_CONTROL)
                .build();

        try (WriteChannel writer = storage.writer(blobInfo)) {
            writer.write(ByteBuffer.wrap(arquivo.conteudo()));
        } catch (IOException e) {
            throw new BucketException("Falha ao enviar o objeto " + objeto, e);
        }

        return gerarUrlPublica(objeto);
    }

    public List<ObjetoBucketResponse> listar(String prefixo) {
        Iterable<Blob> blobs = storage.list(properties.bucket(), Storage.BlobListOption.prefix(prefixo)).iterateAll();

        return StreamSupport.stream(blobs.spliterator(), false)
                .map(blob -> new ObjetoBucketResponse(
                        blob.getName(),
                        gerarUrlPublica(blob.getName()),
                        blob.getSize(),
                        blob.getContentType(),
                        blob.getCreateTimeOffsetDateTime()))
                .toList();
    }

    public byte[] baixar(String objeto) {
        Blob blob = storage.get(blobId(objeto));
        if (blob == null) {
            throw new RecursoNaoEncontradoException("Objeto não encontrado no bucket: " + objeto);
        }
        return blob.getContent();
    }

    public void excluir(String objeto) {
        storage.delete(blobId(objeto));
    }

    public String gerarUrlPublica(String objeto) {
        return "%s/%s/%s".formatted(properties.urlPublica(), properties.bucket(), objeto);
    }

    private BlobId blobId(String objeto) {
        return BlobId.of(properties.bucket(), objeto);
    }

}
