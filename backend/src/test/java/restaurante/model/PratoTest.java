package restaurante.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PratoTest {

    private Prato prato;

    @BeforeEach
    void setUp() {
        prato = new Prato("Pessoni Dog Quente", "Cachorro-quente da casa", new BigDecimal("24.90"));
    }

    @Test
    void primeiraImagemAdicionadaDeveSerPrincipal() {
        Imagem primeira = novaImagem("1");
        Imagem segunda = novaImagem("2");

        prato.adicionarImagem(primeira);
        prato.adicionarImagem(segunda);

        assertThat(primeira.isPrincipal()).isTrue();
        assertThat(segunda.isPrincipal()).isFalse();
    }

    @Test
    void deveManterApenasUmaImagemPrincipal() {
        prato.adicionarImagem(novaImagem("1"));
        prato.adicionarImagem(novaImagem("2"));
        prato.adicionarImagem(novaImagem("3"));

        prato.definirImagemPrincipal("3");

        assertThat(prato.getImagens()).filteredOn(Imagem::isPrincipal).hasSize(1);
        assertThat(prato.getImagemPrincipal()).get().extracting(Imagem::getId).isEqualTo("3");
    }

    @Test
    void aoRemoverPrincipalOutraImagemDeveAssumir() {
        Imagem primeira = novaImagem("1");
        prato.adicionarImagem(primeira);
        prato.adicionarImagem(novaImagem("2"));

        prato.removerImagem(primeira);

        assertThat(prato.getImagens()).hasSize(1);
        assertThat(prato.getImagemPrincipal()).get().extracting(Imagem::getId).isEqualTo("2");
    }

    @Test
    void aoRemoverUnicaImagemPratoFicaSemPrincipal() {
        Imagem unica = novaImagem("1");
        prato.adicionarImagem(unica);

        prato.removerImagem(unica);

        assertThat(prato.getImagens()).isEmpty();
        assertThat(prato.getImagemPrincipal()).isEmpty();
    }

    private Imagem novaImagem(String id) {
        return new Imagem(id, "pratos/teste/" + id + ".jpg", "https://exemplo/" + id + ".jpg",
                id + ".jpg", "image/jpeg", 1024);
    }

}
