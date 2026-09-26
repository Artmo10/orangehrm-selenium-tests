# OrangeHRM – Automatización del alta de empleados (PIM)

Proyecto final de automatización con **Selenium WebDriver + Java + TestNG**, usando el patrón **Page Object Model**.

Sitio bajo prueba: https://opensource-demo.orangehrmlive.com/

---

## Caso de negocio automatizado

En una sola prueba (`EmployeeLifecycleTests.testCreateAndSearchEmployee`):

1. Iniciar sesión como administrador.
2. Ir al módulo **PIM** desde el menú lateral.
3. Crear un empleado nuevo con:
   - Nombre, segundo nombre y apellido.
   - Employee Id (se captura el valor que genera la aplicación).
   - Datos de usuario (activando el switch *Create Login Details*): usuario, contraseña y estado.
4. Buscar el empleado en **Employee List** por su Employee Id.
5. Verificar que aparece en la grilla de resultados, validando que:
   - El **Id** de la fila coincide exactamente con el generado.
   - El **nombre** de la fila corresponde al nombre único creado en la corrida.

Fuera de alcance: pestaña Personal Details, contactos, cargo, salario, dependientes y foto.

---

## Tecnologías

| Herramienta | Versión |
|---|---|
| Java (JDK) | 25 |
| Maven | 3.x |
| Selenium (chrome-driver, firefox-driver, support) | 4.48.0 |
| TestNG | 7.12.0 |
| Jackson Databind | 2.17.2 |
| Maven Surefire Plugin | 3.5.2 |

Los drivers de los navegadores se resuelven automáticamente con **Selenium Manager** (incluido en Selenium 4), no hace falta descargarlos a mano.

**Requisitos en la máquina:** JDK 25, Maven, Google Chrome y Mozilla Firefox instalados.

---

## Estructura del proyecto

```
orangeHRM/
├── src/
│   ├── main/java/pages/
│   │   ├── LoginPage.java          # Login
│   │   ├── DashboardPage.java      # Verificación de login exitoso
│   │   ├── SidebarMenu.java        # Menú lateral (componente compartido)
│   │   ├── EmployeeListPage.java   # Búsqueda y grilla de empleados
│   │   └── AddEmployeePage.java    # Formulario de alta de empleado
│   │
│   └── test/
│       ├── java/
│       │   ├── base/BaseTest.java              # Setup/teardown del driver (Chrome/Firefox)
│       │   ├── pim/EmployeeLifecycleTests.java # La prueba del caso de negocio
│       │   └── utils/
│       │       ├── Employee.java               # Modelo de datos del empleado
│       │       ├── EmployeeDataProvider.java   # Lee el JSON y expone el @DataProvider
│       │       └── UniqueNameGenerator.java    # Genera el sufijo único por corrida
│       └── resources/testdata/
│           └── employees.json                  # Datos de los 2 empleados
│
├── testng.xml   # Suite: ejecuta el caso en Chrome y en Firefox
└── pom.xml
```

---

## Decisiones de diseño

### Page Object Model
- La prueba **no contiene locators ni búsquedas de elementos**; se lee como la descripción del caso de negocio.
- En cada página los **locators están agrupados al inicio**, separados de los métodos de acción.
- Las páginas **no hacen aserciones**: devuelven valores (`boolean`, `String`) y todas las aserciones viven en la prueba.
- `SidebarMenu` es un componente independiente del Dashboard, porque el menú está presente en todas las pantallas después del login.

### Datos de prueba
- Los datos de los empleados se cargan desde `src/test/resources/testdata/employees.json` usando Jackson.
- El `@DataProvider` entrega **dos empleados distintos**, por lo que la prueba se ejecuta una vez por cada uno.

### Nombre único por corrida
- **Del archivo** vienen: nombre base, segundo nombre, apellido, usuario base, contraseña y estado.
- **En ejecución** se agrega un sufijo con timestamp (`_<epochSeconds>`) al **nombre** y al **usuario**.
  - Ejemplo: `Carlos` → `Carlos_1758790421`.
  - El sufijo en el usuario evita el error de "usuario ya existente", ya que la demo es compartida y la suite crea 4 empleados por corrida.

### Búsqueda y verificación
- La búsqueda se hace por **Employee Id**, capturado del formulario antes de guardar. Es un identificador inequívoco y evita la ambigüedad del autocompletado del campo *Employee Name*.
- La verificación lee la grilla por columnas: busca la fila cuyo **Id es exactamente igual** al generado y valida que su nombre **empiece con el nombre único** de la corrida.

### Estabilidad
- Esperas explícitas (`WebDriverWait`) en todas las interacciones; no se usa `Thread.sleep`.
- Se espera a que desaparezca el loader del formulario (`.oxd-form-loader`) antes de interactuar.
- El switch *Create Login Details* y los radio de estado se clickean sobre su elemento visual (`span`), porque el `input` nativo está oculto.
- Después de *Search* se espera a que la grilla anterior se recargue (`stalenessOf`) antes de leer los resultados.
- Se verifica el mensaje *Successfully Saved* antes de continuar con la búsqueda.
- En Chrome se desactiva el gestor de contraseñas para evitar el aviso de "contraseña filtrada", que roba el foco.

---

## Cómo ejecutar

### Opción 1 – Suite completa desde IntelliJ (recomendado para la entrega)
Click derecho sobre `testng.xml` → **Run 'testng.xml'**.

Resultado esperado: **4 ejecuciones** (2 empleados × 2 navegadores: Chrome y Firefox).

### Opción 2 – Suite completa desde la terminal (sin IDE)
En la raíz del proyecto:

```bash
mvn clean test
```

El `pom.xml` configura `maven-surefire-plugin` para usar `testng.xml` como suite, así que Maven ejecuta el mismo caso en Chrome y en Firefox: 4 ejecuciones.

### Opción 3 – Solo la clase de prueba (desarrollo / depuración)
Click derecho sobre `EmployeeLifecycleTests.java` → **Run**.

Esta forma **no usa** `testng.xml`: el navegador toma el valor por defecto (`chrome`), por lo que se ven **2 ejecuciones, ambas en Chrome**. Sirve para depurar rápido, pero no cubre el requisito de dos navegadores.

---

## Credenciales

Usuario administrador de la demo pública (publicadas en la pantalla de login del sitio):

- Usuario: `Admin`
- Contraseña: `admin123`
