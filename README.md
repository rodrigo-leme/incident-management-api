# Incident Management API

API ReST (Back-End) para o sistema de gestão de ocorrências escolares — Checkpoint 6 de Arquitetura SOA e Web Services.

## Integrante

- Rodrigo Leme — RM550266

## Tecnologias

- Java 17
- Spring Boot 3 (Web, Data JPA, Validation, Security)
- JWT (auth0 java-jwt)
- Banco H2 em memória

## Como executar

```bash
mvn spring-boot:run
```

A API sobe em `http://localhost:8090` (mesma porta usada pelo Front-End).

O banco H2 é criado em memória a cada execução, já com os perfis, categorias, tipos de ocorrência e um usuário de cada perfil (arquivo `src/main/resources/data.sql`).

## Usuários iniciais

Senha de todos: `123456`

| Login | Perfil |
|---|---|
| professor@fiap.com.br | PROFESSOR |
| analista@fiap.com.br | ANALISTA |
| coordenador@fiap.com.br | COORDENADOR |
| administrativo@fiap.com.br | ADMINISTRATIVO |

## Autenticação

`POST /login` com `{ "login": "...", "password": "..." }` retorna `{ tokenJWT, id, login }`.
Nas demais requisições, envie o header `Authorization: Bearer <tokenJWT>`.

## Endpoints

### Usuários — Administrativo
| Método | Rota | Descrição |
|---|---|---|
| POST | /users | Cadastrar usuário |
| GET | /users | Listar usuários ativos (paginado) |
| GET | /users/{id} | Consultar usuário |
| GET | /users/roles | Listar perfis |
| PUT | /users | Atualizar nome e login |
| PATCH | /users | Atualizar perfil |
| DELETE | /users/{id} | Inativar usuário |
| PATCH | /users/password | Alterar a própria senha (qualquer usuário logado) |

### Alunos — Administrativo (consultas: qualquer usuário logado)
| Método | Rota | Descrição |
|---|---|---|
| POST | /students | Cadastrar aluno |
| GET | /students | Listar alunos ativos (paginado) |
| GET | /students/{id} | Consultar aluno |
| PUT | /students | Atualizar aluno |
| PATCH | /students/{id}/image | Enviar foto (multipart, campo `image`) |
| DELETE | /students/{id} | Inativar aluno |

### Turmas — Administrativo (consultas: qualquer usuário logado)
| Método | Rota | Descrição |
|---|---|---|
| POST | /schoolclasses | Cadastrar turma |
| GET | /schoolclasses | Listar turmas ativas (paginado) |
| GET | /schoolclasses/canceled | Listar turmas canceladas (paginado) |
| GET | /schoolclasses/{id} | Consultar turma |
| GET | /schoolclasses/shifts | Listar turnos |
| GET | /schoolclasses/semesters | Listar semestres |
| PUT | /schoolclasses | Atualizar turma |
| DELETE | /schoolclasses/{id} | Cancelar turma |
| POST | /schoolclasses/{id}/students/{idStudent} | Vincular aluno à turma |
| GET | /schoolclasses/{id}/students | Listar alunos da turma |

### Categorias e tipos de ocorrência — Administrativo (consultas: qualquer usuário logado)
| Método | Rota | Descrição |
|---|---|---|
| POST | /incidents/categories | Cadastrar categoria |
| GET | /incidents/categories | Listar categorias |
| POST | /incidents/types | Cadastrar tipo `{ name, category }` |
| GET | /incidents/types/{category} | Listar tipos da categoria |

### Ocorrências
| Método | Rota | Descrição | Perfil |
|---|---|---|---|
| POST | /incidents | Registrar ocorrência | Professor |
| PUT | /incidents | Alterar a própria ocorrência | Professor |
| DELETE | /incidents/{id} | Excluir a própria ocorrência | Professor |
| GET | /incidents | Listar ocorrências (paginado) | Professor, Analista, Coordenador |
| GET | /incidents/{id} | Detalhar ocorrência | Professor, Analista, Coordenador |
| GET | /incidents/search | Consulta/relatório com filtros `idStudent`, `idSchoolClass`, `category`, `type`, `startDate`, `endDate` (dd/MM/yyyy) | Professor, Analista, Coordenador |
| GET | /incidents/status | Listar status | Professor, Analista, Coordenador |
| GET | /incidents/status/{group} | Listar por status: `waiting`, `progressing`, `active`, `solved`, `unsolved`, `closed` | Professor, Analista, Coordenador |
| PATCH | /incidents/status | Alterar status | Coordenador |
| GET | /incidents/summary | Dados do dashboard | Coordenador, Analista |

Formatos de data: `dd/MM/yyyy` (nascimento) e `dd/MM/yyyy - HH:mm` (registro da ocorrência).
