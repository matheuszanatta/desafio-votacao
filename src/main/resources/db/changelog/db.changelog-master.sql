-- liquibase formatted sql

-- changeset matheus.zanatta:01-create-table-associado
CREATE TABLE associado
(
    id   bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    cpf  varchar(11)        NOT NULL,
    nome varchar(100)       NOT NULL,
    CONSTRAINT uk_cpf UNIQUE (cpf)
);

-- changeset matheus.zanatta:02-create-table-pauta
CREATE TABLE pauta
(
    id        bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    descricao varchar(200)       NOT NULL,
    titulo    varchar(100)       NOT NULL
);

-- changeset matheus.zanatta:03-create-table-sessao
CREATE TABLE sessao
(
    id           bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    data_final   timestamp          NOT NULL,
    data_inicial timestamp          NOT NULL,
    pauta_id     bigint             NOT NULL,
    enviada      boolean            NOT NULL,
    CONSTRAINT fk_sessao_pauta_id
        FOREIGN KEY (pauta_id) REFERENCES pauta (id)
);

-- changeset matheus.zanatta:04-create-table-voto
CREATE TABLE voto
(
    id             bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
    voto_computado varchar(3)         NOT NULL,
    associado_id   bigint             NOT NULL,
    pauta_id       bigint             NOT NULL,
    CONSTRAINT ck_voto_computado CHECK (voto_computado IN ('SIM', 'NAO')),
    CONSTRAINT uk_associado_pauta UNIQUE (associado_id, pauta_id),
    CONSTRAINT fk_voto_associado_id
        FOREIGN KEY (associado_id) REFERENCES associado (id),
    CONSTRAINT fk_voto_pauta_id
        FOREIGN KEY (pauta_id) REFERENCES pauta (id)
);
