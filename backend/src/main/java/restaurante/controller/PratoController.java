package restaurante.controller;

import restaurante.dto.PratoDetalheResponse;
import restaurante.dto.PratoRequest;
import restaurante.dto.PratoResumoResponse;
import restaurante.service.PratoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pratos")
public class PratoController {

    private final PratoService pratoService;

    public PratoController(PratoService pratoService) {
        this.pratoService = pratoService;
    }

    @GetMapping
    public List<PratoResumoResponse> listar(@RequestParam(required = false) String busca) {
        return pratoService.listar(busca);
    }

    @GetMapping("/{id}")
    public PratoDetalheResponse buscar(@PathVariable String id) {
        return pratoService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<PratoDetalheResponse> criar(@RequestBody @Valid PratoRequest request) {
        PratoDetalheResponse prato = pratoService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(prato.id())
                .toUri();
        return ResponseEntity.created(location).body(prato);
    }

    @PutMapping("/{id}")
    public PratoDetalheResponse atualizar(@PathVariable String id, @RequestBody @Valid PratoRequest request) {
        return pratoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) {
        pratoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

}
