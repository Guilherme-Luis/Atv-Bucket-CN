package restaurante.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Document(collection = "pratos")
public class Prato {

    @Id
    private String id;

    @Indexed
    private String nome;

    private String descricao;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal preco;

    private List<Imagem> imagens = new ArrayList<>();

    @CreatedDate
    private Instant criadoEm;

    @LastModifiedDate
    private Instant atualizadoEm;

    public Prato() {
    }

    public Prato(String nome, String descricao, BigDecimal preco) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
    }

    public Optional<Imagem> getImagemPrincipal() {
        return imagens.stream().filter(Imagem::isPrincipal).findFirst();
    }

    public Optional<Imagem> buscarImagem(String imagemId) {
        return imagens.stream().filter(imagem -> imagem.getId().equals(imagemId)).findFirst();
    }

    public void adicionarImagem(Imagem imagem) {
        imagem.setPrincipal(getImagemPrincipal().isEmpty());
        imagens.add(imagem);
    }

    public void definirImagemPrincipal(String imagemId) {
        imagens.forEach(imagem -> imagem.setPrincipal(imagem.getId().equals(imagemId)));
    }

    public void removerImagem(Imagem imagem) {
        imagens.remove(imagem);
        if (imagem.isPrincipal() && !imagens.isEmpty()) {
            imagens.getFirst().setPrincipal(true);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public List<Imagem> getImagens() {
        return imagens;
    }

    public void setImagens(List<Imagem> imagens) {
        this.imagens = imagens;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

}
