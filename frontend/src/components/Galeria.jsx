import { urlDownloadImagem } from '../services/pratoService';
import { formatarDataHora, formatarTamanho } from '../utils/formatadores';
import './Galeria.css';

export default function Galeria({ pratoId, imagens, imagemEmProcesso, onDefinirCapa, onExcluir }) {
  if (imagens.length === 0) {
    return <p className="galeria__vazia">Este prato ainda não tem fotos. Envie a primeira logo abaixo.</p>;
  }

  return (
    <ul className="galeria">
      {imagens.map((imagem) => {
        const ocupada = imagemEmProcesso === imagem.id;
        return (
          <li key={imagem.id} className={`galeria__item${imagem.principal ? ' galeria__item--capa' : ''}`}>
            <a href={imagem.url} target="_blank" rel="noreferrer" className="galeria__foto">
              <img src={imagem.url} alt={imagem.nomeOriginal} loading="lazy" />
              {imagem.principal && <span className="galeria__selo">Capa</span>}
            </a>

            <div className="galeria__info">
              <span className="galeria__nome" title={imagem.nomeOriginal}>
                {imagem.nomeOriginal}
              </span>
              <span className="galeria__meta">
                {formatarTamanho(imagem.tamanho)}, enviada em {formatarDataHora(imagem.dataUpload)}
              </span>
            </div>

            <div className="galeria__acoes">
              {!imagem.principal && (
                <button
                  type="button"
                  className="botao-texto"
                  disabled={ocupada}
                  onClick={() => onDefinirCapa(imagem)}
                >
                  Usar como capa
                </button>
              )}
              <a className="botao-texto" href={urlDownloadImagem(pratoId, imagem.id)}>
                Baixar
              </a>
              <button
                type="button"
                className="botao-texto botao-texto--perigo"
                disabled={ocupada}
                onClick={() => onExcluir(imagem)}
              >
                Excluir
              </button>
            </div>
          </li>
        );
      })}
    </ul>
  );
}
