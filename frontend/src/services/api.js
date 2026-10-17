const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api';

export class ApiError extends Error {
  constructor(mensagem, status = 0, campos = {}) {
    super(mensagem);
    this.status = status;
    this.campos = campos;
  }
}

export function montarUrl(caminho) {
  return `${API_URL}${caminho}`;
}

export async function requisicao(caminho, opcoes = {}) {
  let resposta;
  try {
    resposta = await fetch(montarUrl(caminho), opcoes);
  } catch {
    throw new ApiError('Não foi possível conectar à API. Verifique se o backend está em execução.');
  }

  if (resposta.status === 204) {
    return null;
  }

  const corpo = await resposta.json().catch(() => null);

  if (!resposta.ok) {
    throw new ApiError(
      corpo?.mensagem ?? 'Não foi possível concluir a operação.',
      resposta.status,
      corpo?.campos ?? {},
    );
  }

  return corpo;
}

export function enviarJson(caminho, metodo, dados) {
  return requisicao(caminho, {
    method: metodo,
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(dados),
  });
}
