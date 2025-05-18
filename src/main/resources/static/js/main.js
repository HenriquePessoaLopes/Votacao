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
    
    const usarAgendamento = document.getElementById('usarAgendamento').checked;
    
    if (usarAgendamento) {
        const dataInicio = document.getElementById('dataInicio').value;
        const horaInicio = document.getElementById('horaInicio').value;
        
        if (dataInicio && horaInicio) {
            sessao.dataInicio = `${dataInicio}T${horaInicio}:00`;
        } else {
            mostrarMensagem('Informe a data e hora de início para agendar a sessão', false);
            return;
        }
        
        const dataFim = document.getElementById('dataFim').value;
        const horaFim = document.getElementById('horaFim').value;
        
        if (dataFim && horaFim) {
            sessao.dataFim = `${dataFim}T${horaFim}:00`;
        }
    }

    try {
        const response = await fetch('/api/v1/sessoes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sessao)
        });

        if (response.ok) {
            mostrarMensagem(`Sessão ${usarAgendamento ? 'agendada' : 'aberta'} com sucesso!`, true);
            document.getElementById('sessaoForm').reset();
            document.getElementById('agendamentoFields').style.display = 'none';
        } else {
            const errorData = await response.json();
            mostrarMensagem(errorData.mensagem || `Erro ao ${usarAgendamento ? 'agendar' : 'abrir'} sessão`, false);
        }
    } catch (error) {
        console.error('Erro:', error);
        mostrarMensagem(`Erro ao ${usarAgendamento ? 'agendar' : 'abrir'} sessão`, false);
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

async function consultarResultado() {
    const pautaId = document.getElementById('pautaResultadoSelect').value;
    if (!pautaId) {
        mostrarMensagem('Selecione uma pauta', false);
        return;
    }

    try {
        const response = await fetch(`/api/v1/votos/resultado/${pautaId}`);
        
        if (!response.ok) {
            const errorData = await response.json();
            mostrarMensagem(errorData.mensagem || 'Erro ao consultar resultado', false);
            document.getElementById('resultado').innerHTML = '';
            return;
        }
        
        const data = await response.json();
        
        const resultado = {
            tituloPauta: data.tituloPauta || 'Título não disponível',
            totalVotos: data.totalVotos != null ? data.totalVotos : 0,
            votosSim: data.votosSim != null ? data.votosSim : 0,
            votosNao: data.votosNao != null ? data.votosNao : 0,
            resultado: data.resultado || 'Indefinido'
        };
        
        let resultadoHtml = `
            <div class="data-item">
                <h3>${resultado.tituloPauta}</h3>
                <strong>Total de votos:</strong> ${resultado.totalVotos}<br>
                <strong>Votos SIM:</strong> ${resultado.votosSim}<br>
                <strong>Votos NÃO:</strong> ${resultado.votosNao}<br>
                <strong>Resultado:</strong> ${resultado.resultado}
            </div>
        `;
        
        document.getElementById('resultado').innerHTML = resultadoHtml;
    } catch (error) {
        console.error('Erro ao consultar resultado:', error);
        mostrarMensagem('Erro ao consultar resultado. Verifique o console para detalhes.', false);
        document.getElementById('resultado').innerHTML = '';
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

async function buscarSessao() {
    const id = document.getElementById('sessaoId').value;
    if (!id) {
        mostrarMensagem('Informe um ID de sessão', false);
        return;
    }

    try {
        const response = await fetch(`/api/v1/sessoes/${id}`);
        
        if (!response.ok) {
            const errorData = await response.json();
            mostrarMensagem(errorData.mensagem || 'Sessão não encontrada', false);
            document.getElementById('sessaoResult').innerHTML = '';
            return;
        }
        
        const sessao = await response.json();
        
        const dataAbertura = new Date(sessao.dataAbertura).toLocaleString();
        const dataFechamento = new Date(sessao.dataFechamento).toLocaleString();
        
        const agora = new Date();
        const abertura = new Date(sessao.dataAbertura);
        const fechamento = new Date(sessao.dataFechamento);
        
        let status;
        if (agora < abertura) {
            status = "AGENDADA";
        } else if (agora > fechamento) {
            status = "ENCERRADA";
        } else {
            status = "ABERTA";
        }
        
        document.getElementById('sessaoResult').innerHTML = `
            <div class="data-item">
                <strong>ID:</strong> ${sessao.id}<br>
                <strong>Pauta:</strong> ${sessao.tituloPauta} (ID: ${sessao.pautaId})<br>
                <strong>Abertura:</strong> ${dataAbertura}<br>
                <strong>Fechamento:</strong> ${dataFechamento}<br>
                <strong>Status:</strong> <span class="status-${status.toLowerCase()}">${status}</span>
            </div>
        `;
    } catch (error) {
        console.error('Erro ao buscar sessão:', error);
        mostrarMensagem('Erro ao buscar sessão. Verifique o console para detalhes.', false);
        document.getElementById('sessaoResult').innerHTML = '';
    }
}