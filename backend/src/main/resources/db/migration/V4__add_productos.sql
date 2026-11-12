CREATE TABLE productos (
    id           BIGSERIAL    PRIMARY KEY,
    semillero_id BIGINT       NOT NULL REFERENCES semilleros(id),
    nombre       VARCHAR(200) NOT NULL,
    descripcion  TEXT         NOT NULL,
    tipo         VARCHAR(60)  NOT NULL
);

CREATE TABLE producto_integrantes (
    producto_id   BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    integrante_id BIGINT NOT NULL REFERENCES integrantes(id),
    PRIMARY KEY (producto_id, integrante_id)
);

-- =====================================================
-- Productos de ejemplo según tipología Minciencias
-- =====================================================
INSERT INTO productos (semillero_id, nombre, descripcion, tipo) VALUES
    (
        (SELECT id FROM semilleros WHERE sigla = 'SIAA'),
        'Asistente Virtual Académico con RAG',
        'Plataforma web basada en modelos de lenguaje y generación aumentada por recuperación para orientar a estudiantes de primer semestre.',
        'SOFTWARE'
    ),
    (
        (SELECT id FROM semilleros WHERE sigla = 'SIAA'),
        'Detección temprana de deserción estudiantil mediante Random Forest',
        'Artículo publicado en revista indexada sobre modelos predictivos de retención universitaria en programas de ingeniería.',
        'ARTICULO_INVESTIGACION'
    ),
    (
        (SELECT id FROM semilleros WHERE sigla = 'SAM'),
        'Arquitectura de Microservicios para Gestión de Laboratorios',
        'Sistema distribuido con Spring Boot, Docker y mensajería asíncrona para la reserva de equipos académicos.',
        'SOFTWARE'
    ),
    (
        (SELECT id FROM semilleros WHERE sigla = 'SIHE'),
        'Guía Práctica de Hardening para Servidores Universitarios',
        'Documento de trabajo con lineamientos de seguridad, auditoría y respuesta ante incidentes en infraestructura Linux.',
        'DOCUMENTO_TRABAJO'
    ),
    (
        (SELECT id FROM semilleros WHERE sigla = 'SRSE'),
        'Estación IoT de Monitoreo Agroclimático de Bajo Costo',
        'Prototipo funcional con sensores de humedad, temperatura y radiación solar conectado a tablero en la nube.',
        'PROTOTIPO_INDUSTRIAL'
    ),
    (
        (SELECT id FROM semilleros WHERE sigla = 'SVRX'),
        'Simulador VR de Seguridad Industrial en Laboratorios',
        'Aplicación de realidad virtual inmersiva para entrenamiento en protocolos de seguridad en laboratorios de ingeniería.',
        'CONTENIDO_DIGITAL'
    );

-- Asociación de integrantes participantes a cada producto
INSERT INTO producto_integrantes (producto_id, integrante_id) VALUES
    (
        (SELECT id FROM productos WHERE nombre = 'Asistente Virtual Académico con RAG'),
        (SELECT id FROM integrantes WHERE codigo = '20231001')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Asistente Virtual Académico con RAG'),
        (SELECT id FROM integrantes WHERE codigo = '20231002')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Detección temprana de deserción estudiantil mediante Random Forest'),
        (SELECT id FROM integrantes WHERE codigo = '20231001')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Detección temprana de deserción estudiantil mediante Random Forest'),
        (SELECT id FROM integrantes WHERE codigo = '20231003')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Detección temprana de deserción estudiantil mediante Random Forest'),
        (SELECT id FROM integrantes WHERE codigo = '20231004')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Arquitectura de Microservicios para Gestión de Laboratorios'),
        (SELECT id FROM integrantes WHERE codigo = '20233001')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Arquitectura de Microservicios para Gestión de Laboratorios'),
        (SELECT id FROM integrantes WHERE codigo = '20233002')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Arquitectura de Microservicios para Gestión de Laboratorios'),
        (SELECT id FROM integrantes WHERE codigo = '20233003')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Guía Práctica de Hardening para Servidores Universitarios'),
        (SELECT id FROM integrantes WHERE codigo = '20235001')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Guía Práctica de Hardening para Servidores Universitarios'),
        (SELECT id FROM integrantes WHERE codigo = '20235004')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Estación IoT de Monitoreo Agroclimático de Bajo Costo'),
        (SELECT id FROM integrantes WHERE codigo = '20236001')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Estación IoT de Monitoreo Agroclimático de Bajo Costo'),
        (SELECT id FROM integrantes WHERE codigo = '20236002')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Simulador VR de Seguridad Industrial en Laboratorios'),
        (SELECT id FROM integrantes WHERE codigo = '20237001')
    ),
    (
        (SELECT id FROM productos WHERE nombre = 'Simulador VR de Seguridad Industrial en Laboratorios'),
        (SELECT id FROM integrantes WHERE codigo = '20237003')
    );

