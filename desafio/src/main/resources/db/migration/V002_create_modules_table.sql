-- ===========================================
-- TABLE: modules
-- ===========================================
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