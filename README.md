
# 🛋️ API Antunes Interiores - Gestão de Estofados Premium

A **API Antunes Interiores** é uma interface RESTful corporativa desenvolvida para a gestão e catálogo de estofados sob medida. Projetada com arquitetura limpa, segurança robusta e altíssima maturidade técnica.

## 🚀 Arquitetura e Tecnologias

Este projeto foi construído seguindo rigorosos padrões de engenharia de software:

* **Maturidade Richardson Nível 3 (HATEOAS):** Respostas hipermidia com links dinâmicos (`_links`) para navegação autônoma pela API.
* **Segurança e Rate Limiting:** Proteção de endpoints com `X-API-Key` e limite restrito de requisições (5 req/min) utilizando algoritmo Token Bucket via **Bucket4j**.
* **Idempotência Restrita:** Controle de transações seguras em rotas de mutação (`POST`) através do cabeçalho `X-Idempotency-Key`, com sistema de cache de respostas via `ConcurrentHashMap`.
* **Data Transfer Objects (DTOs):** Encapsulamento de dados utilizando Java Records e validação rigorosa via **Jakarta Bean Validation**.
* **Global Exception Handling:** Tratamento unificado de erros com padrão RFC, garantindo que o cliente nunca receba stacktraces expostos, e sim respostas no formato `ErroPadrao` (`400`, `404`, `409`, `429`, `500`).

## ☁️ Acesso à Produção (Deploy)
A API está hospedada e totalmente funcional em ambiente de nuvem (Render).
* **URL Base da API:** `https://apideestofados.onrender.com`
* **Documentação Interativa (Swagger):** 👉 **[https://apideestofados.onrender.com/documentacao](https://apideestofados.onrender.com/documentacao)**

## ⚙️ Como Executar Localmente
1. Clone o repositório e acesse a pasta: `git clone https://github.com/theylorantunes/ApiDeEstofados.git`
2. Execute o projeto via Maven: `./mvnw spring-boot:run`
3. A documentação local estará em: `http://localhost:8080/documentacao`

## 📖 Documentação Interativa (Swagger)

A API possui documentação auto-gerada e estilizada. Após subir o projeto, acesse:
👉 **[http://localhost:8080/documentacao](http://localhost:8080/documentacao)**

## 🛡️ Autenticação

Todas as requisições (exceto a geração de chaves) requerem o envio do cabeçalho de autenticação:
`X-API-Key: sua-chave-gerada-aqui`

## 🔗 Endpoints Principais

| Método | Endpoint | Descrição | Segurança |
| --- | --- | --- | --- |
| `POST` | `/auth/key` | Gera uma nova API Key | Aberto |
| `GET` | `/estofados` | Lista estofados de forma paginada | `X-API-Key` |
| `POST` | `/estofados` | Cadastra estofado | `X-API-Key` + `X-Idempotency-Key` |
| `GET` | `/categorias` | Lista categorias e ambientes | `X-API-Key` |
| `GET` | `/fabricantes` | Consulta fabricantes parceiros | `X-API-Key` |

---

## 📖 Passo a Passo: Cadastrando um Estofado Completo

Devido à arquitetura relacional, a criação de um Estofado exige que as suas dependências (Foreign Keys) já existam no sistema. Segue esta ordem exata no Swagger:

### Passo 1: Criar a Categoria
A categoria define o ambiente de uso do móvel.
* **POST** `/categorias`
```json
{
  "nomeCategoria": "Área Externa",
  "descricaoUso": "Móveis com resistência a intempéries"
}
```
(Anota o ID gerado na resposta, ex: 1)

### Passo 2: Criar o Fabricante
O fabricante requer uma categoria associada.

* **POST** `/fabricantes`

```json
{
  "nomeFabricante": "ArtFibras",
  "cnpj": "98765432000199",
  "cidadeSede": "Belo Horizonte",
  "categoriaId": 1
}
```

(Anota o ID gerado, ex: 1)

### Passo 3: Criar a Especificação Técnica
Dados estruturais do estofado.

* **POST** `/especificacoes`

````json
{
  "dimensoes": "3.00m x 1.10m",
  "pesoSuportadoKg": 400,
  "densidadeEspuma": "D-28 Soft"
}
````
(Anota o ID gerado, ex: 1)

### Passo 4: Criar os Materiais (Componentes)
Cria os materiais que compõem o móvel. Podes criar vários.

* **POST** `/materiais`

````json
{
  "nomeMaterial": "Corda Náutica",
  "tipoParte": "Revestimento Externo"
}
````
(Anota o IDs gerados, ex: 1 e 2)

### Passo 4: Criar os Materiais (Componentes)
Cria os materiais que compõem o móvel. Podes criar vários.

* **POST** `/estofados`
* **HEADER OBRIGATÓRIO:** `X-Idempotency-Key` (Define um valor único, ex: `req-001`)

````json
{
   "nomeModelo": "Sofá Modular Acapulco",
   "preco": 8500.00,
   "corPrincipal": "Terracota",
   "status": "SOB_MEDIDA",
   "fabricanteId": 1,
   "especificacaoId": 1,
   "materiaisIds": [1, 2]
}
````





*Desenvolvido por Theylor Antunes Cruz - 2026*