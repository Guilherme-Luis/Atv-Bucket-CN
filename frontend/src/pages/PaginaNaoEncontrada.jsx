import { Link } from 'react-router-dom';

export default function PaginaNaoEncontrada() {
  return (
    <section className="nao-encontrada">
      <h1>Página não encontrada</h1>
      <p>O endereço acessado não existe ou foi removido.</p>
      <Link to="/" className="botao">
        Ver o cardápio
      </Link>
    </section>
  );
}
