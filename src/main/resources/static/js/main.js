document.addEventListener('DOMContentLoaded', () => {
    carregarPautas();
    carregarAssociados();
    listarAssociados();
    listarPautas();
});

async function carregarPautas() {
    try {
        const response = await fetch('/api/v1/pautas');
        const pautas = await response.json();
        preencherSelectPautas(pautas);
    } catch (error) {
        console.error('Erro ao carregar pautas:', error);
    }
}

async function carregarAssociados() {
    try {
        const response = await fetch('/api/v1/associados');
        const associados = await response.json();
        preencherSelectAssociados(associados);
    } catch (error) {
        console.error('Erro ao carregar associados:', error);
    }
}

function preencherSelectPautas(pautas) {
    const pautaSelect = document.getElementById('pautaSelect');
    const pautaResultadoSelect = document.getElementById('pautaResultadoSelect');
    const pautaSessaoSelect = document.getElementById('pautaSessaoSelect');
    
   
    pautaSelect.innerHTML = '<option value="">Selecione uma pauta</option>';
    pautaResultadoSelect.innerHTML = '<option value="">Selecione uma pauta</option>';
    pautaSessaoSelect.innerHTML = '<option value="">Selecione uma pauta</option>';
    
    pautas.forEach(pauta => {
        const option = new Option(pauta.titulo, pauta.id);
        pautaSelect.add(option.cloneNode(true));
        pautaResultadoSelect.add(option.cloneNode(true));
        pautaSessaoSelect.add(option);
    });
}

function preencherSelectAssociados(associados) {
    const associadoSelect = document.getElementById('associadoSelect');
    associadoSelect.innerHTML = '<option value="">Selecione um associado</option>';
    
    associados.forEach(associado => {
        const option = new Option(associado.nome, associado.id);
        associadoSelect.add(option);
    });
}


document.getElementById('associadoForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const associado = {
        nome: document.getElementById('nome').value,
        cpf: document.getElementById('cpf').value
    };

    try {
        const response = await fetch('/api/v1/associados', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(associado)
        });

        if (response.ok) {
            mostrarMensagem('Associado cadastrado com sucesso!', true);
            document.getElementById('associadoForm').reset();
            carregarAssociados();
            listarAssociados();
        } else {
            const error = await response.text();
            mostrarMensagem('Erro ao cadastrar associado: ' + error, false);
        }
    } catch (error) {
        console.error('Erro:', error);
        mostrarMensagem('Erro ao cadastrar associado', false);
    }
});

document.getElementById('pautaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const pauta = {
        titulo: document.getElementById('titulo').value,
        descricao: document.getElementById('descricao').value
    };

    try {
        const response = await fetch('/api/v1/pautas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(pauta)
        });

        if (response.ok) {
            mostrarMensagem('Pauta cadastrada com sucesso!', true);
            document.getElementById('pautaForm').reset();
            carregarPautas();
            listarPautas();
        } else {
            mostrarMensagem('Erro ao cadastrar pauta', false);
        }
    } catch (error) {
        console.error('Erro:', error);
        mostrarMensagem('Erro ao cadastrar pauta', false);
    }
});

document.getElementById('sessaoForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const sessao = {
        pautaId: document.getElementById('pautaSessaoSelect').value,
        minutos: document.getElementById('minutos').value || 1
    };

    try {
        const response = await fetch('/api/v1/sessoes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sessao)
        });

        if (response.ok) {
            mostrarMensagem('Sessão aberta com sucesso!', true);
            document.getElementById('sessaoForm').reset();
        } else {
            const error = await response.text();
            mostrarMensagem('Erro ao abrir sessão: ' + error, false);
        }
    } catch (error) {
        console.error('Erro:', error);
        mostrarMensagem('Erro ao abrir sessão', false);
    }
});

document.getElementById('votacaoForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const voto = {
        pautaId: document.getElementById('pautaSelect').value,
        associadoId: document.getElementById('associadoSelect').value,
        opcao: document.getElementById('votoSelect').value
    };

    try {
        const response = await fetch('/api/v1/votos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(voto)
        });

        if (response.ok) {
            mostrarMensagem('Voto registrado com sucesso!', true);
            document.getElementById('votacaoForm').reset();
        } else {
            const error = await response.text();
            mostrarMensagem('Erro ao registrar voto: ' + error, false);
        }
    } catch (error) {
        console.error('Erro:', error);
        mostrarMensagem('Erro ao registrar voto', false);
    }
});

async function listarAssociados() {
    try {
        const response = await fetch('/api/v1/associados');
        const associados = await response.json();
        
        const listaDiv = document.getElementById('listaAssociados');
        listaDiv.innerHTML = associados.map(associado => `
            <div class="data-item">
                <strong>ID:</strong> ${associado.id}<br>
                <strong>Nome:</strong> ${associado.nome}<br>
                <strong>CPF:</strong> ${associado.cpf}
            </div>
        `).join('');
    } catch (error) {
        mostrarMensagem('Erro ao listar associados', false);
    }
}

async function buscarAssociadoPorId() {
    const id = document.getElementById('buscarAssociadoId').value;
    if (!id) {
        mostrarMensagem('Informe um ID', false);
        return;
    }

    try {
        const response = await fetch(`/api/v1/associados/${id}`);
        const associado = await response.json();
        
        const listaDiv = document.getElementById('listaAssociados');
        if (response.ok) {
            listaDiv.innerHTML = `
                <div class="data-item">
                    <strong>ID:</strong> ${associado.id}<br>
                    <strong>Nome:</strong> ${associado.nome}<br>
                    <strong>CPF:</strong> ${associado.cpf}
                </div>
            `;
        } else {
            mostrarMensagem('Associado não encontrado', false);
        }
    } catch (error) {
        mostrarMensagem('Erro ao buscar associado', false);
    }
}

async function listarPautas() {
    try {
        const response = await fetch('/api/v1/pautas');
        const pautas = await response.json();
        
        const listaDiv = document.getElementById('listaPautas');
        listaDiv.innerHTML = pautas.map(pauta => `
            <div class="data-item">
                <strong>ID:</strong> ${pauta.id}<br>
                <strong>Título:</strong> ${pauta.titulo}<br>
                <strong>Descrição:</strong> ${pauta.descricao || 'N/A'}
            </div>
        `).join('');
    } catch (error) {
        mostrarMensagem('Erro ao listar pautas', false);
    }
}

async function buscarPautaPorId() {
    const id = document.getElementById('buscarPautaId').value;
    if (!id) {
        mostrarMensagem('Informe um ID', false);
        return;
    }

    try {
        const response = await fetch(`/api/v1/pautas/${id}`);
        const pauta = await response.json();
        
        const listaDiv = document.getElementById('listaPautas');
        if (response.ok) {
            listaDiv.innerHTML = `
                <div class="data-item">
                    <strong>ID:</strong> ${pauta.id}<br>
                    <strong>Título:</strong> ${pauta.titulo}<br>
                    <strong>Descrição:</strong> ${pauta.descricao || 'N/A'}
                </div>
            `;
        } else {
            mostrarMensagem('Pauta não encontrada', false);
        }
    } catch (error) {
        mostrarMensagem('Erro ao buscar pauta', false);
    }
}

async function buscarSessao() {
    const sessaoId = document.getElementById('sessaoId').value;
    if (!sessaoId) {
        mostrarMensagem('Informe o ID da sessão', false);
        return;
    }

    try {
        const response = await fetch(`/api/v1/sessoes/${sessaoId}`);
        const sessao = await response.json();
        
        const resultDiv = document.getElementById('sessaoResult');
        if (response.ok) {
            resultDiv.innerHTML = `
                <div class="data-item">
                    <strong>Sessão #${sessao.id}</strong><br>
                    <strong>Pauta:</strong> ${sessao.pauta ? sessao.pauta.titulo : 'N/A'}<br>
                    <strong>Abertura:</strong> ${new Date(sessao.dataAbertura).toLocaleString()}<br>
                    <strong>Fechamento:</strong> ${new Date(sessao.dataFechamento).toLocaleString()}<br>
                    <strong>Status:</strong> ${sessao.aberta ? 'Aberta' : 'Fechada'}
                </div>
            `;
            resultDiv.style.display = 'block';
        } else {
            mostrarMensagem('Sessão não encontrada', false);
        }
    } catch (error) {
        mostrarMensagem('Erro ao buscar sessão', false);
    }
}

async function consultarResultado() {
    const pautaId = document.getElementById('pautaResultadoSelect').value;
    if (!pautaId) {
        mostrarMensagem('Selecione uma pauta', false);
        return;
    }

    try {
        const response = await fetch(`/api/v1/votos/resultado/${pautaId}`);
        const resultado = await response.json();
        
        document.getElementById('resultado').innerHTML = `
            <div class="data-item">
                <h3>${resultado.tituloPauta}</h3>
                <strong>Total de votos:</strong> ${resultado.totalVotos}<br>
                <strong>Votos SIM:</strong> ${resultado.votosSim}<br>
                <strong>Votos NÃO:</strong> ${resultado.votosNao}<br>
                <strong>Resultado:</strong> ${resultado.resultado}
            </div>
        `;
    } catch (error) {
        mostrarMensagem('Erro ao consultar resultado', false);
    }
}

function mostrarMensagem(mensagem, sucesso) {
    Toastify({
        text: mensagem,
        duration: 3000,
        gravity: "top",
        position: "right",
        backgroundColor: sucesso ? "#28a745" : "#dc3545",
        stopOnFocus: true
    }).showToast();
}