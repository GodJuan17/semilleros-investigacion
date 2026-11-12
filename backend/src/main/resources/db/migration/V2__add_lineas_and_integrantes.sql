CREATE TABLE lineas (
    id     BIGSERIAL    PRIMARY KEY,
    nombre VARCHAR(160) NOT NULL UNIQUE
);

CREATE TABLE semillero_lineas (
    semillero_id BIGINT NOT NULL REFERENCES semilleros(id),
    linea_id     BIGINT NOT NULL REFERENCES lineas(id),
    PRIMARY KEY (semillero_id, linea_id)
);

CREATE TABLE integrantes (
    id           BIGSERIAL    PRIMARY KEY,
    semillero_id BIGINT       NOT NULL REFERENCES semilleros(id),
    codigo       VARCHAR(40)  NOT NULL,
    nombre       VARCHAR(160) NOT NULL,
    programa     VARCHAR(160) NOT NULL,
    es_lider     BOOLEAN      NOT NULL DEFAULT FALSE
);

