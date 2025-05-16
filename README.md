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
- **H2 Database**: Para ambiente de desenvolvimento
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

### 📋 Listar Associados **GET** `http://localhost:8080/api/v1/associados`

---

## 🧾 Endpoints de Votos

### ➕ Registrar Voto **POST** `http://localhost:8080/api/v1/votos`

### 📊 Consultar Resultado de Votação **GET** `http://localhost:8080/api/v1/votos/resultado/1`

---

## 🧾 Validação de CPF

### 🔍 Validar CPF **GET** `http://localhost:8080/api/v1/cpf/validar/12345678902`


---
## ⚙️ Como Configurar o Projeto  

Siga os passos abaixo para configurar e rodar o projeto localmente.

### ✅ Pré-requisitos  
Certifique-se de ter as seguintes ferramentas instaladas em sua máquina:  
1. [**Java 21LTS+**](https://www.oracle.com/java/technologies/javase-downloads.html)  
- Certifique-se de que o Java esteja no **PATH** da sua máquina.  
- Verifique a instalação executando no terminal:  
```bash
     java -version
```  
2. [**Maven**](https://maven.apache.org/download.cgi)  
- Após a instalação, verifique se o Maven está no **PATH**:  
```bash
     mvn -version
```  

3. [**Git**](https://git-scm.com/downloads)  
- Necessário para clonar o repositório.
```bash
   mvn -version
```  

4. IDE de sua escolha:  
   - Recomendado: [**Visual Studio Code**](https://code.visualstudio.com/) ou [**IntelliJ**](https://www.jetbrains.com/pt-br/idea/).  

---

### 🚀 Passo a Passo para Rodar o Projeto  

1. **Clone o Repositório**  
Execute o seguinte comando no terminal para clonar o projeto:  
```bash
   git clone https://github.com/UnifacolFaculdade/Votacao
```
   
3. Instale as Dependências
No diretório do projeto, execute o comando para baixar as dependências:
```bash
   mvn clean install
```
3.Execute o Projeto
Para iniciar o servidor Spring Boot, rode:
```bash
mvn spring-boot:run
```
