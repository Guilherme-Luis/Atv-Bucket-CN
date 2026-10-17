import { formatarDataHora, formatarTamanho } from '../utils/formatadores';
import './ObjetosBucket.css';

export default function ObjetosBucket({ objetos, erro }) {
  if (erro) {
    return <p className="objetos__mensagem">{erro}</p>;
  }

  if (objetos.length === 0) {
    return <p className="objetos__mensagem">Nenhum arquivo deste prato está no bucket.</p>;
  }

  return (
    <div className="objetos">
      <table>
        <thead>
          <tr>
            <th scope="col">Objeto</th>
            <th scope="col">Tipo</th>
            <th scope="col">Tamanho</th>
            <th scope="col">Criado em</th>
          </tr>
        </thead>
        <tbody>
          {objetos.map((objeto) => (
            <tr key={objeto.nome}>
              <td>
                <a href={objeto.url} target="_blank" rel="noreferrer" className="objetos__nome">
                  {objeto.nome}
                </a>
              </td>
              <td>{objeto.contentType}</td>
              <td className="objetos__numero">{formatarTamanho(objeto.tamanho)}</td>
              <td className="objetos__numero">{formatarDataHora(objeto.criadoEm)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
