package restaurante.controller;

import restaurante.dto.ArquivoImagem;
import restaurante.dto.ObjetoBucketResponse;
import restaurante.dto.PratoDetalheResponse;
import restaurante.service.ImagemService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/pratos/{pratoId}/imagens")
public class ImagemController {

    private final ImagemService imagemService;

    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PratoDetalheResponse enviar(@PathVariable String pratoId,
                                       @RequestPart("arquivos") List<MultipartFile> arquivos,
                                       @RequestParam(required = false) Integer principal) {
        List<ArquivoImagem> imagens = arquivos.stream().map(ArquivoImagem::de).toList();
        return imagemService.adicionar(pratoId, imagens, principal);
    }

    @PatchMapping("/{imagemId}/principal")
    public PratoDetalheResponse definirPrincipal(@PathVariable String pratoId, @PathVariable String imagemId) {
        return imagemService.definirPrincipal(pratoId, imagemId);
    }

    @DeleteMapping("/{imagemId}")
    public PratoDetalheResponse excluir(@PathVariable String pratoId, @PathVariable String imagemId) {
        return imagemService.excluir(pratoId, imagemId);
    }

    @GetMapping("/{imagemId}/download")
    public ResponseEntity<byte[]> baixar(@PathVariable String pratoId, @PathVariable String imagemId) {
        ArquivoImagem arquivo = imagemService.baixar(pratoId, imagemId);
        ContentDisposition disposicao = ContentDisposition.attachment()
                .filename(arquivo.nome(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposicao.toString())
                .contentType(MediaType.parseMediaType(arquivo.contentType()))
                .contentLength(arquivo.tamanho())
                .body(arquivo.conteudo());
    }

    @GetMapping("/bucket")
    public List<ObjetoBucketResponse> listarObjetosNoBucket(@PathVariable String pratoId) {
        return imagemService.listarObjetos(pratoId);
    }

}
