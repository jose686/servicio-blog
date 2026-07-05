# 📝 Servicio Blog - Content Engine

> **Módulo Central de Contenido para Kapitán Kernel**

Este servicio opera como el **motor de contenido principal** de la plataforma. Está diseñado específicamente para gestionar de forma integral las **publicaciones (posts)**, las **taxonomías (categorías y etiquetas)**, y servir como **biblioteca de medios multimedia (imágenes)**, proporcionando una base robusta para la creación y distribución de contenido.

---

## 🏗️ Arquitectura Estrictamente Modular

> [!NOTE]
> **Independencia Garantizada**
> Este componente ha sido concebido bajo una arquitectura estrictamente modular, funcionando en la práctica como una **librería o microservicio independiente**. Garantiza una separación de responsabilidades limpia dentro del ecosistema de **Kapitán Kernel**, evitando cualquier acoplamiento innecesario con otros dominios como la gestión de usuarios o la tienda.

---

## 🛠️ Stack Tecnológico y Dependencias

Extraído directamente de nuestro `pom.xml`, el módulo está construido sobre las siguientes tecnologías y dependencias clave para garantizar rendimiento y seguridad:

*   **Core Framework:** Spring Boot (v4.0.6 parent)
*   **Capa Web:** Spring Boot Starter WebMVC
*   **Persistencia de Datos:** 
    *   Spring Boot Starter Data JPA
    *   MySQL Connector/J (Driver de Base de Datos)
*   **Seguridad y Autenticación:** 
    *   Spring Boot Starter Security
    *   JJWT (Java JWT) versión `0.12.5` (api, impl, jackson)
*   **Validación de Datos:** Spring Boot Starter Validation
*   **Herramientas de Desarrollo:** Lombok (Reducción de boilerplate), Spring Boot DevTools.

---

## 🗺️ Mapa de Endpoints / Rutas

La API RESTful está estructurada de la siguiente manera, exponiendo los recursos necesarios para la gestión del blog:

### 📌 Posts (`/api/posts`)

| Método HTTP | Endpoint | Propósito |
| :--- | :--- | :--- |
| `GET` | `/api/posts` | Lista todos los posts disponibles en el sistema. |
| `GET` | `/api/posts/listar` | Lista posts aplicando filtros (ej. `?query=...&categoria=...`). |
| `GET` | `/api/posts/{slug}` | Obtiene los detalles de un post específico utilizando su URL amigable (slug). |
| `POST` | `/api/posts` | Crea una nueva publicación a partir de un DTO. |
| `DELETE` | `/api/posts/{slug}` | Elimina un post de la base de datos de manera definitiva mediante su slug. |

### 🏷️ Categorías (`/api/categorias`)

| Método HTTP | Endpoint | Propósito |
| :--- | :--- | :--- |
| `GET` | `/api/categorias` | Lista todas las categorías (taxonomías) del sistema. |
| `POST` | `/api/categorias` | Crea y registra una nueva categoría. |
| `DELETE` | `/api/categorias/{id}` | Elimina una categoría específica usando su ID. |

### 🖼️ Biblioteca Multimedia - Imágenes (`/api/imagenes`)

| Método HTTP | Endpoint | Propósito |
| :--- | :--- | :--- |
| `GET` | `/api/imagenes` | Lista todas las imágenes almacenadas en la biblioteca. |
| `POST` | `/api/imagenes/subir` | Sube una nueva imagen (Requiere `MultipartFile` y permite metadatos como `idUsuario` y `altText`). |
| `DELETE` | `/api/imagenes/{id}` | Elimina una imagen de la biblioteca mediante su ID. |

---

## 🛡️ Estrategia de Tests (WebMvcTest & Mockito)

Para asegurar la calidad y estabilidad de este módulo crítico, hemos blindado la lógica de negocio y los controladores mediante una suite de pruebas robusta:

*   **Aislamiento Total con Mockito:** Utilizamos `@Mock` e `@InjectMocks` (como se evidencia en `PostServiceImplTest`) para simular por completo la capa de persistencia (`PostRepository`, `ImagenRepository`, `CategoriaRepository`). Esto garantiza que la lógica de nuestros servicios se pruebe de forma aislada, sin depender de una base de datos real.
*   **Cobertura de Controladores:** Empleamos `@WebMvcTest` para levantar únicamente la capa web en las pruebas de los controladores. Esto nos permite validar rutas, respuestas HTTP, y mapeo de datos con extrema precisión.
*   **Ejecución Ultrarrápida:** Al no levantar el contexto completo de Spring Boot ni conexiones a base de datos reales durante los tests unitarios, nuestra suite de pruebas se ejecuta en milisegundos, favoreciendo un flujo de Integración Continua (CI) altamente eficiente.

---

## 🚀 Instrucciones de Ejecución

Para trabajar con este módulo en tu entorno local, utiliza los siguientes comandos a través del Maven Wrapper (`mvnw`):

### 🏃‍♂️ Arrancar el Módulo

Para ejecutar el servicio en modo de desarrollo:

```bash
./mvnw spring-boot:run
```
*(En Windows, puedes usar `mvnw.cmd spring-boot:run`)*

### 🧪 Ejecutar la Suite de Pruebas

Para validar la integridad del código ejecutando todos los tests unitarios:

```bash
./mvnw test
```
*(En Windows, puedes usar `mvnw.cmd test`)*
