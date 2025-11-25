
CREATE TABLE access_request (
    id UUID PRIMARY KEY,
    protocol VARCHAR(255),
    requester_id UUID,
    justification VARCHAR(255),
    urgent BOOLEAN NOT NULL,
    status request_status_enum,
    created_at TIMESTAMP,
    expires_at TIMESTAMP,
    denial_reason VARCHAR(255),
    CONSTRAINT fk_access_request_user FOREIGN KEY (requester_id) REFERENCES users(id)
);

-- ManyToMany: access_request.modules
CREATE TABLE access_request_modules (
    access_request_id UUID NOT NULL,
    modules_id UUID NOT NULL,
    PRIMARY KEY (access_request_id, modules_id),
    CONSTRAINT fk_req_mod_request FOREIGN KEY (access_request_id) REFERENCES access_request(id),
    CONSTRAINT fk_req_mod_module FOREIGN KEY (modules_id) REFERENCES modules(id)
);