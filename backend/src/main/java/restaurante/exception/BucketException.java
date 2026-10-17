package restaurante.exception;

public class BucketException extends RuntimeException {

    public BucketException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

}
