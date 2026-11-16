# Termos de Uso e Política de Privacidade - Antunes Interiores API
**Versão:** 1.0 | **Data de Atualização:** Novembro de 2026

## 1. Escopo e Propósito
A "API Gestão de Estofados" (doravante "Serviço") é uma interface RESTful comercial fornecida sob o modelo SaaS (Software as a Service) para integração, consulta e mutação de dados de catálogos de design de interiores. O uso do Serviço implica a aceitação integral destes termos pelos clientes e desenvolvedores ("Utilizadores").

## 2. Autenticação e Segurança (API Key)
O acesso aos endpoints da API requer uma chave de autenticação válida (`X-API-Key`) enviada no cabeçalho (header) de todas as requisições. A partilha indevida ou o vazamento da chave resultará no bloqueio imediato das credenciais, sendo o titular responsável por qualquer atividade gerada pela sua chave.

## 3. Diretrizes de Uso Técnico e Rate Limiting
Para manter a integridade, a arquitetura e a alta disponibilidade do Serviço para todos os clientes, aplicamos políticas rigorosas de tráfego:
* **Plano Gratuito (Free Tier):** O Serviço aplica um limite de 30 requisições por minuto (RPM) por chave/IP.
* Exceder este limite resultará na recusa temporária do serviço com o código HTTP `429 Too Many Requests`.

## 4. Idempotência e Prevenção de Falhas
De forma a garantir consistência financeira e de inventário, as operações de criação (POST) requerem ou suportam chaves de idempotência (`X-Idempotency-Key`). Isto previne a duplicidade de registos caso ocorram falhas de rede ou reenvios acidentais por parte do cliente.

## 5. Política de Privacidade e Tratamento de Dados (LGPD)
É expressamente proibido o envio de dados sensíveis ou informações de identificação pessoal (PII) que não estejam publicamente disponíveis, bem como especificações protegidas por propriedade intelectual, sem o consentimento expresso dos respetivos titulares.

## 6. Suporte
Para dúvidas técnicas, upgrades de plano para limites superiores de RPM ou problemas de integração, contacte a nossa equipa através do e-mail: suporte@antunesinteriores.com