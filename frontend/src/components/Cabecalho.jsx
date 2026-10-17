import { Link, NavLink } from 'react-router-dom';
import './Cabecalho.css';

export default function Cabecalho() {
  return (
    <header className="cabecalho">
      <div className="container cabecalho__conteudo">
        <Link to="/" className="cabecalho__marca">
          <img src="/favicon.svg" alt="" width="34" height="34" />
          <span>Cardápio</span>
        </Link>

        <nav className="cabecalho__menu" aria-label="Navegação principal">
          <NavLink to="/" end className="cabecalho__link">
            Pratos
          </NavLink>
          <NavLink to="/pratos/novo" className="cabecalho__link cabecalho__link--destaque">
            Cadastrar prato
          </NavLink>
        </nav>
      </div>
    </header>
  );
}
