# Termos de Uso e Política de Segurança - Antunes Interiores API

**Versão:** 2.0.0 (Strict Enforcement) | **Data de Atualização:** Novembro de 2026

## 1. Escopo e Aceitação Irrevogável
A "API Gestão de Estofados" (doravante "Serviço") é uma interface RESTful proprietária fornecida sob o modelo SaaS. O uso de qualquer endpoint, a geração de chaves de API ou o mero envio de requisições ao nosso servidor constitui **aceitação integral, automática e irrevogável** destes termos. Se discorda de qualquer cláusula, interrompa o uso imediatamente.

## 2. Autenticação e Responsabilidade da API Key
O acesso aos endpoints (exceto a rota de geração) exige o cabeçalho `X-API-Key`.
* **Uso Pessoal e Intransferível:** A chave gerada é de uso estritamente individual ou atrelada a uma única aplicação cliente homologada.
* **Tolerância Zero para Vazamentos:** O vazamento, compartilhamento público (ex: commits em repositórios abertos) ou uso simultâneo distribuído de uma mesma chave resultará na **revogação imediata e permanente** das credenciais, sem aviso prévio.

## 3. Rate Limiting e Prevenção de Abuso (Obrigatório)
A arquitetura do Serviço utiliza o algoritmo Token Bucket para garantir alta disponibilidade.
* **Limite Estrito:** O tráfego está restrito a **5 requisições por minuto (RPM)** por chave de API e/ou IP.
* **Evasão de Bloqueio:** É **terminantemente proibido** tentar burlar o limite de 5 RPM através de rotação de IPs, spoofing de cabeçalhos, proxies dinâmicos ou geração automatizada de múltiplas chaves. Tentativas de evasão caracterizam ataque de negação de serviço (DDoS) e resultarão em banimento a nível de firewall (IP Block).

## 4. Condutas Estritamente Proibidas
Sob nenhuma circunstância o usuário/cliente está autorizado a:
1. **Engenharia Reversa e Exploits:** Tentar mapear endpoints ocultos, injetar payloads maliciosos (SQLi, XSS, NoSQLi) ou forçar a quebra do `GlobalExceptionHandler` para expor stacktraces do servidor.
2. **Abuso de Idempotência:** Utilizar a chave `X-Idempotency-Key` (obrigatória em métodos `POST`) para inundar o servidor com requisições repetidas de propósito (Spam de Cache). O mecanismo existe apenas para contingência de rede, não para testes de estresse.
3. **Data Scraping (Raspagem de Dados):** Criar scripts automatizados, botnets ou crawlers para extrair a totalidade do catálogo paginado da aplicação.
4. **Falsificação de Dados (Mocking Abusivo):** Inserir dados ofensivos, links maliciosos ou conteúdo não comercial nos campos de cadastro de Estofados, Fabricantes ou Especificações.

## 5. Tratamento de Dados e LGPD
O Serviço é destinado exclusivamente a dados de catálogo de produtos. É expressamente proibido enviar Informações de Identificação Pessoal (PII), dados financeiros reais ou informações sensíveis de terceiros no corpo das requisições. O Serviço reserva-se o direito de expurgar qualquer dado que viole esta norma.

## 6. Direitos de Rescisão e Monitoramento
A equipe de engenharia da Antunes Interiores monitora ativamente a telemetria, logs de acesso e métricas de consumo da API. Reservamo-nos o direito de **suspender, limitar ou encerrar** o acesso de qualquer cliente que apresente comportamento anômalo, consumo predatório ou violação de qualquer item destas políticas, a nosso exclusivo critério e sem necessidade de notificação prévia.

---
*Para solicitações de aumento de cota (Enterprise Tier) ou auditoria de integração, contacte: admin-security@antunesinteriores.com*