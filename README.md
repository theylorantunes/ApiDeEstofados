#  API Gestão de Estofados - Antunes Interiores

API RESTful desenvolvida em Spring Boot para o gerenciamento de catálogos de estofados premium. O sistema integra estofados, fabricantes, materiais e especificações técnicas através de relacionamentos complexos de banco de dados.

## Tecnologias Utilizadas
* **Java 17** & **Spring Boot 3** (Web, Data JPA, Validation)
* **Spring HATEOAS** (Maturidade Richardson Nível 3)
* **Banco de Dados H2** (Em memória para homologação)
* **OpenAPI 3 / Swagger** (Documentação automatizada)

## Padrões Arquiteturais Implementados
* **DTO Pattern:** Encapsulamento de dados nas requisições e respostas usando `Records`.
* **Global Exception Handling:** Tratamento centralizado de erros com `@ControllerAdvice` e exceções customizadas.
* **Respostas HTTP Padronizadas:** Uso estrito de códigos de status (`200 OK`, `201 Created`, `204 No Content`, `404 Not Found`).
* **HATEOAS:** Separação de responsabilidades utilizando `RepresentationModelAssembler` para injeção de hipermídia.
* **Prevenção de Referência Circular:** Uso de `@JsonIgnore` na serialização do Jackson.

## Configuração e Execução
1. Clone este repositório.
2. Certifique-se de possuir o JDK 17+ e o Maven instalados.
3. Execute a aplicação a partir da classe principal ou via comando: `mvn spring-boot:run`.
4. A API estará acessível em `http://localhost:8080`.
5. O banco de dados é populado automaticamente via `CommandLineRunner` (Classe `LoadDatabase`).
6. A interface visual de testes (Swagger) fica em: `http://localhost:8080/swagger-ui/index.html`.
   Markdown
## 🛠️ Passo a Passo: Cadastrando um Estofado Completo
Devido à arquitetura relacional da base de dados, a criação de um Estofado exige que suas dependências (Foreign Keys) já existam no sistema. Siga esta ordem exata no Swagger ou Thunder Client/Postman:

### Passo 1: Criar a Categoria
A categoria define o ambiente de uso do móvel.
* **POST** `/categorias`
```JSON
{
  "nomeCategoria": "Área Externa",
  "descricaoUso": "Móveis com resistência a intempéries"
}
```

(Anote o categoriaId gerado na resposta, ex: 1)

## Passo 2: Criar o Fabricante
O fabricante requer uma categoria associada.

POST /fabricantes

```JSON
{
  "nomeFabricante": "ArtFibras",
  "cnpj": "98765432000199",
  "cidadeSede": "Belo Horizonte",
  "categoriaId": 1
}
```
(Anote o fabricanteId gerado, ex: 1)

## Passo 3: Criar a Especificação Técnica
Dados estruturais do estofado (Relação One-to-One).

POST /especificacoes

```JSON
{
  "dimensoes": "3.00m x 1.10m",
  "pesoSuportadoKg": 400,
  "densidadeEspuma": "D-28 Soft"
}
```
(Anote o especificacaoId gerado, ex: 1)

## Passo 4: Criar os Materiais (Componentes)
Crie quantos materiais forem necessários (Relação Many-to-Many).

POST /materiais

```JSON
{
  "nomeMaterial": "Corda Náutica",
  "tipoParte": "Revestimento Externo"
}
```
(Anote os materialId gerados, ex: 1 e 2)

## Passo 5: Criar o Estofado (O Gran Finale)
Agora, com todos os IDs em mãos, amarre o ecossistema criando a entidade principal. O array materiaisIds permite adicionar múltiplos materiais de uma vez.

POST /estofados

```JSON
{
  "nomeModelo": "Sofá Modular Acapulco",
  "preco": 8500.00,
  "corPrincipal": "Terracota",
  "status": "SOB_MEDIDA",
  "fabricanteId": 1,
  "especificacaoId": 1,
  "materiaisIds": [1, 2]
}
```