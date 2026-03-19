CREATE TABLE pedidos (
    id BIGSERIAL PRIMARY KEY,
    produto VARCHAR(255) NOT NULL,
    quantidade INTEGER NOT NULL,
    valor DECIMAL(19,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP
);

CREATE INDEX idx_pedidos_status ON pedidos(status);
CREATE INDEX idx_pedidos_valor ON pedidos(valor);
