# Termos de Uso e Política de Privacidade - Antunes Interiores API
**Versão:** 1.0 | **Data de Atualização:** Outubro de 2026

## 1. Escopo e Propósito
A "API Gestão de Estofados" (doravante "Serviço") é uma interface de programação de aplicações (API) RESTful fornecida para integração, consulta e mutação de dados de catálogos de design de interiores. O uso do Serviço implica na aceitação integral destes termos pelas aplicações clientes ou desenvolvedores ("Usuários").

## 2. Política de Privacidade e Tratamento de Dados (LGPD)
* **Volatilidade dos Dados:** No atual ambiente de homologação, os dados são processados através de um banco de dados em memória (H2). Nenhuma informação enviada será persistida permanentemente após a reinicialização da instância do servidor.
* **Dados de Terceiros:** É expressamente proibido o envio de dados sensíveis ou informações de identificação pessoal (PII) que não estejam publicamente disponíveis, como dados sigilosos de fabricantes ou especificações protegidas por propriedade intelectual, sem o consentimento dos titulares.

## 3. Diretrizes de Uso Técnico e Rate Limiting
Para manter a integridade arquitetural e disponibilidade do Serviço:
* **Taxa de Requisição:** O Serviço aplica um limite de 1.000 requisições por minuto (RPM) por endpoint. Requisições subsequentes receberão o código HTTP `429 Too Many Requests`.
* **CORS e Integração:** A API suporta Cross-Origin Resource Sharing (CORS) configurado para facilitar ambientes de testes no front-end, devendo ser estritamente configurado para domínios de produção antes do deployment (exclusão do wildcard `*`).

## 4. Disponibilidade e Isenção de Responsabilidade
O Serviço é fornecido "no estado em que se encontra" (As-Is). Embora empreguemos padrões arquiteturais modernos (Tratamento de Exceções Global, DTOs), não garantimos o funcionamento ininterrupto da API em ambientes fora do escopo de testes e homologação para os quais foi projetada.