import { enviarJson, montarUrl, requisicao } from './api';

const RECURSO = '/pratos';

export function listarPratos(busca = '') {
  const parametros = busca.trim() ? `?busca=${encodeURIComponent(busca.trim())}` : '';
  return requisicao(`${RECURSO}${parametros}`);
}

export function buscarPrato(id) {
  return requisicao(`${RECURSO}/${id}`);
}

export function criarPrato(dados) {
  return enviarJson(RECURSO, 'POST', dados);
}

export function atualizarPrato(id, dados) {
  return enviarJson(`${RECURSO}/${id}`, 'PUT', dados);
}

export function excluirPrato(id) {
  return requisicao(`${RECURSO}/${id}`, { method: 'DELETE' });
}

export function enviarImagens(pratoId, arquivos, indicePrincipal) {
  const formulario = new FormData();
  arquivos.forEach((arquivo) => formulario.append('arquivos', arquivo));

  const parametros = indicePrincipal != null ? `?principal=${indicePrincipal}` : '';
  return requisicao(`${RECURSO}/${pratoId}/imagens${parametros}`, {
    method: 'POST',
    body: formulario,
  });
}

export function definirImagemPrincipal(pratoId, imagemId) {
  return requisicao(`${RECURSO}/${pratoId}/imagens/${imagemId}/principal`, { method: 'PATCH' });
}

export function excluirImagem(pratoId, imagemId) {
  return requisicao(`${RECURSO}/${pratoId}/imagens/${imagemId}`, { method: 'DELETE' });
}

export function urlDownloadImagem(pratoId, imagemId) {
  return montarUrl(`${RECURSO}/${pratoId}/imagens/${imagemId}/download`);
}
