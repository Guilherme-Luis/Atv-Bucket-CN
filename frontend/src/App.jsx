import { Route, Routes } from 'react-router-dom';
import Cabecalho from './components/Cabecalho';
import ListaPratos from './pages/ListaPratos';
import CadastroPrato from './pages/CadastroPrato';
import DetalhePrato from './pages/DetalhePrato';
import PaginaNaoEncontrada from './pages/PaginaNaoEncontrada';

export default function App() {
  return (
    <>
      <Cabecalho />
      <main className="container conteudo">
        <Routes>
          <Route path="/" element={<ListaPratos />} />
          <Route path="/pratos/novo" element={<CadastroPrato />} />
          <Route path="/pratos/:id" element={<DetalhePrato />} />
          <Route path="/pratos/:id/editar" element={<CadastroPrato />} />
          <Route path="*" element={<PaginaNaoEncontrada />} />
        </Routes>
      </main>
    </>
  );
}
