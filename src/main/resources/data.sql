-- Perfis
INSERT INTO roles (name) VALUES ('PROFESSOR');
INSERT INTO roles (name) VALUES ('ANALISTA');
INSERT INTO roles (name) VALUES ('COORDENADOR');
INSERT INTO roles (name) VALUES ('ADMINISTRATIVO');

-- Usuários iniciais (senha de todos: 123456)
INSERT INTO users (name, login, password, active, role_id) VALUES ('Professor', 'professor@fiap.com.br', '$2a$10$mDHwGX66DUoI2sFQYO4fiu2i4os1aKuEiKRUzrDQx1Vxhfpa9Cv.2', true, 1);
INSERT INTO users (name, login, password, active, role_id) VALUES ('Analista', 'analista@fiap.com.br', '$2a$10$mDHwGX66DUoI2sFQYO4fiu2i4os1aKuEiKRUzrDQx1Vxhfpa9Cv.2', true, 2);
INSERT INTO users (name, login, password, active, role_id) VALUES ('Coordenador', 'coordenador@fiap.com.br', '$2a$10$mDHwGX66DUoI2sFQYO4fiu2i4os1aKuEiKRUzrDQx1Vxhfpa9Cv.2', true, 3);
INSERT INTO users (name, login, password, active, role_id) VALUES ('Administrativo', 'administrativo@fiap.com.br', '$2a$10$mDHwGX66DUoI2sFQYO4fiu2i4os1aKuEiKRUzrDQx1Vxhfpa9Cv.2', true, 4);

-- Categorias de ocorrência
INSERT INTO categories (name) VALUES ('DISCIPLINAR');
INSERT INTO categories (name) VALUES ('PEDAGOGICA');
INSERT INTO categories (name) VALUES ('ADMINISTRATIVA');

-- Tipos de ocorrência
INSERT INTO incident_types (name, category_id) VALUES ('INDISCIPLINA', 1);
INSERT INTO incident_types (name, category_id) VALUES ('RENDIMENTO', 2);
INSERT INTO incident_types (name, category_id) VALUES ('ATRASO', 3);
