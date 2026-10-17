const moeda = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });

const dataHora = new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' });

export function formatarPreco(valor) {
  return moeda.format(Number(valor ?? 0));
}

export function formatarDataHora(valor) {
  return valor ? dataHora.format(new Date(valor)) : '-';
}

export function formatarTamanho(bytes) {
  if (bytes == null) {
    return '-';
  }
  if (bytes < 1024) {
    return `${bytes} B`;
  }
  if (bytes < 1024 * 1024) {
    return `${(bytes / 1024).toFixed(0)} KB`;
  }
  return `${(bytes / (1024 * 1024)).toFixed(1).replace('.', ',')} MB`;
}

export function pluralizar(quantidade, singular, plural) {
  return `${quantidade} ${quantidade === 1 ? singular : plural}`;
}
