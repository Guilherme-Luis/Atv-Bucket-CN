package restaurante.service;

import restaurante.dto.PratoDetalheResponse;
import restaurante.dto.PratoRequest;
import restaurante.dto.PratoResumoResponse;
import restaurante.exception.RecursoNaoEncontradoException;
import restaurante.model.Imagem;
import restaurante.model.Prato;
import restaurante.repository.PratoRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PratoService {

    private final PratoRepository pratoRepository;
    private final BucketService bucketService;

    public PratoService(PratoRepository pratoRepository, BucketService bucketService) {
        this.pratoRepository = pratoRepository;
        this.bucketService = bucketService;
    }

    public List<PratoResumoResponse> listar(String busca) {
        List<Prato> pratos = StringUtils.hasText(busca)
                ? pratoRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(busca.trim())
                : pratoRepository.findAllByOrderByNomeAsc();

        return pratos.stream().map(PratoResumoResponse::de).toList();
    }

    public PratoDetalheResponse buscar(String id) {
        return PratoDetalheResponse.de(buscarEntidade(id));
    }

    public PratoDetalheResponse criar(PratoRequest request) {
        Prato prato = new Prato(request.nome().trim(), request.descricao().trim(), request.preco());
        return PratoDetalheResponse.de(pratoRepository.save(prato));
    }

    public PratoDetalheResponse atualizar(String id, PratoRequest request) {
        Prato prato = buscarEntidade(id);
        prato.setNome(request.nome().trim());
        prato.setDescricao(request.descricao().trim());
        prato.setPreco(request.preco());
        return PratoDetalheResponse.de(pratoRepository.save(prato));
    }

    public void excluir(String id) {
        Prato prato = buscarEntidade(id);
        prato.getImagens().stream()
                .map(Imagem::getObjeto)
                .forEach(bucketService::excluir);
        pratoRepository.delete(prato);
    }

    Prato buscarEntidade(String id) {
        return pratoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prato não encontrado"));
    }

}
