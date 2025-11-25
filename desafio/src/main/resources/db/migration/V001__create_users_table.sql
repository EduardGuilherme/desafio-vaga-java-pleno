-- ===========================================
-- ENUMS (PostgreSQL)
-- ===========================================
CREATE TYPE departament_enum AS ENUM ('FINANCEIRO','OPERACOES','OUTROS','RH','TI');
CREATE TYPE request_status_enum AS ENUM ('ATIVO','CANCELADO','NEGADO');

CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    department departament_enum,
    -- activeModules é tratado abaixo como ElementCollection
    CONSTRAINT uk_users_email UNIQUE(email)
);

-- Tabela auxiliar de activeModules (ElementCollection)
CREATE TABLE user_active_modules (
    user_id UUID NOT NULL,
    active_modules VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_modules FOREIGN KEY (user_id) REFERENCES users(id)
);