package restaurante.service;

import restaurante.dto.ArquivoImagem;
import restaurante.dto.ObjetoBucketResponse;
import restaurante.dto.PratoDetalheResponse;
import restaurante.exception.ArquivoInvalidoException;
import restaurante.exception.RecursoNaoEncontradoException;
import restaurante.model.Imagem;
import restaurante.model.Prato;
import restaurante.repository.PratoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ImagemService {

    private static final String PASTA_PRATOS = "pratos";

    private static final Map<String, String> EXTENSOES_PERMITIDAS = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif"
    );

    private final PratoService pratoService;
    private final PratoRepository pratoRepository;
    private final BucketService bucketService;

    public ImagemService(PratoService pratoService, PratoRepository pratoRepository, BucketService bucketService) {
        this.pratoService = pratoService;
        this.pratoRepository = pratoRepository;
        this.bucketService = bucketService;
    }

    public PratoDetalheResponse adicionar(String pratoId, List<ArquivoImagem> arquivos, Integer indicePrincipal) {
        validar(arquivos, indicePrincipal);
        Prato prato = pratoService.buscarEntidade(pratoId);

        List<String> objetosEnviados = new ArrayList<>();
        try {
            for (int i = 0; i < arquivos.size(); i++) {
                ArquivoImagem arquivo = arquivos.get(i);
                String objeto = gerarNomeObjeto(pratoId, arquivo.contentType());
                String url = bucketService.enviar(objeto, arquivo);
                objetosEnviados.add(objeto);

                Imagem imagem = new Imagem(UUID.randomUUID().toString(), objeto, url,
                        arquivo.nome(), arquivo.contentType(), arquivo.tamanho());
                prato.adicionarImagem(imagem);

                if (indicePrincipal != null && indicePrincipal == i) {
                    prato.definirImagemPrincipal(imagem.getId());
                }
            }
            return PratoDetalheResponse.de(pratoRepository.save(prato));
        } catch (RuntimeException e) {
            objetosEnviados.forEach(bucketService::excluir);
            throw e;
        }
    }

    public PratoDetalheResponse definirPrincipal(String pratoId, String imagemId) {
        Prato prato = pratoService.buscarEntidade(pratoId);
        buscarImagem(prato, imagemId);
        prato.definirImagemPrincipal(imagemId);
        return PratoDetalheResponse.de(pratoRepository.save(prato));
    }

    public PratoDetalheResponse excluir(String pratoId, String imagemId) {
        Prato prato = pratoService.buscarEntidade(pratoId);
        Imagem imagem = buscarImagem(prato, imagemId);
        bucketService.excluir(imagem.getObjeto());
        prato.removerImagem(imagem);
        return PratoDetalheResponse.de(pratoRepository.save(prato));
    }

    public ArquivoImagem baixar(String pratoId, String imagemId) {
        Imagem imagem = buscarImagem(pratoService.buscarEntidade(pratoId), imagemId);
        byte[] conteudo = bucketService.baixar(imagem.getObjeto());
        return new ArquivoImagem(imagem.getNomeOriginal(), imagem.getContentType(), conteudo);
    }

    public List<ObjetoBucketResponse> listarObjetos(String pratoId) {
        pratoService.buscarEntidade(pratoId);
        return bucketService.listar(PASTA_PRATOS + "/" + pratoId + "/");
    }

    private Imagem buscarImagem(Prato prato, String imagemId) {
        return prato.buscarImagem(imagemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Imagem não encontrada"));
    }

    private void validar(List<ArquivoImagem> arquivos, Integer indicePrincipal) {
        if (arquivos == null || arquivos.isEmpty()) {
            throw new ArquivoInvalidoException("Selecione ao menos uma imagem para enviar");
        }
        for (ArquivoImagem arquivo : arquivos) {
            if (arquivo.tamanho() == 0) {
                throw new ArquivoInvalidoException("O arquivo " + arquivo.nome() + " está vazio");
            }
            if (arquivo.contentType() == null || !EXTENSOES_PERMITIDAS.containsKey(arquivo.contentType())) {
                throw new ArquivoInvalidoException("O arquivo " + arquivo.nome() + " não é uma imagem JPG, PNG, WEBP ou GIF");
            }
        }
        if (indicePrincipal != null && (indicePrincipal < 0 || indicePrincipal >= arquivos.size())) {
            throw new ArquivoInvalidoException("A imagem principal informada não faz parte do envio");
        }
    }

    private String gerarNomeObjeto(String pratoId, String contentType) {
        return "%s/%s/%s.%s".formatted(PASTA_PRATOS, pratoId, UUID.randomUUID(), EXTENSOES_PERMITIDAS.get(contentType));
    }

}
