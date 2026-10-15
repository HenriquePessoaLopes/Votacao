# Sistema de Votação 

## ❤️ Objetivo do Projeto  
O sistema de votação foi desenvolvido como uma API REST utilizando Spring Boot para gerenciar sessões de votação em pautas específicas. O objetivo principal é permitir que associados cadastrados possam votar durante o período em que a sessão estiver aberta, garantindo a integridade e transparência do processo de votação.

## 🛠️ Funcionalidades  
- **Cadastro de associados: Gerenciamento completo dos associados com validação de CPF**  
- **Cadastro de pautas: Criação e consulta de pautas para votação**
- **Abertura de sessão de votação**: Possibilidade de abrir uma sessão com tempo determinado (padrão: 1 minuto)*
- **Receber votos dos associados: Cada associado pode votar uma única vez por pauta (Sim/Não)**
- **Contabilizar os votos: Exibição dos resultados após o encerramento da sessão**

## 🚀 Tecnologias Utilizadas  
- **Java** (Spring Boot)    
- **Spring Data JPA**: Para persistência de dados
- **H2 Database**: Para ambiente de desenvolvimento (PostgreSQL suportado)
- **Swagger / OpenAPI (springdoc)**: Documentação interativa dos endpoints
- **HTML, CSS e JavaScript**: Front-end responsivo servido pela própria aplicação
- **JUnit 5, Mockito e MockMvc**: Testes unitários e de ponta a ponta
- **Lombok**: Para redução de código boilerplate
- **Controle de versão**: Git e GitHub

##  🖌️Modelo de Domínio Votação
![image](https://github.com/user-attachments/assets/8adf3516-7d14-43c2-b748-bcabd40d6326)

# 📚 Arquitetura e Decisões Técnicas

## Arquitetura em Camadas

O sistema foi organizado seguindo uma estrutura em camadas:

- **Controllers:** Responsáveis por receber requisições HTTP e delegar às camadas de serviço.
- **Services:** Contêm a lógica de negócio e orquestram as operações.
- **Repositories:** Gerenciam o acesso aos dados e persistência.
- **Models:** Representam as entidades do domínio.
- **DTOs:** Objetos para transferência de dados entre camadas.

---

## Modelo de Domínio

O domínio do sistema foi modelado com as seguintes entidades principais:

- **Associado:** Representa um membro que pode votar em pautas.
- **Pauta:** O tema que será votado.
- **Sessão:** Define o período em que uma pauta está disponível para votação.
- **Voto:** Registra o voto de um associado em uma pauta específica.

---

## Validações e Regras de Negócio

Regras implementadas para garantir a integridade do sistema:

- CPF único para associados.
- Verificação se a sessão está aberta antes de aceitar votos.
- Restrição de um único voto por associado em cada pauta.
- Tempo configurável para duração da sessão (padrão: 1 minuto).
- Contabilização de votos somente após o encerramento da sessão.

---

## Padrões de Projeto

- **Repository Pattern:** Abstração da camada de acesso a dados.
- **DTO Pattern:** Para transferência segura de dados entre camadas.
- **Service Layer:** Encapsulamento da lógica de negócio.

---

## API REST

Foram implementados endpoints RESTful para as seguintes operações:

- **Associados:** CRUD de associados.
- **Pautas:** Criação e consulta de pautas.
- **Sessões:** Abertura de sessão para uma pauta específica.
- **Votos:** Registro de votos e consulta de resultados.

## 🧾 Endpoints de Pautas

### ➕ Criar Pauta **POST** `http://localhost:8080/api/v1/pautas`

#### Exemplo:
```json
{
  "titulo": "Aprovação",
  "descricao": "Votação para aprovar o orçamento do próximo ano fiscal"
}
```

### 📋 Listar Pautas **GET** `http://localhost:8080/api/v1/pautas`

### 🔍 Buscar Pauta por ID **GET** `http://localhost:8080/api/v1/pautas/1`

---

## 🧾 Endpoints de Sessões

### ➕ Abrir Sessão **POST** `http://localhost:8080/api/v1/sessoes`

#### Exemplo:
```json
{
  "pautaId": 1,
  "minutos": 5
}
```

### 🔍 Buscar Sessão por ID **GET** `http://localhost:8080/api/v1/sessoes/1`

---

## 🧾 Endpoints de Associados

### ➕ Criar Associado **POST** `http://localhost:8080/api/v1/associados`

#### Exemplo:
```json
{
  "nome": "João da Silva",
  "cpf": "12345678901"
}
```

### 📋 Listar Associados **GET** `http://localhost:8080/api/v1/associados`

### 🔍 Buscar Associado por ID **GET** `http://localhost:8080/api/v1/associados/1`

---

## 🧾 Endpoints de Votos

### ➕ Registrar Voto **POST** `http://localhost:8080/api/v1/votos`

### 📊 Consultar Resultado de Votação **GET** `http://localhost:8080/api/v1/votos/resultado/1`

---

## 🧾 Validação de CPF

### 🔍 Validar CPF **GET** `http://localhost:8080/api/v1/cpf/validar/12345678902`


---
## ⚙️ Como Rodar o Projeto

Este projeto foi desenvolvido em **Java 21** com **Spring Boot 3.4.3**, banco de dados **H2** em memória e um front-end em **HTML, CSS e JavaScript** servido pela própria aplicação.
Para executá-lo, utilize o terminal da sua IDE (VS Code, IntelliJ etc.) aberto na pasta raiz do projeto.

### ✅ Pré-requisitos
- **Java 21 (LTS)** — confira com `java -version`.
- **Git** — para clonar o repositório.
- **Maven não precisa ser instalado**: o projeto já inclui o Maven Wrapper (`mvnw`).

### 🚀 Passo a passo (Windows PowerShell)

**1. Clone o repositório**

```powershell
git clone https://github.com/HenriquePessoaLopes/Votacao.git
cd Votacao
```

**2. Suba a aplicação**

```powershell
.\mvnw.cmd clean spring-boot:run
```

Aguarde até aparecer no terminal a mensagem `Started VotacaoApplication`. A partir daí a API está no ar em `http://localhost:8080`.

**3. Execute os testes automatizados**

Pare a aplicação (`Ctrl + C` no terminal) e rode:

```powershell
.\mvnw.cmd clean test
```

O resultado esperado é `Tests run: 9, Failures: 0, Errors: 0` seguido de `BUILD SUCCESS`.

**4. Limpar o cache de compilação**

Antes de repetir os testes ou subir a aplicação novamente, apague os arquivos compilados anteriormente:

```powershell
.\mvnw.cmd clean
```

> Os comandos acima já incluem o `clean`, então ele é executado automaticamente. Use-o isoladamente apenas quando quiser limpar o projeto sem rodar mais nada.
> Isso evita que classes antigas ou geradas pela compilação automática da IDE causem erros na execução.
> No VS Code, o projeto já traz o arquivo `.vscode/settings.json` com a compilação automática do Java desativada, para evitar esse conflito.

No Linux/macOS use `./mvnw` no lugar de `.\mvnw.cmd`.

### 🌐 Como testar a aplicação

> ⚠️ **A aplicação precisa estar em execução (passo 2) para os endereços abaixo funcionarem.**

| O que | Endereço |
|---|---|
| **Swagger UI** — testar os endpoints da API | http://localhost:8080/swagger-ui/index.html |
| **Front-end** — interface web do sistema | http://localhost:8080 |
| Especificação OpenAPI (JSON) | http://localhost:8080/api-docs |
| Console do banco H2 | http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:testdb`, usuário `sa`, senha vazia) |

Os endpoints foram testados pelo **Swagger**: abra o link no navegador, escolha o endpoint, clique em **Try it out**, preencha o corpo da requisição e clique em **Execute**.
O front-end permite executar o mesmo fluxo de forma visual: cadastrar associados e pautas, abrir sessões, votar e consultar o resultado.

> O banco H2 é em memória: os dados são apagados toda vez que a aplicação reinicia.

### 🖥️ Front-end

A interface web fica em `src/main/resources/static` e é servida pela própria aplicação, sem precisar instalar Node, npm ou qualquer dependência. É feita com **HTML, CSS e JavaScript puro**.

| Tela | O que faz |
|---|---|
| **Painel** | Mostra os totais de associados, pautas e sessões abertas, e o passo a passo do fluxo de votação. |
| **Associados** | Cadastra associados (com máscara de CPF), lista todos e busca por ID. |
| **Pautas** | Cadastra pautas, lista, busca por ID e atalhos para abrir sessão ou ver o resultado. |
| **Sessões** | Abre ou agenda sessões de votação e acompanha o status e a contagem regressiva ao vivo. |
| **Votação** | Registra o voto SIM ou NÃO de um associado em uma pauta. |
| **Resultados** | Exibe o veredito (aprovada, reprovada ou empate) com barras de percentual. |

**Segurança do front-end**
- **Proteção contra XSS:** nenhum dado vindo da API ou do usuário é inserido como HTML. O JavaScript não usa `innerHTML`; nomes, títulos e descrições entram sempre como texto. Um nome como `<script>alert(1)</script>` aparece escrito na tela e nunca é executado.
- **Content Security Policy (CSP):** o `index.html` define uma política que só permite scripts e estilos do próprio servidor, bloqueando scripts embutidos e de terceiros.
- **Sem dependências externas:** nenhuma biblioteca ou fonte é carregada de CDN.
- **Validação no cliente:** nome e título obrigatórios, CPF com 11 dígitos e limite de 255 caracteres nos campos de texto (a validação final continua na API).

**Usabilidade:** tema claro e escuro (segue o sistema e pode ser alternado), layout responsivo para celular, navegação por teclado e rótulos de acessibilidade.

### 🔁 Fluxo de uso
1. `POST /api/v1/associados` — cadastrar um associado.
2. `POST /api/v1/pautas` — cadastrar uma pauta.
3. `POST /api/v1/sessoes` — abrir a sessão da pauta (`minutos`, padrão 1).
4. `POST /api/v1/votos` — votar `SIM` ou `NAO` enquanto a sessão estiver aberta.
5. `GET /api/v1/votos/resultado/{pautaId}` — consultar o resultado **depois** que a sessão encerrar.

### 🧪 Testes automatizados

A suíte tem 9 testes:
- **Teste de ponta a ponta** (`FluxoVotacaoIntegrationTest`): cadastra associado e pauta, abre a sessão, vota, e verifica que voto duplicado e segunda sessão na mesma pauta retornam `409 Conflict`.
- **Testes unitários** (`VotoServiceTest`, com Mockito): voto em sessão encerrada, voto duplicado, contagem antes do encerramento, pauta aprovada e empate.

### 🗄️ Usar PostgreSQL em vez do H2
O driver do PostgreSQL já está no `pom.xml`. Para usá-lo, substitua as propriedades `spring.datasource.*` e `spring.jpa.database-platform` em `src/main/resources/application.properties` pelos dados do seu banco.

