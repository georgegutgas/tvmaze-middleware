# TVMaze Middleware API 🚀💚

API Middleware desarrollada con **Spring Boot 4** y **MongoDB Atlas** para la gestión y enriquecimiento de comentarios y calificaciones de series de TV, incorporando estándares avanzados de arquitectura y seguridad web.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 4.x
* **Base de Datos:** MongoDB Atlas (NoSQL)
* **Seguridad:**
  * **Jsoup:** Sanitización contra ataques XSS (Cross-Site Scripting).
* **Herramientas de Construcción:** Maven

---

## ⚙️ Configuración y Ejecución Local

### Prerrequisitos
* Java 21 SDK instalado.
* Maven instalado (o utilizar el wrapper `./mvnw`).
* Cuenta en MongoDB Atlas con una lista blanca de IP configurada (`Network Access`).

### 1. Exportar la cadenas de credenciales por la variable de entorno MONGODB_URI y correrlo
```bash
export MONGODB_URI="mongodb+srv://usuario:password@cluster0.mongodb.net/tvmaze_db?retryWrites=true&w=majority"
./mvnw spring-boot:run
