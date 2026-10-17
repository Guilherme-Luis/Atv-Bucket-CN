import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import Aviso from '../components/Aviso';
import Carregando from '../components/Carregando';
import SeletorImagens from '../components/SeletorImagens';
import { atualizarPrato, buscarPrato, criarPrato, enviarImagens } from '../services/pratoService';
import './CadastroPrato.css';

const PRATO_VAZIO = { nome: '', descricao: '', preco: '' };

export default function CadastroPrato() {
  const { id } = useParams();
  const edicao = Boolean(id);
  const navigate = useNavigate();

  const [dados, setDados] = useState(PRATO_VAZIO);
  const [arquivos, setArquivos] = useState([]);
  const [indiceCapa, setIndiceCapa] = useState(null);
  const [errosCampos, setErrosCampos] = useState({});
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(edicao);
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    if (!edicao) {
      setDados(PRATO_VAZIO);
      return;
    }
    buscarPrato(id)
      .then((prato) => setDados({ nome: prato.nome, descricao: prato.descricao, preco: Number(prato.preco).toFixed(2) }))
      .catch((falha) => setErro(falha.message))
      .finally(() => setCarregando(false));
  }, [id, edicao]);

  function alterarCampo(evento) {
    const { name, value } = evento.target;
    setDados((atual) => ({ ...atual, [name]: value }));
    setErrosCampos((atual) => ({ ...atual, [name]: undefined }));
  }

  function alterarImagens(novosArquivos, novaCapa) {
    setArquivos(novosArquivos);
    setIndiceCapa(novaCapa);
  }

  async function salvar(evento) {
    evento.preventDefault();
    setSalvando(true);
    setErro('');
    setErrosCampos({});

    const corpo = { ...dados, preco: dados.preco === '' ? null : Number(dados.preco) };

    let prato;
    try {
      prato = edicao ? await atualizarPrato(id, corpo) : await criarPrato(corpo);
    } catch (falha) {
      setErro(falha.message);
      setErrosCampos(falha.campos ?? {});
      setSalvando(false);
      return;
    }

    if (!edicao && arquivos.length > 0) {
      try {
        await enviarImagens(prato.id, arquivos, indiceCapa);
      } catch (falha) {
        navigate(`/pratos/${prato.id}`, {
          state: { erro: `O prato foi salvo, mas as fotos não foram enviadas: ${falha.message}` },
        });
        return;
      }
    }

    navigate(`/pratos/${prato.id}`, {
      state: { sucesso: edicao ? 'Alterações salvas.' : 'Prato cadastrado.' },
    });
  }

  if (carregando) {
    return <Carregando />;
  }

  return (
    <section className="cadastro">
      <Link to={edicao ? `/pratos/${id}` : '/'} className="link-voltar">
        {edicao ? 'Voltar para o prato' : 'Voltar ao cardápio'}
      </Link>

      <h1 className="cadastro__titulo">{edicao ? 'Editar prato' : 'Novo prato'}</h1>

      <Aviso>{erro}</Aviso>

      <form className={`cadastro__form${edicao ? ' cadastro__form--edicao' : ''}`} onSubmit={salvar} noValidate>
        <div className="cadastro__dados">
          <div className="campo">
            <label htmlFor="nome">Nome</label>
            <input
              id="nome"
              name="nome"
              value={dados.nome}
              onChange={alterarCampo}
              maxLength={120}
              placeholder="Ex.: Pessoni Dog Quente"
              aria-invalid={Boolean(errosCampos.nome)}
              required
            />
            {errosCampos.nome && <span className="campo__erro">{errosCampos.nome}</span>}
          </div>

          <div className="campo">
            <label htmlFor="preco">Preço (R$)</label>
            <input
              id="preco"
              name="preco"
              type="number"
              inputMode="decimal"
              min="0.01"
              step="0.01"
              value={dados.preco}
              onChange={alterarCampo}
              placeholder="0,00"
              aria-invalid={Boolean(errosCampos.preco)}
              required
            />
            {errosCampos.preco && <span className="campo__erro">{errosCampos.preco}</span>}
          </div>

          <div className="campo">
            <label htmlFor="descricao">Descrição</label>
            <textarea
              id="descricao"
              name="descricao"
              rows={6}
              value={dados.descricao}
              onChange={alterarCampo}
              maxLength={1000}
              placeholder="Ingredientes, acompanhamentos e modo de preparo"
              aria-invalid={Boolean(errosCampos.descricao)}
              required
            />
            {errosCampos.descricao && <span className="campo__erro">{errosCampos.descricao}</span>}
          </div>
        </div>

        {!edicao && (
          <fieldset className="cadastro__fotos">
            <legend>Fotos do prato</legend>
            <p className="cadastro__dica">Envie quantas fotos quiser e marque qual será a capa no cardápio.</p>
            <SeletorImagens arquivos={arquivos} indiceCapa={indiceCapa} onChange={alterarImagens} />
          </fieldset>
        )}

        <div className="cadastro__acoes">
          <button type="submit" className="botao" disabled={salvando}>
            {salvando ? 'Salvando...' : edicao ? 'Salvar alterações' : 'Cadastrar prato'}
          </button>
          <Link to={edicao ? `/pratos/${id}` : '/'} className="botao botao--secundario">
            Cancelar
          </Link>
        </div>
      </form>
    </section>
  );
}
