CREATE TABLE mensagens_processadas (
    id BIGSERIAL PRIMARY KEY,
    message_id VARCHAR(255) NOT NULL UNIQUE,
    tipo VARCHAR(50) NOT NULL,
    processado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_mensagens_message_id ON mensagens_processadas(message_id);
