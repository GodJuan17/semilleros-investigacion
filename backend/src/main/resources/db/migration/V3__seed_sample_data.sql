-- =====================================================
-- Datos de ejemplo: 5 directores, 6 líneas, 8 semilleros y 3-6 integrantes por semillero
-- =====================================================

-- 1. Directores (5)
INSERT INTO directores (nombre, correo) VALUES
    ('Dra. Ana María Pérez', 'ana.perez@universidad.edu.co'),
    ('Dr. Carlos Eduardo Martínez', 'carlos.martinez@universidad.edu.co'),
    ('Mg. Laura Sofía Gómez', 'laura.gomez@universidad.edu.co'),
    ('Dr. Andrés Felipe Rojas', 'andres.rojas@universidad.edu.co'),
    ('Mg. Diana Carolina Torres', 'diana.torres@universidad.edu.co')
ON CONFLICT (correo) DO NOTHING;

-- 2. Líneas de investigación (6)
INSERT INTO lineas (nombre) VALUES
    ('Inteligencia Artificial y Ciencia de Datos'),
    ('Ingeniería de Software y Arquitecturas Cloud'),
    ('Ciberseguridad y Redes'),
    ('Internet de las Cosas y Robótica'),
    ('Computación Gráfica y Realidad Virtual'),
    ('Transformación Digital y Analítica Empresarial')
ON CONFLICT (nombre) DO NOTHING;

-- 3. Semilleros (8)
INSERT INTO semilleros (nombre, sigla, grupo_investigacion, mision, vision, objetivo_general, director_id) VALUES
    (
        'Semillero de Inteligencia Artificial Aplicada',
        'SIAA',
        'Grupo de Sistemas Inteligentes',
        'Fomentar la investigación formativa en modelos de aprendizaje automático y procesamiento de lenguaje natural.',
        'Ser referente regional en soluciones de inteligencia artificial ética para 2030.',
        'Desarrollar prototipos basados en IA orientados a resolver problemas académicos e industriales.',
        (SELECT id FROM directores WHERE correo = 'ana.perez@universidad.edu.co')
    ),
    (
        'Semillero de Ciencia de Datos y Analítica',
        'SCDA',
        'Grupo de Sistemas Inteligentes',
        'Capacitar estudiantes en análisis estadístico, minería de datos y visualización interactiva.',
        'Consolidarse como laboratorio estudiantil líder en analítica predictiva.',
        'Aplicar técnicas de ciencia de datos en conjuntos de datos abiertos y empresariales.',
        (SELECT id FROM directores WHERE correo = 'ana.perez@universidad.edu.co')
    ),
    (
        'Semillero de Arquitectura y Microservicios',
        'SAM',
        'Grupo de Ingeniería de Software',
        'Explorar patrones modernos de diseño, arquitectura limpia y despliegue en la nube.',
        'Formar arquitectos de software con sólidos criterios de calidad y escalabilidad.',
        'Construir aplicaciones distribuidas resilientes siguiendo buenas prácticas DevOps.',
        (SELECT id FROM directores WHERE correo = 'carlos.martinez@universidad.edu.co')
    ),
    (
        'Semillero de Calidad y Pruebas de Software',
        'SCPS',
        'Grupo de Ingeniería de Software',
        'Promover la cultura de automatización de pruebas y aseguramiento de calidad.',
        'Integrar prácticas de QA continuo en los proyectos de desarrollo de la facultad.',
        'Implementar marcos de pruebas automatizadas para sistemas web y móviles.',
        (SELECT id FROM directores WHERE correo = 'carlos.martinez@universidad.edu.co')
    ),
    (
        'Semillero de Seguridad Informática y Hacking Ético',
        'SIHE',
        'Grupo de Redes y Seguridad',
        'Investigar vulnerabilidades, criptografía aplicada y defensa de infraestructuras críticas.',
        'Ser reconocido en competencias nacionales de ciberseguridad CTF.',
        'Evaluar y fortalecer la postura de seguridad de aplicaciones y redes institucionales.',
        (SELECT id FROM directores WHERE correo = 'laura.gomez@universidad.edu.co')
    ),
    (
        'Semillero de Robótica y Sistemas Embebidos',
        'SRSE',
        'Grupo de Automatización e IoT',
        'Diseñar dispositivos conectados y soluciones robóticas de bajo costo.',
        'Impulsar proyectos de agricultura inteligente y automatización urbana.',
        'Integrar sensores, microcontroladores y plataformas en la nube para monitoreo en tiempo real.',
        (SELECT id FROM directores WHERE correo = 'andres.rojas@universidad.edu.co')
    ),
    (
        'Semillero de Videojuegos y Realidad Extendida',
        'SVRX',
        'Grupo de Computación Visual',
        'Desarrollar experiencias interactivas educativas mediante motores gráficos y VR/AR.',
        'Posicionarse como incubadora de talento creativo en computación gráfica.',
        'Crear simuladores y videojuegos serios enfocados en aprendizaje inmersivo.',
        (SELECT id FROM directores WHERE correo = 'andres.rojas@universidad.edu.co')
    ),
    (
        'Semillero de Innovación y Transformación Digital',
        'SITD',
        'Grupo de Gestión Tecnológica',
        'Acompañar procesos de digitalización e innovación en pequeñas y medianas empresas.',
        'Contribuir al ecosistema emprendedor regional mediante tecnología aplicada.',
        'Diseñar soluciones digitales centradas en el usuario para optimizar procesos de negocio.',
        (SELECT id FROM directores WHERE correo = 'diana.torres@universidad.edu.co')
    )
ON CONFLICT (sigla) DO NOTHING;

-- 4. Relación Semillero - Líneas
INSERT INTO semillero_lineas (semillero_id, linea_id) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SIAA'), (SELECT id FROM lineas WHERE nombre = 'Inteligencia Artificial y Ciencia de Datos')),
    ((SELECT id FROM semilleros WHERE sigla = 'SIAA'), (SELECT id FROM lineas WHERE nombre = 'Transformación Digital y Analítica Empresarial')),
    ((SELECT id FROM semilleros WHERE sigla = 'SCDA'), (SELECT id FROM lineas WHERE nombre = 'Inteligencia Artificial y Ciencia de Datos')),
    ((SELECT id FROM semilleros WHERE sigla = 'SCDA'), (SELECT id FROM lineas WHERE nombre = 'Transformación Digital y Analítica Empresarial')),
    ((SELECT id FROM semilleros WHERE sigla = 'SAM'),  (SELECT id FROM lineas WHERE nombre = 'Ingeniería de Software y Arquitecturas Cloud')),
    ((SELECT id FROM semilleros WHERE sigla = 'SAM'),  (SELECT id FROM lineas WHERE nombre = 'Ciberseguridad y Redes')),
    ((SELECT id FROM semilleros WHERE sigla = 'SCPS'), (SELECT id FROM lineas WHERE nombre = 'Ingeniería de Software y Arquitecturas Cloud')),
    ((SELECT id FROM semilleros WHERE sigla = 'SIHE'), (SELECT id FROM lineas WHERE nombre = 'Ciberseguridad y Redes')),
    ((SELECT id FROM semilleros WHERE sigla = 'SIHE'), (SELECT id FROM lineas WHERE nombre = 'Ingeniería de Software y Arquitecturas Cloud')),
    ((SELECT id FROM semilleros WHERE sigla = 'SRSE'), (SELECT id FROM lineas WHERE nombre = 'Internet de las Cosas y Robótica')),
    ((SELECT id FROM semilleros WHERE sigla = 'SRSE'), (SELECT id FROM lineas WHERE nombre = 'Inteligencia Artificial y Ciencia de Datos')),
    ((SELECT id FROM semilleros WHERE sigla = 'SVRX'), (SELECT id FROM lineas WHERE nombre = 'Computación Gráfica y Realidad Virtual')),
    ((SELECT id FROM semilleros WHERE sigla = 'SVRX'), (SELECT id FROM lineas WHERE nombre = 'Ingeniería de Software y Arquitecturas Cloud')),
    ((SELECT id FROM semilleros WHERE sigla = 'SITD'), (SELECT id FROM lineas WHERE nombre = 'Transformación Digital y Analítica Empresarial')),
    ((SELECT id FROM semilleros WHERE sigla = 'SITD'), (SELECT id FROM lineas WHERE nombre = 'Ingeniería de Software y Arquitecturas Cloud'))
ON CONFLICT (semillero_id, linea_id) DO NOTHING;

-- 5. Integrantes (entre 3 y 6 por semillero)
-- SIAA (4 integrantes)
INSERT INTO integrantes (semillero_id, codigo, nombre, programa, es_lider) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SIAA'), '20231001', 'Valentina Ruiz Castro', 'Ingeniería de Sistemas', TRUE),
    ((SELECT id FROM semilleros WHERE sigla = 'SIAA'), '20231002', 'Sebastián Mora Díaz', 'Ingeniería de Sistemas', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SIAA'), '20231003', 'Camila Andrea Vega', 'Ciencia de Datos', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SIAA'), '20231004', 'Mateo Ospina León', 'Matemáticas Aplicadas', FALSE);

-- SCDA (3 integrantes)
INSERT INTO integrantes (semillero_id, codigo, nombre, programa, es_lider) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SCDA'), '20232001', 'Daniela Pardo Gil', 'Ciencia de Datos', TRUE),
    ((SELECT id FROM semilleros WHERE sigla = 'SCDA'), '20232002', 'Juan Pablo Herrera', 'Estadística', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SCDA'), '20232003', 'Sofía Navarro Cruz', 'Ingeniería Industrial', FALSE);

-- SAM (5 integrantes)
INSERT INTO integrantes (semillero_id, codigo, nombre, programa, es_lider) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SAM'), '20233001', 'Santiago Mejía Ortiz', 'Ingeniería de Software', TRUE),
    ((SELECT id FROM semilleros WHERE sigla = 'SAM'), '20233002', 'Mariana López Soto', 'Ingeniería de Software', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SAM'), '20233003', 'Nicolás Vargas Peña', 'Ingeniería de Sistemas', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SAM'), '20233004', 'Isabella Cortés Ramírez', 'Ingeniería de Sistemas', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SAM'), '20233005', 'Tomás Silva Mendoza', 'Ingeniería de Software', FALSE);

-- SCPS (4 integrantes)
INSERT INTO integrantes (semillero_id, codigo, nombre, programa, es_lider) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SCPS'), '20234001', 'Paula Andrea Jiménez', 'Ingeniería de Software', TRUE),
    ((SELECT id FROM semilleros WHERE sigla = 'SCPS'), '20234002', 'David Alejandro Ríos', 'Ingeniería de Sistemas', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SCPS'), '20234003', 'Lucía Fernanda Bravo', 'Tecnología en Desarrollo de Software', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SCPS'), '20234004', 'Esteban Quintero Arias', 'Ingeniería de Software', FALSE);

-- SIHE (6 integrantes)
INSERT INTO integrantes (semillero_id, codigo, nombre, programa, es_lider) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SIHE'), '20235001', 'Gabriel Muñoz Acosta', 'Ingeniería en Seguridad Informática', TRUE),
    ((SELECT id FROM semilleros WHERE sigla = 'SIHE'), '20235002', 'Natalia Castro Duarte', 'Ingeniería de Telecomunicaciones', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SIHE'), '20235003', 'Samuel Cardona Reyes', 'Ingeniería de Sistemas', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SIHE'), '20235004', 'Valeria Suárez Pineda', 'Ingeniería en Seguridad Informática', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SIHE'), '20235005', 'Felipe Benítez Cano', 'Ingeniería de Telecomunicaciones', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SIHE'), '20235006', 'Juliana Salgado Mora', 'Ingeniería de Sistemas', FALSE);

-- SRSE (3 integrantes)
INSERT INTO integrantes (semillero_id, codigo, nombre, programa, es_lider) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SRSE'), '20236001', 'Miguel Ángel Lozano', 'Ingeniería Electrónica', TRUE),
    ((SELECT id FROM semilleros WHERE sigla = 'SRSE'), '20236002', 'Sara Manuela Nieto', 'Ingeniería Mecatrónica', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SRSE'), '20236003', 'Cristian Camilo Bernal', 'Ingeniería Electrónica', FALSE);

-- SVRX (4 integrantes)
INSERT INTO integrantes (semillero_id, codigo, nombre, programa, es_lider) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SVRX'), '20237001', 'Alejandra Franco Molina', 'Ingeniería Multimedia', TRUE),
    ((SELECT id FROM semilleros WHERE sigla = 'SVRX'), '20237002', 'Kevin Stiven Romero', 'Ingeniería Multimedia', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SVRX'), '20237003', 'Manuela Echeverri Toro', 'Diseño Digital', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SVRX'), '20237004', 'Julián David Montoya', 'Ingeniería de Sistemas', FALSE);

-- SITD (5 integrantes)
INSERT INTO integrantes (semillero_id, codigo, nombre, programa, es_lider) VALUES
    ((SELECT id FROM semilleros WHERE sigla = 'SITD'), '20238001', 'Carolina Patiño Vargas', 'Ingeniería Industrial', TRUE),
    ((SELECT id FROM semilleros WHERE sigla = 'SITD'), '20238002', 'Óscar Iván Castaño', 'Administración de Empresas', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SITD'), '20238003', 'Laura Daniela Guzmán', 'Ingeniería de Sistemas', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SITD'), '20238004', 'Brayan Alexis Cárdenas', 'Ingeniería Industrial', FALSE),
    ((SELECT id FROM semilleros WHERE sigla = 'SITD'), '20238005', 'María José Rincón', 'Diseño de Experiencia de Usuario', FALSE);
