-- =====================================================================

DROP DATABASE IF EXISTS kl_club;
CREATE DATABASE IF NOT EXISTS kl_club;
USE kl_club;

-- ---------------------------------------------------------------------
-- Catálogo
-- ---------------------------------------------------------------------

CREATE TABLE categoria (
    id_categoria INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL UNIQUE,
    ativo TINYINT NOT NULL
);

CREATE TABLE produto (
    id_produto INT PRIMARY KEY AUTO_INCREMENT,
    fk_categoria INT NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NULL,
    ingredientes TEXT NULL,
    preco DECIMAL(10,2) NOT NULL,
    pontuacao INT NOT NULL,
    imagem VARCHAR(500) NULL,
    disponibilidade TINYINT(1) NOT NULL,
    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (fk_categoria) REFERENCES categoria (id_categoria)
);

-- ---------------------------------------------------------------------
-- Cliente (cadastro opcional — tela própria, com opção de pular)
-- ---------------------------------------------------------------------

CREATE TABLE usuario (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL UNIQUE,
    conta_criada DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
-- Pedido (cliente é opcional — pedido pode ser feito sem cadastro)
-- ---------------------------------------------------------------------

CREATE TABLE pedido (
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modalidade ENUM('LOCAL', 'RETIRADA') NOT NULL,
    status_pedido ENUM('PENDENTE', 'PAGO', 'EM_PREPARO', 'PRONTO_PARA_RETIRADA', 'CONCLUIDO', 'CANCELADO') NOT NULL,
    forma_pagamento ENUM('PIX', 'CREDITO', 'DEBITO', 'VOUCHER', 'DINHEIRO') NOT NULL,
    valor_total DECIMAL(10,2)  NOT NULL,
    pontos_gerados INT NOT NULL,
    observacoes VARCHAR(500) NULL,
    fk_usuario INT NULL,
    CONSTRAINT fk_pedido_usuario
        FOREIGN KEY (fk_usuario) REFERENCES usuario (id_usuario),
    -- Dashboard, gestão e histórico filtram por data
    INDEX idx_pedido_data_hora (data_hora)
);

CREATE TABLE item_pedido (
    id_item_pedido INT PRIMARY KEY AUTO_INCREMENT,
    fk_pedido INT NOT NULL,
    fk_produto INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    pontos_unitario INT NOT NULL,
    CONSTRAINT fk_item_pedido_pedido
        FOREIGN KEY (fk_pedido) REFERENCES pedido (id_pedido),
    CONSTRAINT fk_item_pedido_produto
        FOREIGN KEY (fk_produto) REFERENCES produto (id_produto),
    -- Cada produto aparece no máximo uma vez por pedido
    CONSTRAINT uq_item_pedido_produto UNIQUE (fk_pedido, fk_produto)
);

-- ---------------------------------------------------------------------
-- Acesso da dashboard
-- ---------------------------------------------------------------------

CREATE TABLE funcionario (
    id_funcionario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo TINYINT NOT NULL,
    nivel ENUM('ADMINISTRADOR', 'OPERACIONAL') NOT NULL
);