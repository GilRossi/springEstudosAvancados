# Regras Locais do Repositório

## Git Hygiene Obrigatório

Antes de qualquer `git add`, `commit` ou `push`, é obrigatório:

1. Revisar `git status --short` e classificar cada arquivo novo/modificado.
2. Revisar `.gitignore` se houver arquivo novo na raiz ou arquivo de ferramenta local.
3. Tratar como suspeito por padrão qualquer arquivo de agente, IDE, cache, log, secret, output temporário ou configuração local.
4. Não versionar arquivos como `CLAUDE.md`, `.claude/`, `.env*`, logs, settings locais de editor ou artefatos temporários.
5. Se houver dúvida sobre a necessidade de versionar um arquivo, parar e decidir antes do commit.

## Alinhamento com Skills

Estas regras se aplicam a qualquer trabalho neste repositório, inclusive quando a tarefa principal for backend, banco, frontend ou GitHub.

- `backend-specialist`: além da implementação, deve validar higiene de Git quando houver arquivos novos, configs novas ou mudanças de infraestrutura.
- `github-specialist`: deve priorizar revisão de `.gitignore`, arquivos indevidos e higiene do índice antes de commit/push.

## Prioridade de Revisão em Commit

A ordem mínima antes de subir mudanças é:

1. Integridade funcional
2. Testes/validação
3. Higiene de versionamento
4. Clareza do commit

Um commit funcional, mas com arquivos locais indevidos, não está pronto para push.
