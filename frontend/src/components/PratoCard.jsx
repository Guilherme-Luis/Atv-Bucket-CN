import { Link } from 'react-router-dom';
import LinhaPreco from './LinhaPreco';
import { pluralizar } from '../utils/formatadores';
import './PratoCard.css';

export default function PratoCard({ prato }) {
  return (
    <article className="prato-card">
      <Link to={`/pratos/${prato.id}`} className="prato-card__link">
        <div className="prato-card__foto">
          {prato.imagemPrincipalUrl ? (
            <img src={prato.imagemPrincipalUrl} alt={prato.nome} loading="lazy" />
          ) : (
            <span className="prato-card__sem-foto">Sem foto</span>
          )}
        </div>

        <LinhaPreco nome={prato.nome} preco={prato.preco} />
        <p className="prato-card__descricao">{prato.descricao}</p>
        <p className="prato-card__fotos">{pluralizar(prato.quantidadeImagens, 'foto', 'fotos')}</p>
      </Link>
    </article>
  );
}
