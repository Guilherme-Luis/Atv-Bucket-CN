import './Aviso.css';

export default function Aviso({ tipo = 'erro', children, onFechar }) {
  if (!children) {
    return null;
  }

  return (
    <div className={`aviso aviso--${tipo}`} role={tipo === 'erro' ? 'alert' : 'status'}>
      <p>{children}</p>
      {onFechar && (
        <button type="button" className="aviso__fechar" onClick={onFechar} aria-label="Fechar aviso">
          ×
        </button>
      )}
    </div>
  );
}
