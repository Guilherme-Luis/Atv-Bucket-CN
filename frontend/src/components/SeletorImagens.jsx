import { useEffect, useState } from 'react';
import { formatarTamanho } from '../utils/formatadores';
import './SeletorImagens.css';

const TIPOS_ACEITOS = ['image/jpeg', 'image/png', 'image/webp', 'image/gif'];

export default function SeletorImagens({ arquivos, indiceCapa, onChange, permitirCapa = true }) {
  const [arrastando, setArrastando] = useState(false);
  const [previas, setPrevias] = useState([]);

  useEffect(() => {
    const novas = arquivos.map((arquivo) => ({ arquivo, url: URL.createObjectURL(arquivo) }));
    setPrevias(novas);
    return () => novas.forEach((previa) => URL.revokeObjectURL(previa.url));
  }, [arquivos]);

  function adicionar(lista) {
    const novos = Array.from(lista).filter((arquivo) => TIPOS_ACEITOS.includes(arquivo.type));
    if (novos.length === 0) {
      return;
    }
    const todos = [...arquivos, ...novos];
    onChange(todos, indiceCapa ?? 0);
  }

  function remover(indice) {
    const restantes = arquivos.filter((_, i) => i !== indice);
    let novaCapa = indiceCapa;
    if (restantes.length === 0) {
      novaCapa = null;
    } else if (indice === indiceCapa) {
      novaCapa = 0;
    } else if (indice < indiceCapa) {
      novaCapa = indiceCapa - 1;
    }
    onChange(restantes, novaCapa);
  }

  function aoSoltar(evento) {
    evento.preventDefault();
    setArrastando(false);
    adicionar(evento.dataTransfer.files);
  }

  return (
    <div className="seletor">
      <label
        className={`seletor__area${arrastando ? ' seletor__area--ativa' : ''}`}
        onDragOver={(evento) => {
          evento.preventDefault();
          setArrastando(true);
        }}
        onDragLeave={() => setArrastando(false)}
        onDrop={aoSoltar}
      >
        <input
          type="file"
          accept={TIPOS_ACEITOS.join(',')}
          multiple
          onChange={(evento) => {
            adicionar(evento.target.files);
            evento.target.value = '';
          }}
        />
        <strong>Arraste as fotos aqui ou clique para escolher</strong>
        <span>JPG, PNG, WEBP ou GIF, até 10 MB cada</span>
      </label>

      {previas.length > 0 && (
        <ul className="seletor__lista">
          {previas.map(({ arquivo, url }, indice) => {
            const capa = permitirCapa && indice === indiceCapa;
            return (
              <li key={url} className={`seletor__item${capa ? ' seletor__item--capa' : ''}`}>
                <img src={url} alt={arquivo.name} />
                <div className="seletor__info">
                  <span className="seletor__nome" title={arquivo.name}>
                    {arquivo.name}
                  </span>
                  <span className="seletor__tamanho">{formatarTamanho(arquivo.size)}</span>
                </div>
                <div className="seletor__acoes">
                  {permitirCapa && (
                    <label className="seletor__capa">
                      <input
                        type="radio"
                        name="capa"
                        checked={capa}
                        onChange={() => onChange(arquivos, indice)}
                      />
                      Capa
                    </label>
                  )}
                  <button type="button" className="botao-texto botao-texto--perigo" onClick={() => remover(indice)}>
                    Remover
                  </button>
                </div>
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
}
