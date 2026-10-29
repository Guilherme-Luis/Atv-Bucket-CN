import { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom';
import Aviso from '../components/Aviso';
import Carregando from '../components/Carregando';
import Galeria from '../components/Galeria';
import LinhaPreco from '../components/LinhaPreco';
import SeletorImagens from '../components/SeletorImagens';
import {
  buscarPrato,
  definirImagemPrincipal,
  enviarImagens,
  excluirImagem,
  excluirPrato,
} from '../services/pratoService';
import { formatarDataHora, pluralizar } from '../utils/formatadores';
import './DetalhePrato.css';

export default function DetalhePrato() {
  const { id } = useParams();
  const navigate = useNavigate();
  const location = useLocation();

  const [prato, setPrato] = useState(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState(location.state?.erro ?? '');
  const [sucesso, setSucesso] = useState(location.state?.sucesso ?? '');
  const [imagemEmProcesso, setImagemEmProcesso] = useState(null);
  const [novosArquivos, setNovosArquivos] = useState([]);
  const [enviando, setEnviando] = useState(false);

  useEffect(() => {
    if (location.state) {
      navigate(location.pathname, { replace: true, state: null });
    }
  }, [location, navigate]);

  useEffect(() => {
    setCarregando(true);
    buscarPrato(id)
      .then(setPrato)
      .catch((falha) => setErro(falha.message))
      .finally(() => setCarregando(false));
  }, [id]);

  function aplicarAtualizacao(pratoAtualizado, mensagem) {
    setPrato(pratoAtualizado);
    setSucesso(mensagem);
    setErro('');
  }

  async function executar(acao, mensagem) {
    try {
      const atualizado = await acao();
      aplicarAtualizacao(atualizado, mensagem);
      return true;
    } catch (falha) {
      setErro(falha.message);
      setSucesso('');
      return false;
    }
  }

  async function definirCapa(imagem) {
    setImagemEmProcesso(imagem.id);
    await executar(() => definirImagemPrincipal(id, imagem.id), 'Capa do prato atualizada.');
    setImagemEmProcesso(null);
  }

  async function removerImagem(imagem) {
    if (!window.confirm(`Excluir a foto "${imagem.nomeOriginal}"? Ela também será apagada do bucket.`)) {
      return;
    }
    setImagemEmProcesso(imagem.id);
    await executar(() => excluirImagem(id, imagem.id), 'Foto excluída.');
    setImagemEmProcesso(null);
  }

  async function enviarNovasImagens() {
    setEnviando(true);
    const mensagem = `${pluralizar(novosArquivos.length, 'foto enviada', 'fotos enviadas')}.`;
    if (await executar(() => enviarImagens(id, novosArquivos), mensagem)) {
      setNovosArquivos([]);
    }
    setEnviando(false);
  }

  async function removerPrato() {
    if (!window.confirm(`Excluir o prato "${prato.nome}" e todas as suas fotos?`)) {
      return;
    }
    try {
      await excluirPrato(id);
      navigate('/', { replace: true });
    } catch (falha) {
      setErro(falha.message);
    }
  }

  if (carregando) {
    return <Carregando />;
  }

  if (!prato) {
    return (
      <section>
        <Link to="/" className="link-voltar">
          Voltar ao cardápio
        </Link>
        <Aviso>{erro || 'Prato não encontrado.'}</Aviso>
      </section>
    );
  }

  const capa = prato.imagemPrincipal;

  return (
    <article className="detalhe">
      <Link to="/" className="link-voltar">
        Voltar ao cardápio
      </Link>

      <Aviso onFechar={() => setErro('')}>{erro}</Aviso>
      <Aviso tipo="sucesso" onFechar={() => setSucesso('')}>
        {sucesso}
      </Aviso>

      <div className="detalhe__principal">
        <figure className="detalhe__capa">
          {capa ? (
            <img src={capa.url} alt={`Foto principal de ${prato.nome}`} />
          ) : (
            <span className="detalhe__sem-foto">Sem foto principal</span>
          )}
        </figure>

        <div className="detalhe__dados">
          <LinhaPreco nome={prato.nome} preco={prato.preco} como="h1" tamanho="grande" />
          <p className="detalhe__descricao">{prato.descricao}</p>

          <dl className="detalhe__meta">
            <div>
              <dt>Fotos</dt>
              <dd>{prato.imagens.length}</dd>
            </div>
            <div>
              <dt>Cadastrado em</dt>
              <dd>{formatarDataHora(prato.criadoEm)}</dd>
            </div>
            <div>
              <dt>Última alteração</dt>
              <dd>{formatarDataHora(prato.atualizadoEm)}</dd>
            </div>
          </dl>

          <div className="detalhe__acoes">
            <Link to={`/pratos/${prato.id}/editar`} className="botao botao--secundario">
              Editar dados
            </Link>
            <button type="button" className="botao botao--perigo" onClick={removerPrato}>
              Excluir prato
            </button>
          </div>
        </div>
      </div>

      <section className="detalhe__secao">
        <h2>Galeria</h2>
        <Galeria
          pratoId={prato.id}
          imagens={prato.imagens}
          imagemEmProcesso={imagemEmProcesso}
          onDefinirCapa={definirCapa}
          onExcluir={removerImagem}
        />
      </section>

      <section className="detalhe__secao">
        <h2>Adicionar fotos</h2>
        <SeletorImagens
          arquivos={novosArquivos}
          indiceCapa={null}
          permitirCapa={false}
          onChange={(arquivos) => setNovosArquivos(arquivos)}
        />
        {novosArquivos.length > 0 && (
          <button type="button" className="botao detalhe__enviar" onClick={enviarNovasImagens} disabled={enviando}>
            {enviando ? 'Enviando...' : `Enviar ${pluralizar(novosArquivos.length, 'foto', 'fotos')}`}
          </button>
        )}
      </section>
    </article>
  );
}
