# Refugio de mascotas — Sistema de adopción

Proyecto final de **Lógica de Programación** (2026-2), proyecto 13 de 15: *Adopción de mascotas*.
Aplicación web hecha en Java con Vaadin.

- **Autor:** Jesus Valencia Rey
- **Fecha de entrega:** 11 de noviembre de 2026

## Descripción

Un refugio de animales recibe solicitudes de adopción por redes sociales y pierde el rastro de cuáles ya revisó, de la salud de cada mascota y de las donaciones. Esta aplicación centraliza esa información: el catálogo de mascotas, los adoptantes, las solicitudes de adopción, el historial médico y las donaciones.

## Requisitos del proyecto

Requisitos propios del proyecto:

- [ ] Mascotas con nombre, especie, raza, edad, sexo, estado de salud, foto (por URL) y estado (disponible, en proceso o adoptada)
- [ ] Adoptantes con sus datos, tipo de vivienda, si tienen otras mascotas y sus preferencias
- [ ] Solicitudes pendientes, aprobadas o rechazadas, con observación. Al aprobar una, la mascota queda adoptada y las demás solicitudes pendientes por ella se rechazan solas
- [ ] Historial médico básico de cada mascota (vacunas y consultas) y alerta de las mascotas que necesitan atención urgente
- [ ] Registro de donaciones y estadísticas de adopciones por mes

Componentes investigados:

- [ ] `Image`: foto de cada mascota en el catálogo y en su ficha
- [ ] `TabSheet`: ficha de la mascota en pestañas (General, Médica y Adopción)
- [ ] `Badge`: estado de la mascota y de cada solicitud

Núcleo común:

- [ ] CRUD completo (crear, ver, modificar y eliminar)
- [ ] Grid con búsqueda en tiempo real y al menos un filtro
- [ ] Al menos 3 vistas con `@Route`, navegables desde el menú de un `AppLayout`
- [ ] Validación de formularios con mensajes en `Notification`
- [ ] Confirmación antes de borrar
- [ ] Datos en archivos `.txt` que sobreviven a un reinicio, con manejo de errores (try/catch)
- [ ] Funciona en la pantalla del celular

Bonificación (opcional):

- [ ] Puntaje de compatibilidad adoptante–mascota (0 a 100)

## Tecnologías

- Java 21
- Spring Boot (proyecto creado con Spring Initializr desde VS Code)
- Vaadin 25 (solo componentes gratuitos)
- Archivos de texto (`.txt`) con campos separados por `|` para guardar los datos

## Manual de uso

*Pendiente: se completará con capturas de pantalla cuando las vistas estén terminadas.*

## Bitácora de avance

### 22 de octubre de 2026 — Creación del proyecto

- Proyecto creado con Spring Initializr desde VS Code (Java 21, dependencia Vaadin, Group Id `com.example`, Artifact Id `adopcion`).
- Primera vista (`views/MainView.java`) en la ruta de inicio, con el título y la descripción del sistema, para comprobar que la aplicación arranca.
- README inicial con la descripción del proyecto, la lista de requisitos y las instrucciones para ejecutarlo.