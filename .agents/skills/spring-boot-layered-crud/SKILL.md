---
name: spring-boot-layered-crud
description: >-
  Guía paso a paso para agregar nuevas tablas, entidades, DTOs, repositorios,
  servicios y controladores REST CRUD en el backend Spring Boot siguiendo la
  arquitectura lineal por capas con Flyway, Lombok, Swagger y PostgreSQL.
  Úsalo cuando el usuario pida crear una nueva tabla, entidad, módulo o CRUD
  en backend/.
---

# Crear un Nuevo Módulo CRUD (Arquitectura Lineal)

Sigue este orden exacto cada vez que agregues un nuevo recurso al proyecto `backend/`.

## Pasos

1. **Migración Flyway (`backend/src/main/resources/db/migration/`)**
   - Revisa la última versión existente (`V1__...`, `V2__...`) y crea el siguiente archivo secuencial `V<N>__<descripcion>.sql`.
   - Define `BIGSERIAL PRIMARY KEY`, restricciones `NOT NULL`, `UNIQUE` y llaves foráneas `REFERENCES`.

2. **Entidad JPA (`com.semilleros.backend.entity`)**
   - Crea `<Recurso>.java` con `@Entity`, `@Table(name = "...")`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`.
   - Usa `@GeneratedValue(strategy = GenerationType.IDENTITY)` en `id` (`Long`).
   - Verifica que cada columna y longitud coincida con el SQL de Flyway (`ddl-auto=validate`).
   - Usa `FetchType.LAZY` en relaciones `@ManyToOne`.

3. **DTOs (`com.semilleros.backend.dto`)**
   - Crea `<Recurso>RequestDTO.java` con `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Schema` y validaciones Jakarta (`@NotBlank`, `@NotNull`, `@Size`, `@Email`).
   - Crea `<Recurso>ResponseDTO.java` con `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor` y `@Schema`.

4. **Repositorio (`com.semilleros.backend.repository`)**
   - Crea `<Recurso>Repository.java` extendiendo `JpaRepository<<Recurso>, Long>`.
   - Agrega métodos `existsBy...` necesarios para validar unicidad o integridad referencial antes de eliminar.

5. **Servicio (`com.semilleros.backend.service`)**
   - Crea `<Recurso>Service.java` con `@Service` y `@RequiredArgsConstructor`.
   - Implementa `findAll`, `findById`, `create`, `update`, `delete` usando `@Transactional(readOnly = true)` para lecturas y `@Transactional` para escrituras.
   - Lanza `ResourceNotFoundException` cuando no exista el registro y `BusinessException` cuando se viole una regla de negocio (ej. campo único duplicado o registro con dependencias).
   - Mapea Entidad ↔ DTO dentro del servicio (o mediante métodos privados `toResponseDTO`).

6. **Controlador REST (`com.semilleros.backend.controller`)**
   - Crea `<Recurso>Controller.java` con `@RestController`, `@RequestMapping("/api/<recursos>")`, `@RequiredArgsConstructor` y `@Tag`.
   - Expone `GET` (listar y por ID), `POST` (`201 Created`), `PUT` (`200 OK`) y `DELETE` (`204 No Content`).
   - Usa `@Valid @RequestBody` en `POST` y `PUT` y documenta cada endpoint con `@Operation`.

7. **Pruebas Unitarias (`backend/src/test/java/com/semilleros/backend/service/`)**
   - Crea `<Recurso>ServiceTest.java` usando `@ExtendWith(MockitoExtension.class)` cubriendo creación exitosa, validaciones de negocio y manejo de `ResourceNotFoundException`.

## Verificación

Ejecuta dentro de `backend/`:

```bash
mvn test
mvn spring-boot:run
```

Comprueba que Flyway aplique la migración sin errores, que Hibernate valide el esquema (`ddl-auto=validate`) y que los nuevos endpoints aparezcan en `http://localhost:8080/swagger-ui.html`.

