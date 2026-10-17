package restaurante.model;

import java.time.Instant;

public class Imagem {

    private String id;
    private String objeto;
    private String url;
    private String nomeOriginal;
    private String contentType;
    private long tamanho;
    private boolean principal;
    private Instant dataUpload;

    public Imagem() {
    }

    public Imagem(String id, String objeto, String url, String nomeOriginal, String contentType, long tamanho) {
        this.id = id;
        this.objeto = objeto;
        this.url = url;
        this.nomeOriginal = nomeOriginal;
        this.contentType = contentType;
        this.tamanho = tamanho;
        this.dataUpload = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObjeto() {
        return objeto;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public void setNomeOriginal(String nomeOriginal) {
        this.nomeOriginal = nomeOriginal;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getTamanho() {
        return tamanho;
    }

    public void setTamanho(long tamanho) {
        this.tamanho = tamanho;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public Instant getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(Instant dataUpload) {
        this.dataUpload = dataUpload;
    }

}
