package restaurante.exception;

import restaurante.dto.ErroResponse;
import com.google.cloud.storage.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ArquivoInvalidoException.class)
    public ResponseEntity<ErroResponse> tratarArquivoInvalido(ArquivoInvalidoException ex) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErroResponse> tratarArquivoAusente(MissingServletRequestPartException ex) {
        return responder(HttpStatus.BAD_REQUEST, "Selecione ao menos uma imagem para enviar");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErroResponse> tratarTamanhoExcedido(MaxUploadSizeExceededException ex) {
        return responder(HttpStatus.CONTENT_TOO_LARGE, "As imagens excedem o tamanho máximo permitido");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            campos.putIfAbsent(erro.getField(), erro.getDefaultMessage());
        }
        ErroResponse corpo = new ErroResponse(HttpStatus.BAD_REQUEST.value(), "Existem campos inválidos", campos);
        return ResponseEntity.badRequest().body(corpo);
    }

    @ExceptionHandler({BucketException.class, StorageException.class})
    public ResponseEntity<ErroResponse> tratarFalhaBucket(RuntimeException ex) {
        log.error("Falha na comunicação com o bucket", ex);
        return responder(HttpStatus.BAD_GATEWAY, "Não foi possível acessar o bucket de imagens");
    }

    private ResponseEntity<ErroResponse> responder(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErroResponse(status.value(), mensagem));
    }

}
