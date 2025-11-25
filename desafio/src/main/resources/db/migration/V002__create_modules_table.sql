
CREATE TABLE modules (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    active BOOLEAN NOT NULL
);

-- ElementCollection de allowedDepartments
CREATE TABLE modules_allowed_departments (
    modules_id UUID NOT NULL,
    allowed_departments departament_enum NOT NULL,
    CONSTRAINT fk_modules_departments FOREIGN KEY (modules_id) REFERENCES modules(id)
);

-- ManyToMany: incompatibleWith
CREATE TABLE module_incompatibilities (
    module_id UUID NOT NULL,
    incompatible_module_id UUID NOT NULL,
    PRIMARY KEY (module_id, incompatible_module_id),
    CONSTRAINT fk_mod_incompat_1 FOREIGN KEY (module_id) REFERENCES modules(id),
    CONSTRAINT fk_mod_incompat_2 FOREIGN KEY (incompatible_module_id) REFERENCES modules(id)
);

INSERT INTO modules (id, name, description, active) VALUES
('11111111-1111-1111-1111-111111111111', 'Portal do Colaborador', 'Acesso geral para colaboradores', true),
('22222222-2222-2222-2222-222222222222', 'Relatórios Gerenciais', 'Relatórios e dashboards gerenciais', true),
('33333333-3333-3333-3333-333333333333', 'Gestão Financeira', 'Módulo de controle financeiro', true),
('44444444-4444-4444-4444-444444444444', 'Aprovador Financeiro', 'Permite aprovar solicitações financeiras', true),
('55555555-5555-5555-5555-555555555555', 'Solicitante Financeiro', 'Permite solicitar processos financeiros', true),
('66666666-6666-6666-6666-666666666666', 'Administrador RH', 'Gestão completa do setor de RH', true),
('77777777-7777-7777-7777-777777777777', 'Colaborador RH', 'Acesso operacional do RH', true),
('88888888-8888-8888-8888-888888888888', 'Gestão de Estoque', 'Controle de estoque operacional', true),
('99999999-9999-9999-9999-999999999999', 'Compras', 'Gestão de compras e suprimentos', true),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Auditoria', 'Módulo exclusivo para auditoria', true);

-- TODOS os departamentos
INSERT INTO modules_allowed_departments VALUES
('11111111-1111-1111-1111-111111111111', 'FINANCEIRO'),
('11111111-1111-1111-1111-111111111111', 'OPERACOES'),
('11111111-1111-1111-1111-111111111111', 'OUTROS'),
('11111111-1111-1111-1111-111111111111', 'RH'),
('11111111-1111-1111-1111-111111111111', 'TI'),

('22222222-2222-2222-2222-222222222222', 'FINANCEIRO'),
('22222222-2222-2222-2222-222222222222', 'OPERACOES'),
('22222222-2222-2222-2222-222222222222', 'OUTROS'),
('22222222-2222-2222-2222-222222222222', 'RH'),
('22222222-2222-2222-2222-222222222222', 'TI');

-- Financeiro + TI
INSERT INTO modules_allowed_departments VALUES
('33333333-3333-3333-3333-333333333333', 'FINANCEIRO'),
('33333333-3333-3333-3333-333333333333', 'TI'),

('44444444-4444-4444-4444-444444444444', 'FINANCEIRO'),
('44444444-4444-4444-4444-444444444444', 'TI'),

('55555555-5555-5555-5555-555555555555', 'FINANCEIRO'),
('55555555-5555-5555-5555-555555555555', 'TI');

-- RH + TI
INSERT INTO modules_allowed_departments VALUES
('66666666-6666-6666-6666-666666666666', 'RH'),
('66666666-6666-6666-6666-666666666666', 'TI'),

('77777777-7777-7777-7777-777777777777', 'RH'),
('77777777-7777-7777-7777-777777777777', 'TI');

-- Operações + TI
INSERT INTO modules_allowed_departments VALUES
('88888888-8888-8888-8888-888888888888', 'OPERACOES'),
('88888888-8888-8888-8888-888888888888', 'TI'),

('99999999-9999-9999-9999-999999999999', 'OPERACOES'),
('99999999-9999-9999-9999-999999999999', 'TI');

-- Auditoria → apenas TI
INSERT INTO modules_allowed_departments VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'TI');

-- Financeiro incompatíveis
INSERT INTO module_incompatibilities VALUES
('44444444-4444-4444-4444-444444444444', '55555555-5555-5555-5555-555555555555'),
('55555555-5555-5555-5555-555555555555', '44444444-4444-4444-4444-444444444444');

-- RH incompatíveis
INSERT INTO module_incompatibilities VALUES
('66666666-6666-6666-6666-666666666666', '77777777-7777-7777-7777-777777777777'),
('77777777-7777-7777-7777-777777777777', '66666666-6666-6666-6666-666666666666');
