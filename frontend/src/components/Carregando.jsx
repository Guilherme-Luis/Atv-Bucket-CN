export default function Carregando({ texto = 'Carregando...' }) {
  return (
    <p className="carregando" role="status" aria-live="polite">
      {texto}
    </p>
  );
}
