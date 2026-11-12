# Guía de Arquitectura del Proyecto (`backend`)

Este archivo define las reglas y convenciones del backend para que cualquier asistente de IA mantenga la consistencia del proyecto al agregar nuevas funcionalidades, entidades o endpoints.

## Stack Tecnológico

- **Lenguaje**: Java 21
- **Framework**: Spring Boot `3.4.2` (Maven)
- **Base de datos**: PostgreSQL (`localhost:5432/semilleros_db`)
- **Migraciones**: Flyway (`backend/src/main/resources/db/migration/`)
- **Persistencia**: Spring Data JPA + Hibernate (`spring.jpa.hibernate.ddl-auto=validate`)
- **Utilidades**: Lombok
- **Documentación API**: Swagger / OpenAPI 3 (`springdoc-openapi-starter-webmvc-ui`)
- **Configuración**: Archivos `.properties` (`application.properties`, no `.yml`)

## Arquitectura Lineal por Capas

El flujo de dependencias es estrictamente lineal y unidireccional:

`Controller` → `Service` → `Repository` → `Entity` (BD)

### 1. Controller (`com.semilleros.backend.controller`)
- Anotado con `@RestController`, `@RequestMapping("/api/<recurso>")`, `@RequiredArgsConstructor` y `@Tag` de Swagger.
- Expone operaciones REST (`GET`, `POST`, `PUT`, `DELETE`) documentadas con `@Operation`.
- **Regla estricta**: Todas las entradas (`@RequestBody` con `@Valid`) y salidas (`ResponseEntity<...>`) usan **DTOs**. Nunca recibir ni retornar entidades JPA directamente.
- Solo delega en la capa `Service`; no contiene lógica de negocio ni acceso a `Repository`.

### 2. Service (`com.semilleros.backend.service`)
- Anotado con `@Service` y `@RequiredArgsConstructor`.
- Contiene las reglas de negocio, validaciones de unicidad/integridad, transformaciones DTO ↔ Entidad y manejo transaccional (`@Transactional` / `@Transactional(readOnly = true)`).
- Usa `ResourceNotFoundException` (HTTP 404) y `BusinessException` (HTTP 409) para errores de dominio.
- Consume uno o más `Repository` (u otros `Service` cuando necesita resolver entidades relacionadas).

### 3. Repository (`com.semilleros.backend.repository`)
- Interfaces que extienden `JpaRepository<Entidad, Long>` anotadas con `@Repository`.
- Declaran métodos derivados de Spring Data JPA para consultas y validaciones (ej. `existsBy...IgnoreCase`, `existsBy...IgnoreCaseAndIdNot`).

### 4. Entity (`com.semilleros.backend.entity`)
- Clases JPA (`@Entity`, `@Table(name = "...")`) con `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`.
- Deben coincidir exactamente con el esquema de Flyway, ya que `ddl-auto=validate` validará columnas, tipos y restricciones al arrancar.
- Relaciones `@ManyToOne` usan `fetch = FetchType.LAZY`.

### 5. DTOs (`com.semilleros.backend.dto`)
- Separados en `<Entidad>RequestDTO` (con validaciones Jakarta `@NotBlank`, `@NotNull`, `@Email`, `@Size` y `@Schema`) y `<Entidad>ResponseDTO` (con `@Schema`).

### 6. Excepciones (`com.semilleros.backend.exception`)
- Manejo centralizado en `GlobalExceptionHandler` (`@RestControllerAdvice`) devolviendo respuestas JSON uniformes con `timestamp`, `status`, `error`, `message` y `errors` (en validaciones de campos).

## Reglas de Base de Datos y Flyway

- **Nunca** modificar manualmente una migración ya aplicada. Crear siempre una nueva versión secuencial (`V2__...sql`, `V3__...sql`, etc.).
- Mantener `spring.jpa.hibernate.ddl-auto=validate`.

## Skill de Flujo de Trabajo

Para crear un nuevo módulo CRUD siguiendo esta arquitectura paso a paso, consultar el skill:
[SKILL.md](.agents/skills/spring-boot-layered-crud/SKILL.md)

