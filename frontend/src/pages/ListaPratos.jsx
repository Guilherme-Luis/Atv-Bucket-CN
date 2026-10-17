import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Aviso from '../components/Aviso';
import Carregando from '../components/Carregando';
import PratoCard from '../components/PratoCard';
import { listarPratos } from '../services/pratoService';
import { pluralizar } from '../utils/formatadores';
import './ListaPratos.css';

export default function ListaPratos() {
  const [pratos, setPratos] = useState([]);
  const [busca, setBusca] = useState('');
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState('');

  useEffect(() => {
    let ativo = true;
    const espera = setTimeout(() => {
      setCarregando(true);
      listarPratos(busca)
        .then((dados) => {
          if (ativo) {
            setPratos(dados);
            setErro('');
          }
        })
        .catch((falha) => ativo && setErro(falha.message))
        .finally(() => ativo && setCarregando(false));
    }, 300);

    return () => {
      ativo = false;
      clearTimeout(espera);
    };
  }, [busca]);

  return (
    <section>
      <header className="lista__topo">
        <div>
          <h1 className="lista__titulo">Cardápio</h1>
          <p className="lista__subtitulo">
            {carregando ? 'Buscando pratos...' : pluralizar(pratos.length, 'prato no cardápio', 'pratos no cardápio')}
          </p>
        </div>

        <label className="lista__busca">
          <span className="visualmente-oculto">Buscar prato pelo nome</span>
          <input
            type="search"
            placeholder="Buscar prato pelo nome"
            value={busca}
            onChange={(evento) => setBusca(evento.target.value)}
          />
        </label>
      </header>

      <Aviso>{erro}</Aviso>

      {carregando && pratos.length === 0 && <Carregando texto="Carregando o cardápio..." />}

      {!carregando && !erro && pratos.length === 0 && (
        <div className="lista__vazia">
          {busca ? (
            <p>Nenhum prato encontrado para "{busca}".</p>
          ) : (
            <>
              <p>Nenhum prato cadastrado ainda.</p>
              <Link to="/pratos/novo" className="botao">
                Cadastrar o primeiro prato
              </Link>
            </>
          )}
        </div>
      )}

      {pratos.length > 0 && (
        <div className="lista__grade">
          {pratos.map((prato) => (
            <PratoCard key={prato.id} prato={prato} />
          ))}
        </div>
      )}
    </section>
  );
}
