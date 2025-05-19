-- Crear usuario específico para la aplicación
CREATE USER taller_user WITH PASSWORD '123';

-- Crear base de datos
CREATE DATABASE gestion_notas OWNER taller_user;

-- Conceder todos los privilegios
GRANT ALL PRIVILEGES ON DATABASE gestion_notas TO taller_user;

-- Conectarse a la nueva base de datos
\c gestion_notas

-- Crear tablas
CREATE TABLE estudiante (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE materia (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    creditos INTEGER NOT NULL CHECK (creditos > 0)
);

CREATE TABLE nota (
    id SERIAL PRIMARY KEY,
    materia_id INTEGER REFERENCES materia(id),
    estudiante_id INTEGER REFERENCES estudiante(id),
    observacion VARCHAR(200),
    valor NUMERIC(3,2) CHECK (valor >= 0 AND valor <= 5),
    porcentaje NUMERIC(4,2) CHECK (porcentaje > 0 AND porcentaje <= 100)
);


-- Conceder permisos al usuario
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO taller_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO taller_user;

-- Insertar estudiantes
INSERT INTO estudiante (nombre, apellido, correo) VALUES 
('Juan', 'Pérez', 'juan.perez@example.com'),
('María', 'Gómez', 'maria.gomez@example.com');

-- Insertar materias
INSERT INTO materia (nombre, creditos) VALUES
('Matemáticas', 4),
('Programación Reactiva', 3);

-- Insertar notas
INSERT INTO nota (materia_id, estudiante_id, valor, porcentaje, observacion) VALUES
(1, 1, 4.5, 30.0, 'Primer parcial'),
(1, 1, 3.8, 30.0, 'Segundo parcial'),
(2, 2, 4.2, 40.0, 'Examen final');