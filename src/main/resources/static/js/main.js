(() => {
  'use strict';

  /*
   * Segurança: este arquivo NUNCA usa innerHTML. Todo conteúdo vindo da API ou do usuário
   * (nomes, títulos, descrições, mensagens de erro) é inserido como TEXTO por meio do helper h(),
   * o que impede injeção de HTML/JavaScript (XSS). A CSP definida no index.html reforça essa proteção.
   */

  const API = '/api/v1';
  const VIEWS = ['painel', 'associados', 'pautas', 'sessoes', 'votacao', 'resultados'];

  const estado = {
    associados: [],
    pautas: [],
    sessoes: new Map(),
  };

  /* ---------- utilitários de DOM ---------- */

  const $ = (seletor, raiz = document) => raiz.querySelector(seletor);
  const $$ = (seletor, raiz = document) => Array.from(raiz.querySelectorAll(seletor));

  /** Cria um elemento; textos entram como nós de texto, nunca como HTML. */
  function h(tag, atributos = {}, ...filhos) {
    const el = document.createElement(tag);
    for (const [chave, valor] of Object.entries(atributos || {})) {
      if (valor === null || valor === undefined || valor === false) continue;
      if (chave === 'class') el.className = valor;
      else if (chave === 'text') el.textContent = valor;
      else if (chave.startsWith('on') && typeof valor === 'function') el.addEventListener(chave.slice(2), valor);
      else el.setAttribute(chave, valor === true ? '' : String(valor));
    }
    for (const filho of filhos.flat()) {
      if (filho === null || filho === undefined || filho === false) continue;
      el.append(filho);
    }
    return el;
  }

  /* ---------- formatação ---------- */

  const soDigitos = (valor) => String(valor ?? '').replace(/\D/g, '');

  function mascararCpf(valor) {
    const d = soDigitos(valor).slice(0, 11);
    let saida = d.slice(0, 3);
    if (d.length > 3) saida += '.' + d.slice(3, 6);
    if (d.length > 6) saida += '.' + d.slice(6, 9);
    if (d.length > 9) saida += '-' + d.slice(9, 11);
    return saida;
  }

  const exibirCpf = (cpf) => (soDigitos(cpf).length === 11 ? mascararCpf(cpf) : String(cpf ?? ''));

  function formatarData(iso) {
    const data = new Date(iso);
    return Number.isNaN(data.getTime())
      ? '—'
      : data.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'medium' });
  }

  function formatarDuracao(ms) {
    const total = Math.max(0, Math.floor(ms / 1000));
    const horas = Math.floor(total / 3600);
    const minutos = Math.floor((total % 3600) / 60);
    const segundos = total % 60;
    const dois = (n) => String(n).padStart(2, '0');
    return horas > 0 ? `${horas}:${dois(minutos)}:${dois(segundos)}` : `${dois(minutos)}:${dois(segundos)}`;
  }

  /* ---------- comunicação com a API ---------- */

  async function api(caminho, { metodo = 'GET', corpo } = {}) {
    const resposta = await fetch(API + caminho, {
      method: metodo,
      headers: corpo ? { 'Content-Type': 'application/json' } : undefined,
      body: corpo ? JSON.stringify(corpo) : undefined,
    });
    marcarApi(true);
    let dados = null;
    if (resposta.status !== 204) {
      try {
        dados = await resposta.json();
      } catch {
        dados = null;
      }
    }
    return { ok: resposta.ok, status: resposta.status, dados };
  }

  const mensagemDeErro = (r, padrao) => (r.dados && typeof r.dados.mensagem === 'string' && r.dados.mensagem) || padrao;

  function marcarApi(online) {
    const pilula = $('#api-status');
    pilula.classList.toggle('pill--ok', online);
    pilula.classList.toggle('pill--erro', !online);
    $('#api-status-texto').textContent = online ? 'API online' : 'API offline';
  }

  /** Executa uma ação tratando falhas de rede e evitando clique duplo no botão. */
  async function seguro(acao, botao) {
    if (botao) botao.disabled = true;
    try {
      await acao();
    } catch (erro) {
      console.error(erro);
      marcarApi(false);
      aviso('Não foi possível conectar à API. Verifique se a aplicação está rodando.', 'erro');
    } finally {
      if (botao) botao.disabled = false;
    }
  }

  const botaoDoFormulario = (evento) => $('button[type="submit"]', evento.currentTarget);

  /* ---------- avisos (toast) ---------- */

  function aviso(mensagem, tipo = 'ok') {
    const el = h('div', { class: `toast toast--${tipo}`, role: tipo === 'erro' ? 'alert' : 'status', text: mensagem });
    $('#toasts').append(el);
    setTimeout(() => el.remove(), 5000);
  }

  /* ---------- navegação ---------- */

  function mostrarView(nome) {
    const view = VIEWS.includes(nome) ? nome : 'painel';
    $$('.view').forEach((secao) => {
      secao.hidden = secao.dataset.view !== view;
    });
    $$('.nav a').forEach((link) => {
      if (link.dataset.view === view) link.setAttribute('aria-current', 'page');
      else link.removeAttribute('aria-current');
    });
    window.scrollTo({ top: 0 });
  }

  const viewDaUrl = () => location.hash.replace('#', '');

  function irPara(view, { pautaId } = {}) {
    if (pautaId !== undefined) {
      const select = view === 'sessoes' ? $('#sessao-pauta') : $('#resultado-pauta');
      select.value = String(pautaId);
    }
    if (viewDaUrl() === view) mostrarView(view);
    else location.hash = view;
  }

  /* ---------- renderização: selects e estatísticas ---------- */

  function preencherSelect(select, itens, rotulo, placeholder) {
    const atual = select.value;
    select.replaceChildren(new Option(placeholder, ''), ...itens.map((item) => new Option(rotulo(item), String(item.id))));
    if (Array.from(select.options).some((opcao) => opcao.value === atual)) select.value = atual;
  }

  function atualizarSelects() {
    const rotuloPauta = (p) => `#${p.id} · ${p.titulo}`;
    preencherSelect($('#sessao-pauta'), estado.pautas, rotuloPauta, 'Selecione uma pauta');
    preencherSelect($('#voto-pauta'), estado.pautas, rotuloPauta, 'Selecione uma pauta');
    preencherSelect($('#resultado-pauta'), estado.pautas, rotuloPauta, 'Selecione uma pauta');
    preencherSelect($('#voto-associado'), estado.associados, (a) => `#${a.id} · ${a.nome}`, 'Selecione um associado');
  }

  function atualizarEstatisticas() {
    $('#stat-associados').textContent = String(estado.associados.length);
    $('#stat-pautas').textContent = String(estado.pautas.length);
    const abertas = Array.from(estado.sessoes.values()).filter((s) => statusSessao(s) === 'ABERTA').length;
    $('#stat-sessoes').textContent = String(abertas);
  }

  /* ---------- associados ---------- */

  function renderAssociados(lista) {
    const linhas = lista.map((a) =>
      h('tr', {}, h('td', { text: a.id }), h('td', { text: a.nome }), h('td', { class: 'mono', text: exibirCpf(a.cpf) })),
    );
    $('#associados-corpo').replaceChildren(...linhas);
    $('#associados-tabela').hidden = lista.length === 0;
    $('#associados-vazio').hidden = lista.length > 0;
  }

  async function carregarAssociados() {
    const r = await api('/associados');
    if (!r.ok) return aviso(mensagemDeErro(r, 'Não foi possível carregar os associados.'), 'erro');
    estado.associados = Array.isArray(r.dados) ? r.dados : [];
    renderAssociados(estado.associados);
    atualizarSelects();
    atualizarEstatisticas();
  }

  async function cadastrarAssociado(evento) {
    evento.preventDefault();
    const nome = $('#assoc-nome').value.trim();
    const cpf = soDigitos($('#assoc-cpf').value);
    $('#assoc-nome').setAttribute('aria-invalid', String(!nome));
    $('#assoc-cpf').setAttribute('aria-invalid', String(cpf.length !== 11));
    if (!nome) return aviso('Informe o nome do associado.', 'erro');
    if (cpf.length !== 11) return aviso('O CPF deve ter 11 dígitos.', 'erro');

    await seguro(async () => {
      const r = await api('/associados', { metodo: 'POST', corpo: { nome, cpf } });
      if (!r.ok) return aviso(mensagemDeErro(r, 'Erro ao cadastrar associado.'), 'erro');
      aviso(`Associado "${r.dados.nome}" cadastrado com sucesso.`);
      evento.target.reset();
      await carregarAssociados();
    }, botaoDoFormulario(evento));
  }

  async function buscarAssociado(evento) {
    evento.preventDefault();
    const id = Number($('#assoc-busca-id').value);
    if (!Number.isInteger(id) || id < 1) return aviso('Informe um ID válido.', 'erro');
    await seguro(async () => {
      const r = await api(`/associados/${id}`);
      if (!r.ok) {
        renderAssociados([]);
        return aviso(mensagemDeErro(r, 'Associado não encontrado.'), 'erro');
      }
      renderAssociados([r.dados]);
    }, botaoDoFormulario(evento));
  }

  /* ---------- pautas ---------- */

  function cartaoPauta(pauta) {
    return h(
      'article',
      { class: 'item' },
      h('div', { class: 'item__head' }, h('span', { class: 'chip', text: `#${pauta.id}` }), h('h3', { class: 'item__title', text: pauta.titulo })),
      h('p', { class: 'item__text', text: pauta.descricao || 'Sem descrição.' }),
      h(
        'div',
        { class: 'item__actions' },
        h('button', { class: 'btn btn--ghost btn--sm', type: 'button', onclick: () => irPara('sessoes', { pautaId: pauta.id }) }, 'Abrir sessão'),
        h('button', { class: 'btn btn--ghost btn--sm', type: 'button', onclick: () => irPara('resultados', { pautaId: pauta.id }) }, 'Ver resultado'),
      ),
    );
  }

  function renderPautas(lista) {
    $('#pautas-lista').replaceChildren(...lista.map(cartaoPauta));
    $('#pautas-vazio').hidden = lista.length > 0;
  }

  async function carregarPautas() {
    const r = await api('/pautas');
    if (!r.ok) return aviso(mensagemDeErro(r, 'Não foi possível carregar as pautas.'), 'erro');
    estado.pautas = Array.isArray(r.dados) ? r.dados : [];
    renderPautas(estado.pautas);
    atualizarSelects();
    atualizarEstatisticas();
  }

  async function cadastrarPauta(evento) {
    evento.preventDefault();
    const titulo = $('#pauta-titulo').value.trim();
    const descricao = $('#pauta-descricao').value.trim();
    $('#pauta-titulo').setAttribute('aria-invalid', String(!titulo));
    if (!titulo) return aviso('Informe o título da pauta.', 'erro');

    await seguro(async () => {
      const r = await api('/pautas', { metodo: 'POST', corpo: { titulo, descricao } });
      if (!r.ok) return aviso(mensagemDeErro(r, 'Erro ao cadastrar pauta.'), 'erro');
      aviso(`Pauta "${r.dados.titulo}" cadastrada com sucesso.`);
      evento.target.reset();
      await carregarPautas();
    }, botaoDoFormulario(evento));
  }

  async function buscarPauta(evento) {
    evento.preventDefault();
    const id = Number($('#pauta-busca-id').value);
    if (!Number.isInteger(id) || id < 1) return aviso('Informe um ID válido.', 'erro');
    await seguro(async () => {
      const r = await api(`/pautas/${id}`);
      if (!r.ok) {
        renderPautas([]);
        return aviso(mensagemDeErro(r, 'Pauta não encontrada.'), 'erro');
      }
      renderPautas([r.dados]);
    }, botaoDoFormulario(evento));
  }

  /* ---------- sessões ---------- */

  function statusSessao(sessao, agora = new Date()) {
    const abertura = new Date(sessao.dataAbertura);
    const fechamento = new Date(sessao.dataFechamento);
    if (agora < abertura) return 'AGENDADA';
    if (agora > fechamento) return 'ENCERRADA';
    return 'ABERTA';
  }

  const classeDoStatus = (status) => ({ ABERTA: 'badge--ok', AGENDADA: 'badge--info' })[status] || '';

  function textoDoTempo(sessao, status, agora = new Date()) {
    if (status === 'ABERTA') return `Encerra em ${formatarDuracao(new Date(sessao.dataFechamento) - agora)}`;
    if (status === 'AGENDADA') return `Inicia em ${formatarDuracao(new Date(sessao.dataAbertura) - agora)}`;
    return 'Sessão encerrada';
  }

  function cartaoSessao(sessao) {
    const status = statusSessao(sessao);
    return h(
      'article',
      { class: 'item', 'data-sessao': sessao.id },
      h(
        'div',
        { class: 'item__head' },
        h('span', { class: 'chip', text: `Sessão #${sessao.id}` }),
        h('h3', { class: 'item__title', text: sessao.tituloPauta || `Pauta #${sessao.pautaId}` }),
        h('span', { class: `badge ${classeDoStatus(status)}`, 'data-campo': 'status', text: status }),
      ),
      h(
        'dl',
        { class: 'item__meta' },
        h('div', {}, h('dt', { text: 'Abertura:' }), h('dd', { text: formatarData(sessao.dataAbertura) })),
        h('div', {}, h('dt', { text: 'Fechamento:' }), h('dd', { text: formatarData(sessao.dataFechamento) })),
        h('div', {}, h('dt', { text: 'Situação:' }), h('dd', { 'data-campo': 'tempo', text: textoDoTempo(sessao, status) })),
      ),
    );
  }

  function renderSessoes() {
    const sessoes = Array.from(estado.sessoes.values()).sort((a, b) => b.id - a.id);
    $('#sessoes-lista').replaceChildren(...sessoes.map(cartaoSessao));
    $('#sessoes-vazio').hidden = sessoes.length > 0;
    atualizarEstatisticas();
  }

  /** Atualiza, a cada segundo, o status e a contagem regressiva sem redesenhar a lista. */
  function tiqueDasSessoes() {
    const agora = new Date();
    estado.sessoes.forEach((sessao, id) => {
      const cartao = $(`[data-sessao="${id}"]`);
      if (!cartao) return;
      const status = statusSessao(sessao, agora);
      const selo = $('[data-campo="status"]', cartao);
      selo.textContent = status;
      selo.className = `badge ${classeDoStatus(status)}`;
      $('[data-campo="tempo"]', cartao).textContent = textoDoTempo(sessao, status, agora);
    });
    atualizarEstatisticas();
  }

  function valorParaIso(valor) {
    return valor.length === 16 ? `${valor}:00` : valor;
  }

  async function abrirSessao(evento) {
    evento.preventDefault();
    const pautaId = Number($('#sessao-pauta').value);
    if (!pautaId) return aviso('Selecione uma pauta.', 'erro');

    const corpo = { pautaId };
    const minutos = Number($('#sessao-minutos').value);
    if (minutos > 0) corpo.minutos = Math.floor(minutos);

    const agendar = $('#sessao-agendar').checked;
    if (agendar) {
      const inicio = $('#sessao-inicio').value;
      if (!inicio) return aviso('Informe a data e a hora de início para agendar.', 'erro');
      corpo.dataInicio = valorParaIso(inicio);
      const fim = $('#sessao-fim').value;
      if (fim) corpo.dataFim = valorParaIso(fim);
    }

    await seguro(async () => {
      const r = await api('/sessoes', { metodo: 'POST', corpo });
      if (!r.ok) return aviso(mensagemDeErro(r, 'Erro ao abrir a sessão.'), 'erro');
      estado.sessoes.set(r.dados.id, r.dados);
      renderSessoes();
      aviso(`Sessão ${agendar ? 'agendada' : 'aberta'} com sucesso para "${r.dados.tituloPauta}".`);
      evento.target.reset();
      $('#sessao-agenda').hidden = true;
    }, botaoDoFormulario(evento));
  }

  async function consultarSessao(evento) {
    evento.preventDefault();
    const id = Number($('#sessao-busca-id').value);
    if (!Number.isInteger(id) || id < 1) return aviso('Informe um ID de sessão válido.', 'erro');
    await seguro(async () => {
      const r = await api(`/sessoes/${id}`);
      if (!r.ok || !r.dados) return aviso(mensagemDeErro(r, 'Sessão não encontrada.'), 'erro');
      estado.sessoes.set(r.dados.id, r.dados);
      renderSessoes();
    }, botaoDoFormulario(evento));
  }

  /* ---------- votação ---------- */

  async function registrarVoto(evento) {
    evento.preventDefault();
    const pautaId = Number($('#voto-pauta').value);
    const associadoId = Number($('#voto-associado').value);
    const escolhido = $('input[name="opcao"]:checked');
    if (!pautaId) return aviso('Selecione uma pauta.', 'erro');
    if (!associadoId) return aviso('Selecione um associado.', 'erro');
    if (!escolhido) return aviso('Escolha entre Sim e Não.', 'erro');

    await seguro(async () => {
      const r = await api('/votos', { metodo: 'POST', corpo: { pautaId, associadoId, opcao: escolhido.value } });
      if (!r.ok) return aviso(mensagemDeErro(r, 'Erro ao registrar o voto.'), 'erro');
      const quem = r.dados.nomeAssociado || `Associado #${associadoId}`;
      aviso(`Voto registrado: ${quem} votou ${escolhido.value === 'SIM' ? 'SIM' : 'NÃO'}.`);
      escolhido.checked = false;
    }, botaoDoFormulario(evento));
  }

  /* ---------- resultados ---------- */

  function linhaDeBarra(rotulo, votos, total, variante) {
    const percentual = total ? Math.round((votos * 100) / total) : 0;
    const preenchimento = h('div', { class: `bar__fill bar__fill--${variante}` });
    // A largura é aplicada pelo CSSOM (permitido pela CSP, ao contrário de style="" no HTML).
    requestAnimationFrame(() => {
      preenchimento.style.width = `${percentual}%`;
    });
    return h(
      'div',
      { class: 'bar-row' },
      h('div', { class: 'bar-row__label' }, h('span', { text: rotulo }), h('span', { text: `${votos} (${percentual}%)` })),
      h('div', { class: 'bar', role: 'img', 'aria-label': `${rotulo}: ${percentual}%` }, preenchimento),
    );
  }

  const classeDoResultado = (resultado) =>
    ({ APROVADA: 'badge--ok', REPROVADA: 'badge--erro', EMPATE: 'badge--warn' })[resultado] || '';

  function renderResultado(dados) {
    const total = Number(dados.totalVotos) || 0;
    const sim = Number(dados.votosSim) || 0;
    const nao = Number(dados.votosNao) || 0;
    $('#resultado-area').replaceChildren(
      h(
        'div',
        { class: 'resultado' },
        h(
          'div',
          { class: 'resultado__top' },
          h('h2', { class: 'resultado__titulo', text: dados.tituloPauta || 'Pauta' }),
          h('span', { class: `badge resultado__veredito ${classeDoResultado(dados.resultado)}`, text: dados.resultado || 'Indefinido' }),
        ),
        linhaDeBarra('Sim', sim, total, 'sim'),
        linhaDeBarra('Não', nao, total, 'nao'),
        h('p', { class: 'resultado__total', text: `Total de votos: ${total}` }),
      ),
    );
  }

  const mostrarAvisoNoResultado = (mensagem, erro = false) =>
    $('#resultado-area').replaceChildren(h('p', { class: `aviso${erro ? ' aviso--erro' : ''}`, text: mensagem }));

  async function consultarResultado(evento) {
    evento.preventDefault();
    const pautaId = Number($('#resultado-pauta').value);
    if (!pautaId) return aviso('Selecione uma pauta.', 'erro');
    await seguro(async () => {
      const r = await api(`/votos/resultado/${pautaId}`);
      if (!r.ok || !r.dados) {
        return mostrarAvisoNoResultado(mensagemDeErro(r, 'Não foi possível consultar o resultado.'), r.status >= 500);
      }
      renderResultado(r.dados);
    }, botaoDoFormulario(evento));
  }

  /* ---------- tema claro/escuro ---------- */

  function alternarTema() {
    const raiz = document.documentElement;
    const atual = raiz.getAttribute('data-theme') || (matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    const proximo = atual === 'dark' ? 'light' : 'dark';
    raiz.setAttribute('data-theme', proximo);
    try {
      localStorage.setItem('tema', proximo);
    } catch {
      // sem armazenamento local: o tema vale apenas até recarregar a página
    }
  }

  /* ---------- inicialização ---------- */

  function iniciar() {
    $('#alternar-tema').addEventListener('click', alternarTema);

    $('#form-associado').addEventListener('submit', cadastrarAssociado);
    $('#busca-associado').addEventListener('submit', buscarAssociado);
    $('#assoc-mostrar-todos').addEventListener('click', () => seguro(carregarAssociados));
    $('#assoc-cpf').addEventListener('input', (e) => {
      e.target.value = mascararCpf(e.target.value);
    });

    $('#form-pauta').addEventListener('submit', cadastrarPauta);
    $('#busca-pauta').addEventListener('submit', buscarPauta);
    $('#pauta-mostrar-todas').addEventListener('click', () => seguro(carregarPautas));

    $('#form-sessao').addEventListener('submit', abrirSessao);
    $('#busca-sessao').addEventListener('submit', consultarSessao);
    $('#sessao-agendar').addEventListener('change', (e) => {
      $('#sessao-agenda').hidden = !e.target.checked;
    });

    $('#form-voto').addEventListener('submit', registrarVoto);
    $('#form-resultado').addEventListener('submit', consultarResultado);

    window.addEventListener('hashchange', () => mostrarView(viewDaUrl()));
    mostrarView(viewDaUrl());

    atualizarSelects();
    renderSessoes();
    seguro(() => Promise.all([carregarAssociados(), carregarPautas()]));
    setInterval(tiqueDasSessoes, 1000);
  }

  document.addEventListener('DOMContentLoaded', iniciar);
})();
