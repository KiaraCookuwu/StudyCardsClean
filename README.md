# 📚 StudyCards - Clean Architecture

**StudyCards** es una aplicación nativa de Android diseñada para optimizar el estudio mediante el uso de tarjetas de memoria (Flashcards). Permite a los estudiantes organizar sus apuntes por **Materias** y **Temas**, facilitando el repaso activo y la memorización visual.

Este proyecto destaca por haber sido migrado desde una arquitectura monolítica hacia una **Clean Architecture** robusta, escalable y modular, siguiendo las mejores prácticas de la industria actual.

---

## 🚀 Características Principales

* **Organización Jerárquica:** Estructura de Materias > Temas > Tarjetas.
* **Flashcards Interactivas:** Tarjetas con animación de volteo (Flip) que muestran el término al frente y la definición/imagen al reverso.
* **Soporte Multimedia:** Capacidad para agregar imágenes a las tarjetas usando la galería del dispositivo.
* **Búsqueda Global Inteligente:** Encuentra cualquier tarjeta buscando por término o definición, sin importar en qué materia se encuentre.
* **Persistencia de Datos:** Todos los datos se guardan localmente para estudiar sin conexión a internet.
* **Configuración de Usuario:** Preferencias guardadas (como vistas o temas) usando DataStore.

---

## 🛠️ Stack Tecnológico

El proyecto está construido 100% en **Kotlin** utilizando las bibliotecas más modernas de Android Jetpack:

* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3).
* **Arquitectura:** Clean Architecture + MVVM (Model-View-ViewModel).
* **Inyección de Dependencias:** [Hilt](https://dagger.dev/hilt/) (Dagger).
* **Base de Datos Local:** [Room](https://developer.android.com/training/data-storage/room) con SQLite.
* **Persistencia Ligera:** [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore).
* **Asincronía:** Kotlin Coroutines & Flow.
* **Carga de Imágenes:** [Coil](https://coil-kt.github.io/coil/).
* **Navegación:** Navigation Compose con paso de argumentos tipados.

---

## 🏗️ Arquitectura del Proyecto

El código está estructurado siguiendo estrictamente los principios de **Clean Architecture**, dividiendo el software en tres capas concéntricas para asegurar la independencia y testabilidad:

### 1. 🧠 Capa de Dominio (`domain`)
Es el núcleo de la aplicación. No tiene dependencias de Android ni de librerías de terceros.
* **Modelos:** Clases de datos puras (`Subject`, `Topic`, `Card`).
* **Repositorios (Interfaces):** Contratos que definen *qué* datos se necesitan.
* **Casos de Uso (UseCases):** Contienen la lógica de negocio pura (ej: `SaveTopicWithCardsUseCase`, `SearchCardsUseCase`).

### 2. 📦 Capa de Datos (`data`)
Responsable de suministrar los datos a la aplicación.
* **Local:** Implementación de la base de datos con **Room** (Entidades y DAOs).
* **DataStore:** Gestión de preferencias de usuario clave-valor.
* **Repositorio (Implementación):** Coordina las fuentes de datos y mapea las entidades de base de datos a modelos de dominio.

### 3. 🎨 Capa de Presentación (`presentation`)
Lo que ve el usuario.
* **UI:** Pantallas construidas con Composables (`HomeScreen`, `FlashcardScreen`).
* **ViewModels:** Gestionan el estado de la UI y se comunican con la capa de dominio a través de los Casos de Uso. Utilizan `HiltViewModel` para la inyección.

---

## 🔧 Configuración y Ejecución

Para correr este proyecto localmente:

1.  Clona el repositorio:
    ```bash
    git clone [https://github.com/TuUsuario/StudyCardsClean.git](https://github.com/TuUsuario/StudyCardsClean.git)
    ```
2.  Abre el proyecto en **Android Studio Koala** (o superior).
3.  Espera a que Gradle sincronice las dependencias.
    * *Nota: Este proyecto utiliza una configuración híbrida de KSP (para Room) y KAPT (para Hilt) para asegurar la compatibilidad.*
4.  Ejecuta la app en un emulador o dispositivo físico.

---

## 👤 Autor

Desarrollado por **Zianya Tayde Joffre Gonzalez e Ismael Morales Diaz**.
Estudiantes de Ingeniería en Informática.

---
