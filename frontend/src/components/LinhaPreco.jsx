import { formatarPreco } from '../utils/formatadores';
import './LinhaPreco.css';

export default function LinhaPreco({ nome, preco, como: Titulo = 'h3', tamanho = 'normal' }) {
  return (
    <div className={`linha-preco linha-preco--${tamanho}`}>
      <Titulo className="linha-preco__nome">{nome}</Titulo>
      <span className="linha-preco__pontos" aria-hidden="true" />
      <span className="linha-preco__valor">{formatarPreco(preco)}</span>
    </div>
  );
}
